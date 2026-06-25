package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepo  extends JpaRepository<PedidoItem, Long> {
}
