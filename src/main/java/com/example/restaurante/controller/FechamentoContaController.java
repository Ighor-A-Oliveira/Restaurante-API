package com.example.restaurante.controller;

import com.example.restaurante.dto.FechamentoContaResponse;
import com.example.restaurante.dto.FechamentoContaRequest;
import com.example.restaurante.service.FechamentoContaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/{pedidoId}/fechamento")
public class FechamentoContaController {

    private final FechamentoContaService fechamentoContaService;

    public FechamentoContaController(FechamentoContaService fechamentoContaService) {
        this.fechamentoContaService = fechamentoContaService;
    }

    @PostMapping
    public FechamentoContaResponse fecharConta(@PathVariable Long pedidoId, @RequestBody FechamentoContaRequest request){
        return fechamentoContaService.fecharConta(pedidoId, request);
    }

    @GetMapping
    public FechamentoContaResponse listar(@PathVariable Long pedidoId){
        return fechamentoContaService.buscarPorFechamento(pedidoId);
    }

}
