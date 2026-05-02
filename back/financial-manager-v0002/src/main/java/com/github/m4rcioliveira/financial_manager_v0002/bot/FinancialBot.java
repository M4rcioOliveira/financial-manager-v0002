package com.github.m4rcioliveira.financial_manager_v0002.bot;

import com.github.m4rcioliveira.financial_manager_v0002.roteirizador.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@Slf4j
public class FinancialBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String token;
    private final TelegramClient telegramClient;

    public FinancialBot(@Value("${financial.bot.token}") String token) {
        this.token = token;
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    //https://rubenlagus.github.io/TelegramBotsDocumentation/lesson-9.html#let-s-go-to-code
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            log.info("Chat ID: {}", chat_id);

            SendMessage message = SendMessage.builder()
                    .chatId(chat_id)
                    .text(Template.MENU_INICIAL)
                    .parseMode("HTML")
                    .build();

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    //Pega do resources
//    public void enviarPdfDoResources(Long chatId) {
//
//        try {
//            ClassPathResource resource = new ClassPathResource("pdf/envioteste.pdf");
//            InputStream inputStream = resource.getInputStream();
//
//            SendDocument sendDocument = SendDocument.builder()
//                    .chatId(chatId.toString())
//                    .document(new InputFile(inputStream, "envioteste.pdf"))
//                    .caption("PDF de teste 📄")
//                    .build();
//
//            telegramClient.execute(sendDocument);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    public void enviarPdf(Long chatId, byte[] pdfBytes) {

        try {
            InputStream inputStream = new ByteArrayInputStream(pdfBytes);

            SendDocument sendDocument = SendDocument.builder()
                    .chatId(chatId.toString())
                    .document(new InputFile(inputStream, "fatura.pdf"))
                    .caption("PDF gerado 📄")
                    .build();

            telegramClient.execute(sendDocument);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
