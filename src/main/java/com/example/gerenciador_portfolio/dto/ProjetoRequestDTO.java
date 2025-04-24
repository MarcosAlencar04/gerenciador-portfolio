package com.example.gerenciador_portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProjetoRequestDTO(String nome, 
                                LocalDate dataInicio, 
                                LocalDate dataTermino, 
                                LocalDate previsaoTermino,
                                BigDecimal orcamentoTotal,
                                String descricao,
                                String gerente,
                                String status,
                                List<String> membros) {

}
