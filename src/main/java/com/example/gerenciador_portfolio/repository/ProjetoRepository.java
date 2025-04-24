package com.example.gerenciador_portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.entity.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNome(String nome);
    int countByMembros_MembroAndStatus_StatusNotIn(Membro membro, List<String> status);
    
    Page<Projeto> findByStatus_Status(String status, Pageable pageable);
    Page<Projeto> findByNomeContaining(String nome, Pageable pageable);
    Page<Projeto> findByStatus_StatusAndNomeContaining(String status, String nome, Pageable pageable);


}
