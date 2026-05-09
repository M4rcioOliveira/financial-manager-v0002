package com.github.m4rcioliveira.financial_manager_v0002.service;

import com.github.m4rcioliveira.financial_manager_v0002.dto.CriarDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.DespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import com.github.m4rcioliveira.financial_manager_v0002.enums.PagamentoStatusEnum;
import com.github.m4rcioliveira.financial_manager_v0002.exception.NotFoundException;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;
import com.github.m4rcioliveira.financial_manager_v0002.dto.FaturaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.model.User;
import com.github.m4rcioliveira.financial_manager_v0002.repository.DespesaRepository;
import com.github.m4rcioliveira.financial_manager_v0002.security.util.AutenticacaoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DespesaService {

    private static final Integer QTD_PARCELA_DEFAULT = 1;

    private final DespesaRepository despesaRepository;

    private final ApplicationEventPublisher publisher;

    private final UserService userService;


    @Transactional
    public void criarNovaDespesa(CriarDespesaDTO criarDespesaDTO) {

        List<Despesa> despesas = new ArrayList<>();

        UUID idUnico = UUID.randomUUID();

        User user = userService.buscarUserPorId(AutenticacaoUtil.getAuthenticatedUserId());

        for (int i = 0; i < criarDespesaDTO.qtdParcelas(); i++) {

            Despesa despesa = novaDespesaDTOToDespesa(criarDespesaDTO);
            despesa.setIdUnico(idUnico);
            despesa.setStatusPagamento(PagamentoStatusEnum.PENDENTE);
            despesa.setUser(user);

            if (i != 0) {
                despesa.setDataVencimento(despesa.getDataVencimento().plusMonths(i));
            }

            despesa.setReferencia(String.valueOf(i + 1) + "/" + despesa.getQtdParcelas());

            despesas.add(despesa);
        }


        despesaRepository.saveAll(despesas);

    }


    //Paga uma unica despesa/parcela
    @Transactional
    public void pagarDespesa(UUID id) {

        Despesa despesa = despesaRepository
                .findByIdAndUserIdAndStatusPagamentoIn(id,
                        AutenticacaoUtil.getAuthenticatedUserId(),
                        PagamentoStatusEnum.pagaveis())
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Despesa {0} não encontrada", id)));

        despesa.setStatusPagamento(PagamentoStatusEnum.PAGO);
        despesa.setDataPagamento(LocalDateTime.now());

    }

    public List<DespesaDTO> obterDespesasDetalhadas() {

        UUID userId = AutenticacaoUtil.getAuthenticatedUserId();

        List<Despesa> despesas = despesaRepository.findAllByUserId(userId);
        List<DespesaDTO> despesasDetalhadas = new ArrayList<>();

        if (despesas.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Despesas não encontradas para o usuário {0}", userId));
        }

        for (Despesa despesa : despesas) {
            despesasDetalhadas.add(DespesaDTO.from(despesa));
        }

        return despesasDetalhadas;

    }

    public FaturaDTO gerarFatura(LocalDate inicio, LocalDate fim) {

        UUID userId = AutenticacaoUtil.getAuthenticatedUserId();

        List<Despesa> despesas = despesaRepository.findAllByUserIdAndDataVencimentoGreaterThanEqualAndDataVencimentoLessThan(userId, inicio, fim);

        if (despesas.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Despesas não encontrada para as informações passadas." +
                    "Usuário {0} -  Data de Inicio {1} - Data de fim {2}", userId, inicio, fim));
        }

        FaturaDTO faturaDTO = new FaturaDTO();

        List<DespesaDTO> despesasDTO = new ArrayList<>();

        for(Despesa despesa : despesas){
            despesasDTO.add(DespesaDTO.from(despesa));
        }

        faturaDTO.setDespesas(despesasDTO);
        String mesPtBr = inicio.getMonth().getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR"));
        faturaDTO.setReferencia(mesPtBr + inicio.getYear());
        faturaDTO.setValorTotal(BigDecimal.ZERO);
        faturaDTO.setValorTotalPago(BigDecimal.ZERO);
        faturaDTO.setValorTotalPendente(BigDecimal.ZERO);

        for (Despesa despesa : despesas) {
            faturaDTO.setValorTotal(faturaDTO.getValorTotal().add(despesa.getValorParcela()));
            if(PagamentoStatusEnum.PAGO.equals(despesa.getStatusPagamento())){
                faturaDTO.setValorTotalPago(faturaDTO.getValorTotalPago().add(despesa.getValorParcela()));
            }
            if(PagamentoStatusEnum.pagaveis().contains(despesa.getStatusPagamento())){
                faturaDTO.setValorTotalPendente(faturaDTO.getValorTotalPendente().add(despesa.getValorParcela()));
            }
        }

        publisher.publishEvent(faturaDTO);

        return faturaDTO;

    }


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
