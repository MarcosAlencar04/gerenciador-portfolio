package com.example.gerenciador_portfolio.external;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;
import com.example.gerenciador_portfolio.dto.MembroResponseDTO;

@Component
public class MembroClient {
    private static final String URL_BASE = "http://localhost:8080/membros";

    @Autowired
    private RestTemplate restTemplate;

    public MembroResponseDTO buscarPorId(Long id) {
        try{
            return restTemplate.getForObject(URL_BASE + "/" + id, MembroResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e){
            throw e;
        }
    }

    public List<MembroResponseDTO> buscarTodos() {
        MembroResponseDTO[] membros = restTemplate.getForObject(URL_BASE, MembroResponseDTO[].class);
        return Arrays.asList(membros);
    }

    public MembroResponseDTO cadastrar(MembroRequestDTO dto) {
        return restTemplate.postForObject(URL_BASE, dto, MembroResponseDTO.class);
    }

    public MembroResponseDTO buscarPorNomeECargo(String nome, String cargo){
        try{
            return restTemplate.getForObject(URL_BASE + "/buscar?nome=" + nome + "&cargo=" + cargo, MembroResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e){
            throw e;
        }
    }
}
