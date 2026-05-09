package com.github.m4rcioliveira.financial_manager_v0002.schedule;

import com.github.m4rcioliveira.financial_manager_v0002.constantes.ArquiteturaConstantes;
import com.github.m4rcioliveira.financial_manager_v0002.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class EnviarParcelaSchedule {

    private final DespesaService despesaService;

    @Scheduled(cron = "0 0 8,18 * * *", zone = ArquiteturaConstantes.ZONE_ID_SP)
    public void execute() {

        LocalDate dataHoje = LocalDate.now();

        LocalDate inicio = YearMonth.of(dataHoje.getYear(), dataHoje.getMonthValue()).atDay(1);
        LocalDate fim = inicio.plusMonths(1);

        despesaService.gerarFatura(inicio, fim);
    }

}
