package com.github.m4rcioliveira.financial_manager_v0002.service;

import com.github.m4rcioliveira.financial_manager_v0002.dto.ListaDetalhadaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.NovaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import com.github.m4rcioliveira.financial_manager_v0002.exception.NotFoundException;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;
import com.github.m4rcioliveira.financial_manager_v0002.repository.DespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private static final Integer QTD_PARCELA_DEFAULT = 1;

    private final DespesaRepository despesaRepository;

    public void criarNovaDespesa(NovaDespesaDTO novaDespesaDTO) {

        List<Despesa> despesas = new ArrayList<>();

        Despesa despesa = novaDespesaDTOToDespesa(novaDespesaDTO);


        for (int i = 0; i < despesa.getQtdParcelas(); i++) {

            if (!despesa.getDataVencimento().isEqual(LocalDate.now())) {
                despesa.setDataVencimento(despesa.getDataVencimento().plusMonths(1L));
            }

            despesas.add(despesa);
        }

        despesaRepository.saveAllAndFlush(despesas);

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
