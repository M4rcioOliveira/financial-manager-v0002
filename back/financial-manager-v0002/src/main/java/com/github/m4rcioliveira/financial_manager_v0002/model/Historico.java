package com.github.m4rcioliveira.financial_manager_v0002.model;


import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Historico {

    private Long chatId;
    private Map<String, String> mensagens = new HashMap<>();
    private List<String> handlersExecutados = new ArrayList<>();
    private String textLast;

}
