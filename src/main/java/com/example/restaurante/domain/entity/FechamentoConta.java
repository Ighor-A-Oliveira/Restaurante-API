package com.example.restaurante.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fechamentos_conta")
@Setter
@Getter
public class FechamentoConta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal subtotal;

    @Column(name = "taxa_servico")
    private BigDecimal taxaServico;


    private BigDecimal desconto;
    private BigDecimal total;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;


    @PrePersist
    private void prePersist(){
        dataFechamento = LocalDateTime.now();
    }

}
