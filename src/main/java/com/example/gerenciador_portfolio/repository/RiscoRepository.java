package com.example.gerenciador_portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciador_portfolio.entity.Risco;

public interface RiscoRepository extends JpaRepository<Risco, Long> {
    Risco findByRisco(String risco);
}
