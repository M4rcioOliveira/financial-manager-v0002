package com.github.m4rcioliveira.financial_manager_v0002.handler;

import com.github.m4rcioliveira.financial_manager_v0002.model.Historico;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NovaDespesaMessageHandler implements IMessageHandler {

    @Override
    public void execute(Historico historico) {

        log.info("Chegou no handler do execute...");

    }
}
