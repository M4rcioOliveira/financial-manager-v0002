package com.github.m4rcioliveira.financial_manager_v0002.enums;

import java.util.List;

public enum PagamentoStatusEnum {

    PAGO,
    PENDENTE,
    VENCIDO;

    public static List<PagamentoStatusEnum> pagaveis() {
        return List.of(PENDENTE, VENCIDO);
    }

}
