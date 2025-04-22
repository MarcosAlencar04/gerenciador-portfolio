package com.example.gerenciador_portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciador_portfolio.entity.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

}
