package com.example.gerenciador_portfolio.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.gerenciador_portfolio.dto.ProjetoRequestDTO;
import com.example.gerenciador_portfolio.dto.ProjetoResponseDTO;
import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.entity.Projeto;
import com.example.gerenciador_portfolio.entity.ProjetoMembro;
import com.example.gerenciador_portfolio.entity.Risco;
import com.example.gerenciador_portfolio.entity.Status;
import com.example.gerenciador_portfolio.repository.ProjetoMembroRepository;
import com.example.gerenciador_portfolio.repository.ProjetoRepository;
import com.example.gerenciador_portfolio.repository.RiscoRepository;
import com.example.gerenciador_portfolio.repository.StatusRepository;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private MembroService membroService;
    
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ProjetoMembroService projetoMembroService;
    
    @Autowired
    private ProjetoMembroRepository projetoMembroRepository;

    @Autowired
    private RiscoRepository riscoRepository;

    private static final BigDecimal ORCAMENTO_BASE_BAIXO = new BigDecimal(100000);
    private static final BigDecimal ORCAMENTO_BASE_MEDIA_ALTA = new BigDecimal(500000);
    private static final String STATUS_INICIAL = "em análise";

    public ProjetoResponseDTO cadastrarProjeto(ProjetoRequestDTO dto) {
        try {
            
            Membro gerente = membroService.validarMembro(dto.gerente());
            Status status = statusRepository.findByStatus(STATUS_INICIAL).orElseThrow(() -> new RuntimeException("Erro ao buscar status inicial 'em análise'"));

            Projeto projeto = new Projeto();
            projeto.setNome(dto.nome());
            projeto.setDataInicio(dto.dataInicio());
            projeto.setDataTermino(dto.dataTermino());
            projeto.setPrevisaoTermino(dto.previsaoTermino());
            projeto.setDescricao(dto.descricao());
            projeto.setGerente(gerente);
            projeto.setStatus(status);
            projeto.setOrcamentoTotal(dto.orcamentoTotal());
            projeto.setRisco(calcularRisco(dto.orcamentoTotal(),dto.previsaoTermino(), dto.dataInicio()));

            if(dto.membros().size() < 1 || dto.membros().size() > 10 ) {
                throw new RuntimeException("O número de membros do projeto deve ser no mínimo 1 e no máximo 10");
            }  
            membroService.verificaCargo(dto.membros());
            List<ProjetoMembro> membrosProjeto = projetoMembroService.criaMembros(dto.membros(), projeto);
            projeto.setMembros(membrosProjeto);
            projetoMembroService.verificaQuantidadeProjetos(membrosProjeto);
            Projeto newProjeto = projetoRepository.save(projeto);
            projetoMembroRepository.saveAll(membrosProjeto);
            return new ProjetoResponseDTO(newProjeto);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar projeto: " + e.getMessage());
        }
    }

    public Risco calcularRisco(BigDecimal orcamentoTotal, LocalDate previsaoTermino, LocalDate dataInicio) {
        int meses = calcularMeses(dataInicio, previsaoTermino);
        Risco risco = new Risco();

        if (orcamentoTotal.compareTo(ORCAMENTO_BASE_MEDIA_ALTA) > 0 || meses > 6) {
            risco = riscoRepository.findByRisco(Projeto.risco.ALTO.name());
        } else if ((orcamentoTotal.compareTo(ORCAMENTO_BASE_BAIXO) > 0 && orcamentoTotal.compareTo(ORCAMENTO_BASE_MEDIA_ALTA) < 0) || (meses > 3 && meses < 6)) {
            risco = riscoRepository.findByRisco(Projeto.risco.MEDIO.name());
        } else if (orcamentoTotal.compareTo(ORCAMENTO_BASE_BAIXO) <= 0 && meses < 3 ){
            risco = riscoRepository.findByRisco(Projeto.risco.BAIXO.name());
        }
    
        return risco;
    }

    public int calcularMeses(LocalDate dataInicio, LocalDate dataTermino) {
        long meses = ChronoUnit.MONTHS.between(dataInicio, dataTermino);
        return (int) meses;
    }

    public Projeto alterarStatus(String nomeProjeto, String novoStatus) {
        Projeto projeto = projetoRepository.findByNome(nomeProjeto)
                .orElseThrow(() -> new RuntimeException("Projeto não cadastrado"));

        Status status = statusRepository.findByStatus(novoStatus).orElseThrow(() -> new RuntimeException("Status inexistente"));

        if (status.getStatus().equals("cancelado")) {
            projeto.setStatus(status);
            projetoRepository.save(projeto);
            return projeto;
        } else {
            Status statusAtual = projeto.getStatus();

            if (status.getOrdem() == statusAtual.getOrdem() + 1) {
                projeto.setStatus(status);
                projetoRepository.save(projeto);
                return projeto;
            } else {
                throw new RuntimeException("Ordem de transição de status incoerente.");
            }
        }
    }

    public void excluirProjeto(String nomeProjeto) {
        Projeto projeto = projetoRepository.findByNome(nomeProjeto)
                .orElseThrow(() -> new RuntimeException("Projeto não cadastrado"));

        if (projeto.getStatus().getStatus().equals("iniciado") || 
            projeto.getStatus().getStatus().equals("em andamento") || 
            projeto.getStatus().getStatus().equals("encerrado")) {
            throw new RuntimeException("Não é possível excluir projeto com status: " + projeto.getStatus().getStatus());
        } else { 
            projetoRepository.delete(projeto);    
        }
    }

    public Projeto adicionarMembros(String nomeProjeto, List<String> nomeMembros) {
        Projeto projeto = projetoRepository.findByNome(nomeProjeto)
                .orElseThrow(() -> new RuntimeException("Projeto não cadastrado"));
    
        if (projeto.getMembros().size() + nomeMembros.size() > 10) {
            throw new RuntimeException("O projeto com número máximo de membros permitido (10).");
        }

        membroService.verificaCargo(nomeMembros);
        List<ProjetoMembro> membrosProjeto = projetoMembroService.criaMembros(nomeMembros, projeto);
        projetoMembroService.verificaQuantidadeProjetos(membrosProjeto);
        projeto.getMembros().addAll(membrosProjeto);
        projetoMembroRepository.saveAll(membrosProjeto);
        return projetoRepository.save(projeto);
    }

    public Projeto adicionarDataTermino(String nomeProjeto, LocalDate dataTermino) {
        Projeto projeto = projetoRepository.findByNome(nomeProjeto)
                .orElseThrow(() -> new RuntimeException("Projeto não cadastrado"));
        projeto.setDataTermino(dataTermino);
        return projetoRepository.save(projeto);
    }
    
    public Map<String, Object> gerarRelatorioPortfolio() {
        List<Projeto> projetos = projetoRepository.findAll();

        Map<String, Long> quantidadePorStatus = projetos.stream()
                .collect(Collectors.groupingBy(projeto -> projeto.getStatus().getStatus(), Collectors.counting()));

        Map<String, BigDecimal> totalOrcadoPorStatus = projetos.stream()
                .collect(Collectors.groupingBy(projeto -> projeto.getStatus().getStatus(),
                        Collectors.mapping(Projeto::getOrcamentoTotal, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        double mediaDuracaoEncerrados = projetos.stream()
                .filter(projeto -> "encerrado".equals(projeto.getStatus().getStatus()))
                .mapToLong(projeto -> calcularMeses(projeto.getDataInicio(), projeto.getDataTermino()))
                .average()
                .orElse(0);

        Set<Long> membrosUnicos = projetos.stream()
                .flatMap(projeto -> projeto.getMembros().stream())
                .map(projetoMembro -> projetoMembro.getMembro().getIdMembro())
                .collect(Collectors.toSet());

        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("quantidadePorStatus", quantidadePorStatus);
        relatorio.put("totalOrcadoPorStatus", totalOrcadoPorStatus);
        relatorio.put("mediaDuracaoEncerrados", mediaDuracaoEncerrados);
        relatorio.put("totalMembrosUnicos", membrosUnicos.size());

        return relatorio;
    }

    public Page<Projeto> listarProjetos(int pagina, int tamanho, String status, String nome) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
    
        if (status != null && nome != null) {
            return projetoRepository.findByStatus_StatusAndNomeContaining(status, nome, pageRequest);
        } else if (status != null) {
            return projetoRepository.findByStatus_Status(status, pageRequest);
        } else if (nome != null) {
            return projetoRepository.findByNomeContaining(nome, pageRequest);
        }
    
        return projetoRepository.findAll(pageRequest);
    }
    
}
