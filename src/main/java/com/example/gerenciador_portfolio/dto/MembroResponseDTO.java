package com.example.gerenciador_portfolio.dto;

import com.example.gerenciador_portfolio.entity.Membro;

public record MembroResponseDTO(Long idMembro, String nome, String cargo) {

    public MembroResponseDTO(Membro membro){
        this(membro.getIdMembro(), membro.getNome(), membro.getCargo());
    }

}
