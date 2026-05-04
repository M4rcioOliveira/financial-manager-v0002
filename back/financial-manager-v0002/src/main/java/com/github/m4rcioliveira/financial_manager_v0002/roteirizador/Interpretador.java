package com.github.m4rcioliveira.financial_manager_v0002.roteirizador;

import com.github.m4rcioliveira.financial_manager_v0002.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Interpretador {

    private final DespesaService despesaService;

    public String interpretadorOpcoes(String mensagem) {

        return null;

    }

}
