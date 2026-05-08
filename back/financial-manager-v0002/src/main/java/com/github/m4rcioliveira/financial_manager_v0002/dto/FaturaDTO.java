package com.github.m4rcioliveira.financial_manager_v0002.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;
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
public class FaturaDTO {

    private String referencia;
    private List<Despesa> despesas = new ArrayList<>();
    private BigDecimal valorTotal;

}
