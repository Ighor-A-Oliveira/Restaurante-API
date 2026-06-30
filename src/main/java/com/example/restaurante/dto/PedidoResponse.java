package com.example.restaurante.dto;

import com.example.restaurante.domain.entity.Pedido;
import com.example.restaurante.domain.entity.Produto;
import com.example.restaurante.domain.enums.StatusPedido;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record PedidoResponse(
        Long id,
        Long mesaId,
        Integer numeroMesa,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        StatusPedido status,
        String observcacao
) {
    public static PedidoResponse fromEntity(Pedido ped){
        return new PedidoResponse(
                ped.getId(),
                ped.getMesa().getId(),
                ped.getMesa().getNumero(),
                ped.getDataAbertura(),
                ped.getDataFechamento(),
                ped.getStatus(),
                ped.getObservacao()

        );
    }
}
