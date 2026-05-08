package com.github.m4rcioliveira.financial_manager_v0002.service;

import com.github.m4rcioliveira.financial_manager_v0002.dto.ListaDetalhadaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.CriarDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import com.github.m4rcioliveira.financial_manager_v0002.enums.PagamentoStatusEnum;
import com.github.m4rcioliveira.financial_manager_v0002.exception.NotFoundException;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;
import com.github.m4rcioliveira.financial_manager_v0002.model.Fatura;
import com.github.m4rcioliveira.financial_manager_v0002.repository.DespesaRepository;
import com.github.m4rcioliveira.financial_manager_v0002.security.util.AutenticacaoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DespesaService {

    private static final Integer QTD_PARCELA_DEFAULT = 1;

    private final DespesaRepository despesaRepository;

    private final ApplicationEventPublisher publisher;


    @Transactional
    public void criarNovaDespesa(CriarDespesaDTO criarDespesaDTO) {

        List<Despesa> despesas = new ArrayList<>();

        UUID idUnico = UUID.randomUUID();

        for (int i = 0; i < criarDespesaDTO.qtdParcelas(); i++) {

            Despesa despesa = novaDespesaDTOToDespesa(criarDespesaDTO);
            despesa.setIdUnico(idUnico);
            despesa.setStatusPagamento(PagamentoStatusEnum.PENDENTE);

            if (i != 0) {
                despesa.setDataVencimento(despesa.getDataVencimento().plusMonths(i));
            }

            despesas.add(despesa);
        }

        log.info("UUID do usuário: {}", AutenticacaoUtil.getAuthenticatedUserId());

        despesaRepository.saveAll(despesas);

    }


    @Transactional
    public void pagarDespesa(UUID id) {

        List<Despesa> despesas = despesaRepository.findAllByIdAndStatusPagamentoIn(id, PagamentoStatusEnum.pagaveis());

        if (despesas.isEmpty()) {
            throw new NotFoundException("Despesas não encontradas!!!");
        }

        for (Despesa despesa : despesas) {
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

        publisher.publishEvent(fatura);

        return fatura;

    }

    //Trocar por mapper
    private Despesa novaDespesaDTOToDespesa(CriarDespesaDTO criarDespesaDTO) {
        Despesa despesa = new Despesa();

        despesa.setNome(criarDespesaDTO.nome());
        despesa.setDescricao(criarDespesaDTO.descricao());
        despesa.setValorTotal(criarDespesaDTO.valorTotal());
        despesa.setValorParcela(criarDespesaDTO.valorTotal().divide(BigDecimal.valueOf(criarDespesaDTO.qtdParcelas()), 2, RoundingMode.HALF_UP));
        despesa.setFixa(criarDespesaDTO.fixa());
        despesa.setQtdParcelas(criarDespesaDTO.qtdParcelas() == 0 ? QTD_PARCELA_DEFAULT : criarDespesaDTO.qtdParcelas());
        despesa.setDataVencimento(criarDespesaDTO.dataVencimento() == null ? LocalDate.now() : criarDespesaDTO.dataVencimento());
        despesa.setCategoria(criarDespesaDTO.categoria() == null ? CategoriaEnum.OUTRAS : criarDespesaDTO.categoria());

        return despesa;
    }


}
