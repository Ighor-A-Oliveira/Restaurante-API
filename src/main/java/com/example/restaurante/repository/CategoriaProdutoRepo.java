package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProdutoRepo  extends JpaRepository<CategoriaProduto, Long> {
}
