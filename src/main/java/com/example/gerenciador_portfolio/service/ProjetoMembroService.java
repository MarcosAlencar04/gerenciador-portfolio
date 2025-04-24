package com.example.gerenciador_portfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciador_portfolio.entity.Membro;
import com.example.gerenciador_portfolio.entity.Projeto;
import com.example.gerenciador_portfolio.entity.ProjetoMembro;

@Service
public class ProjetoMembroService {

    @Autowired
    private MembroService membroService;

    public List<ProjetoMembro> criaMembros(List<String> nomeMembros, Projeto projeto) {
        List<ProjetoMembro> membrosProjeto = new ArrayList<>();

        for(String nomeMembro : nomeMembros) {
            Membro membro = membroService.validarMembro(nomeMembro);
            ProjetoMembro projetoMembro = new ProjetoMembro();
            projetoMembro.setProjeto(projeto);
            projetoMembro.setMembro(membro);
            membrosProjeto.add(projetoMembro);
        }
        
        return membrosProjeto;
    }

    public void verificaQuantidadeProjetos(List<ProjetoMembro> membrosProjeto){
        for(ProjetoMembro pm : membrosProjeto){
            if(membroService.contarProjetosAtivos(pm.getMembro()) >=3 ) {
                throw new RuntimeException("Membro " + pm.getMembro().getNome() + " está em 3 projetos");
            }
        }
    }


}
