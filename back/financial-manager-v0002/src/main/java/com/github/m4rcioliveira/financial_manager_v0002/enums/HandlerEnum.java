package com.github.m4rcioliveira.financial_manager_v0002.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum HandlerEnum {

    NOVA_DESPESA(1, "novaDespesaMessageHandler", "Handler para criação de novas despesas e gastos.");

    private Integer option;
    private String name;
    private String description;

    private static final Map<Integer, HandlerEnum> MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(HandlerEnum::getOption, e -> e));

    public static String getNameByOption(Integer option) {
        HandlerEnum handler = MAP.get(option);
        if (handler == null) {
            throw new IllegalArgumentException("Opção inválida: " + option);
        }
        return handler.getName();
    }

}
