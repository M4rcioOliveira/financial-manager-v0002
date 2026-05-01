package com.github.m4rcioliveira.financial_manager_v0002.controller;

import com.github.m4rcioliveira.financial_manager_v0002.dto.ListaDetalhadaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.NovaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.model.ResponseBaseDTO;
import com.github.m4rcioliveira.financial_manager_v0002.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @PostMapping()
    public ResponseEntity<Void> criarNovaDespesa(@RequestBody NovaDespesaDTO novaDespesaDTO) {

        despesaService.criarNovaDespesa(novaDespesaDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<ResponseBaseDTO<List<ListaDetalhadaDespesaDTO>>> despesasDetalhadas() {

        ResponseBaseDTO<List<ListaDetalhadaDespesaDTO>> responseBaseDTO = new ResponseBaseDTO<>();
        responseBaseDTO.setData(despesaService.obterDespesasDetalhadas());

        return ResponseEntity.ok(responseBaseDTO);

    }

    @PostMapping("/pagamento/{id}")
    public ResponseEntity<Void> pagarDespesa(@PathVariable("id") UUID id) {
        despesaService.pagarDespesa(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
