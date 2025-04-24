package com.example.gerenciador_portfolio.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciador_portfolio.dto.ProjetoRequestDTO;
import com.example.gerenciador_portfolio.dto.ProjetoResponseDTO;
import com.example.gerenciador_portfolio.entity.Projeto;
import com.example.gerenciador_portfolio.service.ProjetoService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping()
    public ResponseEntity<ProjetoResponseDTO> cadastrarProjeto(@RequestBody ProjetoRequestDTO dto) {
        try {
            ProjetoResponseDTO projetoResponseDTO = projetoService.cadastrarProjeto(dto);
            return ResponseEntity.ok(projetoResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Se houver erro, retorna erro 500
        }
    }

    @PutMapping("/status")
    public ResponseEntity<?> alterarStatus(@RequestBody ProjetoRequestDTO dto) {
        try {
            projetoService.alterarStatus(dto.nome(), dto.status());
            return ResponseEntity.ok().body("Status alterado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Erro ao alterar status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{nomeProjeto}")
    public ResponseEntity<?> excluirProjeto(@PathVariable String nomeProjeto) {
        try {
            projetoService.excluirProjeto(nomeProjeto);
            return ResponseEntity.ok().body("Projeto excluído com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Erro ao excluir projeto: " + e.getMessage());
        }
    }
    
    @PutMapping("/membros")
    public ResponseEntity<?> adicionarMembros(@RequestBody ProjetoRequestDTO dto) {
        try {
            Projeto projeto = projetoService.adicionarMembros(dto.nome(), dto.membros());
            return ResponseEntity.ok().body("Membros adicionados no projeto: " + projeto.getNome());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Erro ao adicionar membro: " + e.getMessage());
        }
    }

    @PutMapping("/dataTermino")
    public ResponseEntity<?> adicionarDataTermino(@RequestBody ProjetoRequestDTO dto) {
        try {
            System.out.println(dto.dataTermino());
            projetoService.adicionarDataTermino(dto.nome(), dto.dataTermino());
            return ResponseEntity.ok().body("Data de término adicionada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Erro ao adicionar membro: " + e.getMessage());
        }
    }

    @GetMapping("/relatorio")
    public ResponseEntity<Map<String, Object>> gerarRelatorio() {
        Map<String, Object> relatorio = projetoService.gerarRelatorioPortfolio();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping
    public ResponseEntity<Page<Projeto>> listarProjetos(
        @RequestParam(value = "page", defaultValue = "0") int pagina,
        @RequestParam(value = "size", defaultValue = "10") int tamanho,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "nome", required = false) String nome) {

        Page<Projeto> projetos = projetoService.listarProjetos(pagina, tamanho, status, nome);
        return ResponseEntity.ok(projetos);
    }

}
