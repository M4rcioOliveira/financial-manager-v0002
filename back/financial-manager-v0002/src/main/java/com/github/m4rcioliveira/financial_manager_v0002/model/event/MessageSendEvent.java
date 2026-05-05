package com.github.m4rcioliveira.financial_manager_v0002.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageSendEvent extends MessageBaseEvent {

    public MessageSendEvent(Long chatId, String text, byte[] pdfBytes) {
        super(chatId, text, pdfBytes);
    }

}
