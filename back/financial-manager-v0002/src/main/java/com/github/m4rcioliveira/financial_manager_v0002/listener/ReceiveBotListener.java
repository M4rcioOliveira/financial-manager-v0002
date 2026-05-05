package com.github.m4rcioliveira.financial_manager_v0002.listener;

import com.github.m4rcioliveira.financial_manager_v0002.bot.FinancialBot;
import com.github.m4rcioliveira.financial_manager_v0002.enums.HandlerEnum;
import com.github.m4rcioliveira.financial_manager_v0002.handler.IMessageHandler;
import com.github.m4rcioliveira.financial_manager_v0002.model.Historico;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.MessageReceiveEvent;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.MessageSendEvent;
import com.github.m4rcioliveira.financial_manager_v0002.service.cache.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceiveBotListener {

    private final FinancialBot financialBot;
    private RedisService redisService;
    private Map<String, IMessageHandler> handlers;

    @Async
    @EventListener
    public void receberMensagem(MessageReceiveEvent messageReceiveEvent){

        Historico historico = (Historico) redisService.buscar(messageReceiveEvent.getChatId());

        if(ObjectUtils.anyNull()) {
            try {
               IMessageHandler handler = handlers.get(HandlerEnum.getNameByOption(Integer.valueOf(messageReceiveEvent.getText())));
               handler.execute(historico);
            } catch (NumberFormatException e) {
                log.error("Você informou um texto invés de um número.");
            }
        }


    }

}
