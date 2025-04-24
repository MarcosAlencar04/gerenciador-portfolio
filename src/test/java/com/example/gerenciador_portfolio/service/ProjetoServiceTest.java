package com.example.gerenciador_portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.gerenciador_portfolio.entity.Projeto;
import com.example.gerenciador_portfolio.entity.Risco;
import com.example.gerenciador_portfolio.entity.Status;
import com.example.gerenciador_portfolio.repository.ProjetoMembroRepository;
import com.example.gerenciador_portfolio.repository.ProjetoRepository;
import com.example.gerenciador_portfolio.repository.RiscoRepository;
import com.example.gerenciador_portfolio.repository.StatusRepository;

@ExtendWith(MockitoExtension.class)
public class ProjetoServiceTest {

    @InjectMocks
    private ProjetoService projetoService;

    @Mock
    private ProjetoMembroRepository projetoMembroRepository;

    @Mock
    private RiscoRepository riscoRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private MembroService membroService;

    @Mock
    private ProjetoMembroService projetoMembroService;

   @Test
    void calcularRiscoBaixo() {
        BigDecimal orcamento = BigDecimal.valueOf(50000);
        
        // Usando LocalDate para a data de início e término
        LocalDate dataInicio = LocalDate.of(2024, 3, 10);
        LocalDate previsaoTermino = LocalDate.of(2024, 5, 11);

        // Simulando o retorno do risco "BAIXO" do repositório
        Risco riscoMock = new Risco(1L, "BAIXO");
        when(riscoRepository.findByRisco("BAIXO")).thenReturn(riscoMock);
        
        // Calculando o risco
        Risco riscoCalculado = projetoService.calcularRisco(orcamento, previsaoTermino, dataInicio);
        
        // Verificando os resultados
        assertNotNull(riscoCalculado);
        assertEquals("BAIXO", riscoCalculado.getRisco());
    }

    @Test
    void calcularRiscoAlto() {
        BigDecimal orcamento = BigDecimal.valueOf(600000);
        
        // Usando LocalDate para a data de início e término
        LocalDate dataInicio = LocalDate.of(2024, 3, 10);
        LocalDate previsaoTermino = LocalDate.of(2024, 10, 7);

        // Simulando o retorno do risco "ALTO" do repositório
        Risco riscoMock = new Risco(1L, "ALTO");
        when(riscoRepository.findByRisco("ALTO")).thenReturn(riscoMock);
        
        // Calculando o risco
        Risco riscoCalculado = projetoService.calcularRisco(orcamento, previsaoTermino, dataInicio);
        
        // Verificando os resultados
        assertNotNull(riscoCalculado);
        assertEquals("ALTO", riscoCalculado.getRisco());
    }

    @Test
    void calcularRiscoMedio() {
        BigDecimal orcamento = BigDecimal.valueOf(300000);
        
        // Usando LocalDate para a data de início e término
        LocalDate dataInicio = LocalDate.of(2024, 3, 10);
        LocalDate previsaoTermino = LocalDate.of(2024, 8, 4);
        
        // Simulando o retorno do risco "MEDIO" do repositório
        Risco riscoMock = new Risco(1L, "MEDIO");
        when(riscoRepository.findByRisco(Projeto.risco.MEDIO.name())).thenReturn(riscoMock);
        
        // Calculando o risco
        Risco riscoCalculado = projetoService.calcularRisco(orcamento, previsaoTermino, dataInicio);
        
        // Verificando os resultados
        assertNotNull(riscoCalculado);
        assertEquals("MEDIO", riscoCalculado.getRisco());
    }

    @Test
    void alterarStatusTest() {
        String nomeProjeto = "ProjetoX";
        String novoStatus = "análise realizada";

        Projeto projeto = new Projeto();
        projeto.setNome(nomeProjeto);
        
        Status statusAtual = new Status();
        statusAtual.setStatus("em análise");
        statusAtual.setOrdem(1);

        Status statusNovo = new Status();
        statusNovo.setStatus(novoStatus);
        statusNovo.setOrdem(2);

        when(projetoRepository.findByNome(nomeProjeto)).thenReturn(Optional.of(projeto));
        when(statusRepository.findByStatus(novoStatus)).thenReturn(Optional.of(statusNovo));

        projeto.setStatus(statusAtual);
        
        Projeto projetoAlterado = projetoService.alterarStatus(nomeProjeto, novoStatus);
        
        assertNotNull(projetoAlterado);
        assertEquals(novoStatus, projetoAlterado.getStatus().getStatus());
    }

    @Test
    void excluirProjetoTest() {
        String nomeProjeto = "ProjetoX";

        Projeto projeto = new Projeto();
        projeto.setNome(nomeProjeto);
        
        Status status = new Status();
        status.setStatus("em andamento");

        projeto.setStatus(status);

        when(projetoRepository.findByNome(nomeProjeto)).thenReturn(Optional.of(projeto));

        // Teste para exceção, pois o status do projeto é "em andamento"
        assertThrows(RuntimeException.class, () -> projetoService.excluirProjeto(nomeProjeto));
    }

}
