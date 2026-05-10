package com.github.m4rcioliveira.financial_manager_v0002.controller;

import com.github.m4rcioliveira.financial_manager_v0002.constantes.ArquiteturaConstantes;
import com.github.m4rcioliveira.financial_manager_v0002.dto.DespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.CriarDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.ResponseBaseDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.FaturaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ArquiteturaConstantes.BASE_PATH_REQUEST_MAPPING + "/despesa")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @PostMapping()
    public ResponseEntity<Void> criarNovaDespesa(@RequestBody List<CriarDespesaDTO> despesasDTO) {

        despesaService.criarNovaDespesa(despesasDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<ResponseBaseDTO<List<DespesaDTO>>> despesasDetalhadas() {

        ResponseBaseDTO<List<DespesaDTO>> responseBaseDTO = new ResponseBaseDTO<>();
        responseBaseDTO.setData(despesaService.obterDespesasDetalhadas());

        return ResponseEntity.ok(responseBaseDTO);

    }

    @PostMapping("/pagamento/{id}")
    public ResponseEntity<Void> pagarDespesa(@PathVariable("id") UUID id) {
        despesaService.pagarDespesa(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/fatura")
    public ResponseEntity<FaturaDTO> gerarFatura(
            @RequestParam int ano,
            @RequestParam int mes
    ) {

        LocalDate inicio = YearMonth.of(ano, mes).atDay(1);
        LocalDate fim = inicio.plusMonths(1);

        return ResponseEntity.ok(despesaService.gerarFatura(inicio, fim));

    }

    //Pdf devolvido no controller
//    @GetMapping("/fatura/pdf")
//    public ResponseEntity<byte[]> gerar() {
//
//        LocalDate inicio = YearMonth.of(2026, 6).atDay(1);
//        LocalDate fim = inicio.plusMonths(1);
//
//        Fatura fatura = despesaService.gerarFatura(inicio, fim);
//
//
//        byte[] pdf = despesaService.gerarFaturaPdf(fatura);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fatura.pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdf);
//    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
