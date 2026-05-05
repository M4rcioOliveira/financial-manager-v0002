package com.github.m4rcioliveira.financial_manager_v0002.bot;

import com.github.m4rcioliveira.financial_manager_v0002.model.event.MessageReceiveEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@Slf4j
public class FinancialBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private static final String SEND_RECEIVE_MESSAGE_ERRO = "Falha ao enviar/receber mensagem. {}";
    private static final String CONSUME_MESSAGE_ERRO = "Falha ao consumir mensagem. {}";

    private final String token;
    private final TelegramClient telegramClient;
    private final ApplicationEventPublisher publisher;


    public FinancialBot(@Value("${financial.bot.token}") String token, ApplicationEventPublisher publisher) {
        this.token = token;
        this.telegramClient = new OkHttpTelegramClient(token);
        this.publisher = publisher;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {

                String mensagemTexto = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();

                publisher.publishEvent(new MessageReceiveEvent(chatId, mensagemTexto, null));

            }
        } catch (Exception e) {
            log.error(CONSUME_MESSAGE_ERRO, e, e);
        }
    }

    public void enviarMensagem(Long chatId, String text, byte[] pdfBytes) {
        if (ObjectUtils.anyNull(text)) {
            enviarTexto(chatId, text);
        } else {
            enviarPdf(chatId, pdfBytes);
        }
    }


    public void enviarTexto(Long chatId, String text) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode("HTML")
                    .build();
            telegramClient.execute(message);
        } catch (Exception e) {
            logErrorSendReceive(e);
        }
    }

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
            logErrorSendReceive(e);
        }
    }

    private void logErrorSendReceive(Exception e) {
        log.error(SEND_RECEIVE_MESSAGE_ERRO, e, e);
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

//}
//
//// 🔥 BOTÃO QUE ABRE O WEB APP
//private void enviarBotaoFormulario(Long chatId) {
//
//    InlineKeyboardButton botao = InlineKeyboardButton.builder()
//            .text("Abrir formulário 👤")
//            .webApp(new WebAppInfo("https://www.google.com/")) // ⚠️ troca aqui
//            .build();
//
//    InlineKeyboardRow row = new InlineKeyboardRow();
//    row.add(botao);
//
//    InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
//            .keyboardRow(row)
//            .build();
//
//    SendMessage message = SendMessage.builder()
//            .chatId(chatId.toString())
//            .text("Clique para preencher seu nome:")
//            .replyMarkup(markup)
//            .build();
//
//    try {
//        telegramClient.execute(message);
//    } catch (TelegramApiException e) {
//        e.printStackTrace();
//    }
//}


}