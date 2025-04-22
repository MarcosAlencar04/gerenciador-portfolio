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

    @PostMapping()
    public ResponseEntity<?> cadastrarMembro(@RequestBody MembroRequestDTO dto) {
        try {
            membroService.cadastrarMembro(dto);
            return ResponseEntity.ok().body(Map.of("message", "Membro cadastrado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Erro ao cadastrar Membro", "error", e.getMessage()));
        }
    }
    

    @GetMapping()
    public List<MembroResponseDTO> buscarTodos() {
        return membroService.listarTodos();
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
    public ResponseEntity<MembroResponseDTO> buscarPorNomeECargo(@RequestParam String nome, @RequestParam String cargo) {
        try{
            MembroResponseDTO dto = membroService.buscarPorNomeECargo(nome, cargo);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
