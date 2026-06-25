package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepo  extends JpaRepository<Pagamento, Long> {
}
