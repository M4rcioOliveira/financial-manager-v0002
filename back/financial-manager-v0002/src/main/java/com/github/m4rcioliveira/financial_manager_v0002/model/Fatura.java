package com.github.m4rcioliveira.financial_manager_v0002.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fatura {

    private String referencia;
    private List<Despesa> despesas = new ArrayList<>();
    private BigDecimal valorTotal;

}
