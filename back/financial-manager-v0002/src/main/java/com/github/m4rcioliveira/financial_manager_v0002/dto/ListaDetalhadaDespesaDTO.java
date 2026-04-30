package com.github.m4rcioliveira.financial_manager_v0002.dto;

import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ListaDetalhadaDespesaDTO(

        UUID id,

        String nome,

        String descricao,

        BigDecimal valorTotal,

        BigDecimal valorParcela,

        Boolean fixa,

        Integer qtdParcelas,

        Boolean parcelada,

        LocalDate dataVencimento,

        CategoriaEnum categoria,

        Boolean paga

) {

    //Trocar por mapper
    public static ListaDetalhadaDespesaDTO from(Despesa despesa) {
        return new ListaDetalhadaDespesaDTO(
                despesa.getId(),
                despesa.getNome(),
                despesa.getDescricao(),
                despesa.getValorTotal(),
                despesa.getValorParcela(),
                despesa.getFixa(),
                despesa.getQtdParcelas(),
                despesa.getParcelada(),
                despesa.getDataVencimento(),
                despesa.getCategoria(),
                despesa.getPaga()
        );
    }
}
