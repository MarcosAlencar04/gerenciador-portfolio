package com.example.gerenciador_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;
import com.example.gerenciador_portfolio.dto.MembroResponseDTO;
import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.external.MembroClient;
import com.example.gerenciador_portfolio.repository.MembroRepository;

@Service
public class MembroService {
    
    @Autowired
    private MembroClient membroClient;

    @Autowired
    private MembroRepository membroRepository;

    public MembroResponseDTO cadastrarMembro(MembroRequestDTO dto) {

        MembroResponseDTO response = membroClient.cadastrar(dto);

        Membro membro = new Membro();
        membro.setNome(response.nome());
        membro.setCargo(response.cargo());
        membroRepository.save(membro);
        
        return response;
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
