package com.github.m4rcioliveira.financial_manager_v0002.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageBaseEvent {

    private Long chatId;
    private String text;
    private byte[] pdfBytes;

}
