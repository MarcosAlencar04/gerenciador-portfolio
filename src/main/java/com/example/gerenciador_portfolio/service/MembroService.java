package com.example.gerenciador_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;
import com.example.gerenciador_portfolio.dto.MembroResponseDTO;
import com.example.gerenciador_portfolio.external.MembroClient;

@Service
public class MembroService {
    
    @Autowired
    private MembroClient membroClient;

    public MembroResponseDTO cadastrarMembro(MembroRequestDTO dto) {
        return membroClient.cadastrar(dto);
    }

    public List<MembroResponseDTO> listarTodos() {
        return membroClient.buscarTodos();
    }
    
    public MembroResponseDTO buscarPorId(Long id) {
        try {
            return membroClient.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Membro não encontrado");
        }
    }

    public MembroResponseDTO buscarPorNomeECargo(String nome, String cargo) {
        try {
            return membroClient.buscarPorNomeECargo(nome, cargo);
        } catch (Exception e) {
            throw new RuntimeException("Membro não encontrado");
        }
    }

}
