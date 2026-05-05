package com.github.m4rcioliveira.financial_manager_v0002.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionMessageEnum {

    NOME("NOME", "Nome da despesa: ");

    private String name;
    private String description;

}
