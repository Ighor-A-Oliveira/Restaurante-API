package com.example.restaurante.dto;

import com.example.restaurante.domain.entity.CategoriaProduto;
import com.example.restaurante.domain.entity.Produto;

import java.math.BigDecimal;

public record ProdutoRequest(
        Long categoriaId,
        String nome,
        String descricao,
        BigDecimal preco,
        Boolean disponivel,
        Integer tempoPreparoMinutos
) {
    public Produto toEntity(CategoriaProduto categoria){
        Produto prod = new Produto();
        preencher(prod, categoria);
        return prod;
    }

    public void preencher(Produto prod, CategoriaProduto categoria){
        prod.setCategoria(categoria);
        prod.setNome(nome);
        prod.setDescricao(descricao);
        prod.setPreco(preco);
        prod.setDisponivel(disponivel != null ? disponivel : true);
        prod.setTempoPreparoMinutos(tempoPreparoMinutos);
    }
}
