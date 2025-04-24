package com.example.gerenciador_portfolio.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;
import com.example.gerenciador_portfolio.dto.MembroResponseDTO;
import com.example.gerenciador_portfolio.service.MembroService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/membros")
public class MembroController {

    @Autowired
    private MembroService membroService;

    @PostMapping
    public ResponseEntity<?> cadastrarMembro(@RequestBody MembroRequestDTO dto) {
        try {
            membroService.cadastrarMembro(dto);
            return ResponseEntity.ok().body(Map.of("message", "Membro cadastrado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Erro ao cadastrar Membro", "error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<MembroResponseDTO>> buscarTodos() {
        try {
            List<MembroResponseDTO> membros = membroService.listarTodos();
            return ResponseEntity.ok(membros);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroResponseDTO> buscarPorId(@PathVariable Long id) {
        try{
            MembroResponseDTO dto = membroService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<MembroResponseDTO> buscarPorNome(@RequestParam(required = false) String nome, @RequestParam(required = false) String cargo) {
        try{
            if (nome != null && cargo != null) {
                MembroResponseDTO dto = membroService.buscarPorNomeECargo(nome, cargo);
                return ResponseEntity.ok(dto);
            } else if (nome != null) {
                MembroResponseDTO dto = membroService.buscarPorNome(nome);
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.status(400).body(null);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
