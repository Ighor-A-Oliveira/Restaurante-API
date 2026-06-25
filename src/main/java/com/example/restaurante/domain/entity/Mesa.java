package com.example.restaurante.domain.entity;

import com.example.restaurante.domain.enums.StatusMesa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mesas")
@Getter
@Setter
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;
    private String descricao;
    private Integer capacidade;

    @Enumerated(EnumType.STRING)
    private StatusMesa status = StatusMesa.LIVRE;

}

