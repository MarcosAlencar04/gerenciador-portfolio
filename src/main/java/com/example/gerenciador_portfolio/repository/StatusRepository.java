package com.example.gerenciador_portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciador_portfolio.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long>{

}
