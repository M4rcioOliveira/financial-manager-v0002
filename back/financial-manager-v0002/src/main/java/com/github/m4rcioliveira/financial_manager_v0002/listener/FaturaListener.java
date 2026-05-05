package com.github.m4rcioliveira.financial_manager_v0002.listener;

import com.github.m4rcioliveira.financial_manager_v0002.model.Fatura;
import com.github.m4rcioliveira.financial_manager_v0002.model.event.MensagemEvent;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

@Component
@RequiredArgsConstructor
public class FaturaListener {

    private final TemplateEngine templateEngine;

    private final ApplicationEventPublisher publisher;

    @Async
    @EventListener
    public void gerarFaturaPdf(Fatura fatura) {

        Context context = new Context();
        context.setVariable("fatura", fatura);

        String html = templateEngine.process("faturav2", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();

            byte[] bytePdf = out.toByteArray();

            publisher.publishEvent(new MensagemEvent(8014669915L,null, bytePdf));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

}
