package com.github.m4rcioliveira.financial_manager_v0002.model.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReceiveEvent extends MessageBaseEvent {

    public MessageReceiveEvent(Long chatId, String text, byte[] pdfBytes) {
        super(chatId, text, pdfBytes);
    }
}
