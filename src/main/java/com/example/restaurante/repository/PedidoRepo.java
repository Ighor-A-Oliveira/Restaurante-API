package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepo  extends JpaRepository<Pedido, Long> {
}
