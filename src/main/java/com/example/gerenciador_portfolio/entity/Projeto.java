package com.example.gerenciador_portfolio.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.gerenciador_portfolio.dto.ProjetoRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "projeto")
@Entity(name = "prjeto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idProjeto")
@ToString
public class Projeto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjeto;

    private String nome;
    
    private LocalDate dataInicio;
    
    private LocalDate dataTermino;

    private LocalDate previsaoTermino;

    private BigDecimal orcamentoTotal;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_membro")
    private Membro gerente;
    
    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "id_risco")
    private Risco risco;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProjetoMembro> membros = new ArrayList<>();

    public enum risco {
        BAIXO, MEDIO, ALTO
    }
    
    public Projeto(ProjetoRequestDTO dto) {
        this.nome = dto.nome();
        this.dataInicio = dto.dataInicio();
        this.dataTermino = dto.dataTermino();
        this.previsaoTermino = dto.previsaoTermino();
        this.orcamentoTotal = dto.orcamentoTotal();
        this.descricao = dto.descricao();
    }

}
