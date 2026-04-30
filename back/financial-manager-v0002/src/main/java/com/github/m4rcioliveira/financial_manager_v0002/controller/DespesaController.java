package com.github.m4rcioliveira.financial_manager_v0002.controller;

import com.github.m4rcioliveira.financial_manager_v0002.dto.ListaDetalhadaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.NovaDespesaDTO;
import com.github.m4rcioliveira.financial_manager_v0002.model.ResponseBaseDTO;
import com.github.m4rcioliveira.financial_manager_v0002.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @PostMapping()
    public ResponseEntity<Void> criarNovaDespesa (@RequestBody NovaDespesaDTO novaDespesaDTO){

        despesaService.criarNovaDespesa(novaDespesaDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<ResponseBaseDTO<List<ListaDetalhadaDespesaDTO>>> despesasDetalhadas(){

        ResponseBaseDTO <List<ListaDetalhadaDespesaDTO>> responseBaseDTO  = new ResponseBaseDTO<>();
        responseBaseDTO.setData(despesaService.obterDespesasDetalhadas());

        return ResponseEntity.ok(responseBaseDTO);

    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

}
