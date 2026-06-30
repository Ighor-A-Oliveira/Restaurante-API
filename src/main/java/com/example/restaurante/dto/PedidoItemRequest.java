package com.example.restaurante.dto;

public record PedidoItemRequest(
        Long productId,
        Integer quantidade,
        String observacao
) {
}
