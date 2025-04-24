package com.example.gerenciador_portfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciador_portfolio.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long>{

   Optional<Status> findByStatus(String status);
}
