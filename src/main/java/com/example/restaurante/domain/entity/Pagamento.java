package com.example.restaurante.domain.entity;

import com.example.restaurante.domain.enums.FormaPagamento;
import com.example.restaurante.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Setter
@Getter
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column(name = "codigo_transacao_externa")
    private String codigoTransacaoExterna;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;


    @PrePersist
    private void prePersist(){
        criadoEm = LocalDateTime.now();
    }

}


