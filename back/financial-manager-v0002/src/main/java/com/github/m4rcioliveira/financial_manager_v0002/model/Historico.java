package com.github.m4rcioliveira.financial_manager_v0002.model;


import com.github.m4rcioliveira.financial_manager_v0002.enums.ActionMessageEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Historico {

    private Long chatId;
    private Map<String, String> mensangens = new HashMap<>();
    private List<String> handlersExecutados = new ArrayList<>();
    private String textLast;

}
