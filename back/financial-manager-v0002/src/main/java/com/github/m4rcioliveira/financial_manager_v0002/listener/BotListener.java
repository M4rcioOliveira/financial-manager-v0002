package com.github.m4rcioliveira.financial_manager_v0002.listener;

import com.github.m4rcioliveira.financial_manager_v0002.bot.FinancialBot;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.MensagemEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotListener {

    private final FinancialBot financialBot;

    @Async
    @EventListener
    public void enviarMensagem(MensagemEvent mensagemEvent){
        financialBot.enviarPdf(mensagemEvent.getChatId(), mensagemEvent.getPdfBytes());
    }

    @Async
    @EventListener
    public void receberMensagem(MensagemEvent mensagemEvent){

        //TODO Tratar recebimento

    }

}
