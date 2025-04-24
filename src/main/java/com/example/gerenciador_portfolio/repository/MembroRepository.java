package com.example.gerenciador_portfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciador_portfolio.entity.Membro;

public interface MembroRepository extends JpaRepository<Membro, Long>{

    Optional<Membro> findByNomeAndCargo(String nome, String cargo);
    Optional<Membro> findByNome(String nome);
    
}
