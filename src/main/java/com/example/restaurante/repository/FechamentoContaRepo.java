package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.FechamentoConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FechamentoContaRepo  extends JpaRepository<FechamentoConta, Long> {

    boolean existsByPedidoId(Long pedidoId);

    Optional<FechamentoConta> findByPedidoId(Long pedidoId);
}
