package com.github.m4rcioliveira.financial_manager_v0002.dto;

import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record NovaDespesaDTO(

        String nome,

        String descricao,

        BigDecimal valorTotal,

        Boolean fixa,

        Integer qtdParcelas,

        Boolean parcelada,

        LocalDate dataVencimento,

        CategoriaEnum categoria

) {
}
