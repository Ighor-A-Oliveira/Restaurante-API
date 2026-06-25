package com.example.restaurante.dto;

import com.example.restaurante.domain.entity.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Boolean disponivel,
        Integer tempoPreparoMinutos,
        Long categoriaId,
        String categoriaNome,
        LocalDateTime criadoEm
) {

    public static ProdutoResponse fromEntity(Produto prod){
        return new ProdutoResponse(
                prod.getId(),
                prod.getNome(),
                prod.getDescricao(),
                prod.getPreco(),
                prod.getDisponivel(),
                prod.getTempoPreparoMinutos(),
                prod.getCategoria().getId(),
                prod.getCategoria().getNome(),
                prod.getCriadoEm()

        );
    }
}
