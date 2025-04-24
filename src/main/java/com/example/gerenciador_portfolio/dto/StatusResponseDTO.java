package com.example.gerenciador_portfolio.dto;

import com.example.gerenciador_portfolio.entity.Status;

public record StatusResponseDTO(Long idStatus, String status, int ordem) {
    public StatusResponseDTO(Status status) {
        this(status.getIdStatus(), status.getStatus(), status.getOrdem());
    }
}
