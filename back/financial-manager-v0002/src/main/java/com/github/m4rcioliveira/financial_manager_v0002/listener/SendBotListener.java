package com.github.m4rcioliveira.financial_manager_v0002.listener;

import com.github.m4rcioliveira.financial_manager_v0002.bot.FinancialBot;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.MessageSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendBotListener {

    private final FinancialBot financialBot;

    @Async
    @EventListener
    public void enviarMensagem(MessageSendEvent messageSendEvent) {
        financialBot.enviarMensagem(messageSendEvent.getChatId(), messageSendEvent.getText(), messageSendEvent.getPdfBytes());
    }

}
