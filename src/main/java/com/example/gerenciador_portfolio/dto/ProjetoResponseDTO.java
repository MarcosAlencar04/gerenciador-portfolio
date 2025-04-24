package com.example.gerenciador_portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.gerenciador_portfolio.entity.Projeto;

public record ProjetoResponseDTO(Long idProjeto,
                                 String nome,
                                 LocalDate dataInicio,
                                 LocalDate dataTermino,
                                 LocalDate previsaoTermino,
                                 BigDecimal orcamentoTotal,
                                 String descricao,
                                 MembroResponseDTO gerente,
                                 StatusResponseDTO status,
                                 List<MembroResponseDTO> membros) {

    public ProjetoResponseDTO(Projeto projeto){
        this(projeto.getIdProjeto(),
             projeto.getNome(),
             projeto.getDataInicio(),
             projeto.getDataTermino(),
             projeto.getPrevisaoTermino(),
             projeto.getOrcamentoTotal(),
             projeto.getDescricao(),
             new MembroResponseDTO(projeto.getGerente()),
             new StatusResponseDTO(projeto.getStatus()),
             projeto.getMembros().stream().map(pm -> new MembroResponseDTO(pm.getMembro())).toList());
    }

}
