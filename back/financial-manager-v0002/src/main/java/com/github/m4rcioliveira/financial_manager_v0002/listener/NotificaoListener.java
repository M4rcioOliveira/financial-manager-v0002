package com.github.m4rcioliveira.financial_manager_v0002.listener;

import com.github.m4rcioliveira.financial_manager_v0002.bot.FinancialBot;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.EnviarPDFEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificaoListener {

    private final FinancialBot financialBot;

    @Async
    @EventListener
    public void enviarPdf(EnviarPDFEvent enviarPDFEvent){
        financialBot.enviarPdf(enviarPDFEvent.getChatId(), enviarPDFEvent.getPdfBytes());
    }

}
