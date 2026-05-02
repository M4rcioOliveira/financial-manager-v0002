package com.github.m4rcioliveira.financial_manager_v0002.service;

import com.github.m4rcioliveira.financial_manager_v0002.dto.ListaDetalhadaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.NovaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import com.github.m4rcioliveira.financial_manager_v0002.enums.PagamentoStatusEnum;
import com.github.m4rcioliveira.financial_manager_v0002.exception.NotFoundException;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;
import com.github.m4rcioliveira.financial_manager_v0002.model.Fatura;
import com.github.m4rcioliveira.financial_manager_v0002.repository.DespesaRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private static final Integer QTD_PARCELA_DEFAULT = 1;

    private final DespesaRepository despesaRepository;

    private final TemplateEngine templateEngine;


    @Transactional
    public void criarNovaDespesa(NovaDespesaDTO novaDespesaDTO) {

        List<Despesa> despesas = new ArrayList<>();

        UUID idUnico = UUID.randomUUID();

        for (int i = 0; i < novaDespesaDTO.qtdParcelas(); i++) {

            Despesa despesa = novaDespesaDTOToDespesa(novaDespesaDTO);
            despesa.setIdUnico(idUnico);
            despesa.setStatusPagamento(PagamentoStatusEnum.PENDENTE);

            if (i != 0) {
                despesa.setDataVencimento(despesa.getDataVencimento().plusMonths(i));
            }

            despesas.add(despesa);
        }

        despesaRepository.saveAll(despesas);

    }


    @Transactional
    public void pagarDespesa(UUID id){

        List<Despesa> despesas = despesaRepository.findAllByIdAndStatusPagamentoIn(id, PagamentoStatusEnum.pagaveis());

        if (despesas.isEmpty()) {
            throw new NotFoundException("Despesas não encontradas!!!");
        }

        for(Despesa despesa : despesas) {
            despesa.setStatusPagamento(PagamentoStatusEnum.PAGO);
            despesa.setDataPagamento(LocalDateTime.now());
        }


    }


    public List<ListaDetalhadaDespesaDTO> obterDespesasDetalhadas() {

        List<Despesa> despesas = despesaRepository.findAll();
        List<ListaDetalhadaDespesaDTO> despesasDetalhadas = new ArrayList<>();

        if (despesas.isEmpty()) {
            throw new NotFoundException("Despesas não encontradas!!!");
        }

        for (Despesa despesa : despesas) {
            despesasDetalhadas.add(ListaDetalhadaDespesaDTO.from(despesa));
        }

        return despesasDetalhadas;

    }

    public Fatura gerarFatura(LocalDate inicio, LocalDate fim) {

        List<Despesa> despesas = despesaRepository.findByDataVencimentoGreaterThanEqualAndDataVencimentoLessThan(inicio, fim);

        if (despesas.isEmpty()) {
            throw new NotFoundException("Despesas não encontradas!!!");
        }

        Fatura fatura = new Fatura();

        fatura.setDespesas(despesas);
        fatura.setReferencia(inicio.getDayOfMonth() + String.valueOf(inicio.getMonth()));
        fatura.setValorTotal(BigDecimal.ZERO);

        for (Despesa despesa : despesas) {
            fatura.setValorTotal(fatura.getValorTotal().add(despesa.getValorParcela()));
        }

        return fatura;

    }

    public byte[] gerarFaturaPdf(Fatura fatura) {

        Context context = new Context();
        context.setVariable("fatura", fatura);

        String html = templateEngine.process("faturav2", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    //Trocar por mapper
    private Despesa novaDespesaDTOToDespesa(NovaDespesaDTO novaDespesaDTO) {
        Despesa despesa = new Despesa();

        despesa.setNome(novaDespesaDTO.nome());
        despesa.setDescricao(novaDespesaDTO.descricao());
        despesa.setValorTotal(novaDespesaDTO.valorTotal());
        despesa.setValorParcela(novaDespesaDTO.valorTotal().divide(BigDecimal.valueOf(novaDespesaDTO.qtdParcelas()), 2, RoundingMode.HALF_UP));
        despesa.setFixa(novaDespesaDTO.fixa());
        despesa.setQtdParcelas(novaDespesaDTO.qtdParcelas() == 0 ? QTD_PARCELA_DEFAULT : novaDespesaDTO.qtdParcelas());
        despesa.setDataVencimento(novaDespesaDTO.dataVencimento() == null ? LocalDate.now() : novaDespesaDTO.dataVencimento());
        despesa.setCategoria(novaDespesaDTO.categoria() == null ? CategoriaEnum.OUTRAS : novaDespesaDTO.categoria());

        return despesa;
    }


}
