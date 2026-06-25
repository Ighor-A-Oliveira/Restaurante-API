package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.FechamentoConta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FechamentoContaRepo  extends JpaRepository<FechamentoConta, Long> {
}
