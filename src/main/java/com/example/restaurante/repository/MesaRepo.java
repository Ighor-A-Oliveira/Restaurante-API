package com.example.restaurante.repository;

import com.example.restaurante.domain.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepo extends JpaRepository<Mesa, Long> {
}
