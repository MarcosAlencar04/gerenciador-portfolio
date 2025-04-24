package com.example.gerenciador_portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.entity.Projeto;
import com.example.gerenciador_portfolio.entity.ProjetoMembro;
import com.example.gerenciador_portfolio.repository.ProjetoMembroRepository;
import com.example.gerenciador_portfolio.repository.ProjetoRepository;

@ExtendWith(MockitoExtension.class)
public class ProjetoMembroServiceTest {

    @InjectMocks
    private ProjetoMembroService projetoMembroService;

    @Mock
    private MembroService membroService;

    @Mock
    private ProjetoMembroRepository projetoMembroRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @Test
    void verificaQuantidadeProjetosMembroLimite() {
        Projeto projeto = new Projeto();
        projeto.setIdProjeto(1L);

        Membro membro1 = new Membro(1L, "Carlos", "funcionário");
        Membro membro2 = new Membro(2L, "Ana", "funcionário");

        // Simula que o membro1 já está alocado em 3 projetos
        lenient().when(membroService.contarProjetosAtivos(membro1)).thenReturn(3);
        // Simula que o membro2 está alocado em 2 projetos
        lenient().when(membroService.contarProjetosAtivos(membro2)).thenReturn(2);

        ProjetoMembro pm1 = new ProjetoMembro(1L, projeto, membro1);
        ProjetoMembro pm2 = new ProjetoMembro(2L, projeto, membro2);

        List<ProjetoMembro> membrosProjeto = Arrays.asList(pm1, pm2);

        // Verifica se a exceção é lançada para o membro1, que já está em 3 projetos
        assertThrows(RuntimeException.class, () -> projetoMembroService.verificaQuantidadeProjetos(membrosProjeto));
    }

    @Test
    void verificaQuantidadeProjetosMembroDentroLimite() {
        Projeto projeto = new Projeto();
        projeto.setIdProjeto(1L);

        Membro membro1 = new Membro(1L, "Carlos", "funcionário");
        Membro membro2 = new Membro(2L, "Ana", "funcionário");

        // Simula que o membro1 está alocado em 2 projetos
        lenient().when(membroService.contarProjetosAtivos(membro1)).thenReturn(2);
        // Simula que o membro2 está alocado em 1 projeto
        lenient().when(membroService.contarProjetosAtivos(membro2)).thenReturn(1);

        ProjetoMembro pm1 = new ProjetoMembro(1L, projeto, membro1);
        ProjetoMembro pm2 = new ProjetoMembro(2L, projeto, membro2);

        List<ProjetoMembro> membrosProjeto = Arrays.asList(pm1, pm2);

        // Verifica que a função não lança exceção, pois ambos os membros estão dentro do limite de projetos
        projetoMembroService.verificaQuantidadeProjetos(membrosProjeto);
    }

    @Test
    void criaMembrosTest() {
        Projeto projeto = new Projeto();
        projeto.setIdProjeto(1L);
    
        Membro membro1 = new Membro(1L, "Carlos", "funcionário");
        Membro membro2 = new Membro(2L, "Ana", "funcionário");
    
        List<String> nomeMembros = Arrays.asList("Carlos", "Ana");
    
        lenient().when(membroService.validarMembro("Carlos")).thenReturn(membro1);
        lenient().when(membroService.validarMembro("Ana")).thenReturn(membro2);

        lenient().when(membroService.contarProjetosAtivos(membro1)).thenReturn(3);
        lenient().when(membroService.contarProjetosAtivos(membro2)).thenReturn(2);
    
        List<ProjetoMembro> membrosProjeto = projetoMembroService.criaMembros(nomeMembros, projeto);
    
        assertThrows(RuntimeException.class, () -> projetoMembroService.verificaQuantidadeProjetos(membrosProjeto));
        assertEquals(2, membrosProjeto.size());
    }
    
}
