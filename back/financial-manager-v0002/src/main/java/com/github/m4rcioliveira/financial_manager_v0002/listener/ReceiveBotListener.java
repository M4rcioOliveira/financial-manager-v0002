package com.github.m4rcioliveira.financial_manager_v0002.listener;

import com.github.m4rcioliveira.financial_manager_v0002.bot.FinancialBot;
import com.github.m4rcioliveira.financial_manager_v0002.constantes.Constantes;
import com.github.m4rcioliveira.financial_manager_v0002.enums.HandlerEnum;
import com.github.m4rcioliveira.financial_manager_v0002.handler.IMessageHandler;
import com.github.m4rcioliveira.financial_manager_v0002.model.Historico;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.MessageReceiveEvent;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.MessageSendEvent;
import com.github.m4rcioliveira.financial_manager_v0002.service.cache.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceiveBotListener {

    private final FinancialBot financialBot;
    private final RedisService redisService;
    private final Map<String, IMessageHandler> handlers;
    private final ApplicationEventPublisher publisher;

    @Async
    @EventListener
    public void receberMensagem(MessageReceiveEvent messageReceiveEvent) {

        try {

            //TODO REFATORAR PARA NAO USAR CAST NA BUSCA, pois quando transforma em string fica sem 0 a esquerda
            Historico historico = (Historico) redisService.buscar(String.valueOf(messageReceiveEvent.getChatId()));

            String text = messageReceiveEvent.getText();
            Long chatId = messageReceiveEvent.getChatId();

            if (ObjectUtils.anyNotNull(historico)) {
                if(!historico.getHandlersExecutados().isEmpty()){
                    historico.setTextLast(text);
                    IMessageHandler handler = handlers.get(historico.getHandlersExecutados().getLast());
                    handler.execute(historico);
                }
            } else {
                if (validaSeNumeroEAteMaxSize(text)) {
                    IMessageHandler handler = handlers.get(HandlerEnum.getNameByOption(Integer.parseInt(text)));
                    handler.execute(historico);
                } else {
                    publicarMenuInicialDefault(chatId);
                }

            }
        } catch (Exception e) {
            log.error("Erro ao receber mensagem...", e);
        }

    }

    private void publicarMenuInicialDefault(Long chatId) {
        publisher.publishEvent(new MessageSendEvent(chatId, Constantes.OPCAO_INVALIDA, null));
        publisher.publishEvent(new MessageSendEvent(chatId, Constantes.MENU_INICIAL, null));
    }

    private boolean validaSeNumeroEAteMaxSize(String text) {
        if (text == null || !text.matches("\\d+")) {
            return false;
        }

        int numero = Integer.parseInt(text);
        return numero >= Constantes.MIN_SIZE && numero <= Constantes.MAX_SIZE_MENU_INICIAL;
    }

}
