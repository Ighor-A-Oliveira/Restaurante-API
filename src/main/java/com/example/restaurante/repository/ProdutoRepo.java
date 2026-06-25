package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepo  extends JpaRepository<Produto, Long> {
}
