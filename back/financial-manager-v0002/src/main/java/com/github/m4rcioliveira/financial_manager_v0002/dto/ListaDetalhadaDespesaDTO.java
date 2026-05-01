package com.github.m4rcioliveira.financial_manager_v0002.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import com.github.m4rcioliveira.financial_manager_v0002.enums.PagamentoStatusEnum;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record ListaDetalhadaDespesaDTO(

        UUID id,

        UUID idUnico,

        String nome,

        String descricao,

        BigDecimal valorTotal,

        BigDecimal valorParcela,

        Boolean fixa,

        Integer qtdParcelas,

        LocalDate dataVencimento,

        LocalDateTime dataPagamento,

        CategoriaEnum categoria,

        PagamentoStatusEnum statusPagamento

) {

    //Trocar por mapper
    public static ListaDetalhadaDespesaDTO from(Despesa despesa) {
        return new ListaDetalhadaDespesaDTO(
                despesa.getId(),
                despesa.getIdUnico(),
                despesa.getNome(),
                despesa.getDescricao(),
                despesa.getValorTotal(),
                despesa.getValorParcela(),
                despesa.getFixa(),
                despesa.getQtdParcelas(),
                despesa.getDataVencimento(),
                despesa.getDataPagamento(),
                despesa.getCategoria(),
                despesa.getStatusPagamento()
        );
    }
}
