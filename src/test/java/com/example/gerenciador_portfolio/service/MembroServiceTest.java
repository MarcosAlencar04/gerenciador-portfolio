package com.example.gerenciador_portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;
import com.example.gerenciador_portfolio.dto.MembroResponseDTO;
import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.external.MembroClient;
import com.example.gerenciador_portfolio.repository.MembroRepository;

@ExtendWith(MockitoExtension.class)
public class MembroServiceTest {

    @InjectMocks
    private MembroService membroService;
    
    @Mock
    private MembroRepository membroRepository;

    @Mock
    private MembroClient membroClient;

    @Test
    void cadastrarMembro() {
        MembroRequestDTO dto = new MembroRequestDTO("Carlos", "funcionário");
        Membro membro = new Membro(dto);
        membro.setIdMembro(1L);

        when(membroClient.cadastrar(any(MembroRequestDTO.class))).thenReturn(new MembroResponseDTO(membro));
        when(membroRepository.save(any(Membro.class))).thenReturn(membro);

        MembroResponseDTO response = membroService.cadastrarMembro(dto);

        assertEquals("Carlos", response.nome());
        assertEquals("funcionário", response.cargo());
        assertNotNull(response.idMembro());
    }

    @Test
    void buscarPorNomeECargo() {
        Membro membro = new Membro(1L, "Carlos", "funcionário");
        
        when(membroClient.buscarPorNomeECargo("Carlos", "funcionário")).thenReturn(new MembroResponseDTO(membro));
    
        MembroResponseDTO response = membroService.buscarPorNomeECargo("Carlos", "funcionário");
    
        assertEquals("Carlos", response.nome());
        assertEquals("funcionário", response.cargo());
    }

    @Test
    void buscarPorNome() {
        Membro membro = new Membro(1L, "Carlos", "funcionário");
        
        when(membroClient.buscarPorNome("Carlos")).thenReturn(new MembroResponseDTO(membro));
    
        MembroResponseDTO response = membroService.buscarPorNome("Carlos");
    
        assertEquals("Carlos", response.nome());
        assertEquals("funcionário", response.cargo());
    }
    

    @Test
    void buscaNaoEncontrada() {
        when(membroClient.buscarPorNomeECargo("Não Cadastrado", "cargo")).thenThrow(new RuntimeException("Membro não encontrado na API externa com nome e cargo informados"));
    
        assertThrows(RuntimeException.class, () -> {
            membroService.buscarPorNomeECargo("Não Cadastrado", "cargo");
        });
    }
    
}

