package com.example.gerenciador_portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.entity.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNome(String nome);

    int countByMembros_MembroAndStatus_StatusNotIn(Membro membro, List<String> status);

}
