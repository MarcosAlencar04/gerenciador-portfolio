package com.example.gerenciador_portfolio.external;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;
import com.example.gerenciador_portfolio.dto.MembroResponseDTO;

@Component
public class MembroClient {
    private static final String URL_BASE = "http://localhost:3000/membros";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public MembroResponseDTO buscarPorId(Long id) {
        try {
            return webClientBuilder.baseUrl(URL_BASE)
                    .build()
                    .get()
                    .uri("/{id}", id)
                    .retrieve()
                    .bodyToMono(MembroResponseDTO.class)
                    .block();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado com o ID: " + id, e);
        }
    }

    public MembroResponseDTO buscarPorNome(String nome) {
        try {
            List<MembroResponseDTO> list = buscarTodos();
            MembroResponseDTO newMembro = list.stream().filter(membro -> membro.nome().equals(nome)).collect(Collectors.toList()).get(0);
            return newMembro;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado com o Nome: " + nome, e);
        }
    }

    public List<MembroResponseDTO> buscarTodos() {
        try {
            MembroResponseDTO[] membros = webClientBuilder.baseUrl(URL_BASE)
                    .build()
                    .get()
                    .retrieve()
                    .bodyToMono(MembroResponseDTO[].class)
                    .block();
            return Arrays.asList(membros);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar todos membros", e);
        }
    }

    public MembroResponseDTO cadastrar(MembroRequestDTO dto) {
        try {
            return webClientBuilder.baseUrl(URL_BASE)
                    .build()
                    .post()
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(MembroResponseDTO.class)
                    .block();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao cadastrar membro", e);
        }
    }

    public MembroResponseDTO buscarPorNomeECargo(String nome, String cargo) {
        try {
            return webClientBuilder.baseUrl(URL_BASE)
                    .build()
                    .get()
                    .uri("/buscar?nome={nome}&cargo={cargo}", nome, cargo)
                    .retrieve()
                    .bodyToMono(MembroResponseDTO.class)
                    .block();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado com o nome e cargo: " + nome + ", " + cargo, e);
        }
    }
}
