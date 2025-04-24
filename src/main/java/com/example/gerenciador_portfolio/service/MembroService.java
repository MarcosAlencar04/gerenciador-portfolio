package com.example.gerenciador_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;
import com.example.gerenciador_portfolio.dto.MembroResponseDTO;
import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.external.MembroClient;
import com.example.gerenciador_portfolio.repository.MembroRepository;
import com.example.gerenciador_portfolio.repository.ProjetoRepository;

@Service
public class MembroService {
    
    @Autowired
    private MembroClient membroClient;

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    public MembroResponseDTO cadastrarMembro(MembroRequestDTO dto) {

        try {
            MembroResponseDTO response = membroClient.cadastrar(dto);

            Membro membro = new Membro();
            membro.setNome(response.nome());
            membro.setCargo(response.cargo());
            membroRepository.save(membro);
            
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar membro: " + e.getMessage());
        }
    }

    public List<MembroResponseDTO> listarTodos() {
        try {
            return membroClient.buscarTodos();   
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos membros: " + e.getMessage());
        }
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

    public MembroResponseDTO buscarPorNome(String nome) {
        try {
            return membroClient.buscarPorNome(nome);
        } catch (Exception e) {
            throw new RuntimeException("Membro não encontrado");
        }
    }

    public Membro validarMembro(String nome){
        MembroResponseDTO membroDTO = membroClient.buscarPorNome(nome);
        Membro membro = membroRepository.findByNome(membroDTO.nome()).orElseGet(() -> {
            Membro newMembro = new Membro();
            newMembro.setNome(membroDTO.nome());
            newMembro.setCargo(membroDTO.cargo());
            return membroRepository.save(newMembro);
        });
        return membro;
    }

    public int contarProjetosAtivos(Membro membro) {
        return projetoRepository.countByMembros_MembroAndStatus_StatusNotIn(membro, List.of("encerrado", "cancelado"));
    }
    

    public void verificaCargo(List<String> nomeMembros) {
        
        for (String nomeMembro : nomeMembros) {
            Membro membro = validarMembro(nomeMembro);
            if (!"funcionário".equals(membro.getCargo())) {
                throw new RuntimeException("Membro " + nomeMembro + " não possui cargo de funcionário");
            }
        }
    }
}
