package com.example.gerenciador_portfolio.entity;

import com.example.gerenciador_portfolio.dto.MembroRequestDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "membro")
@Entity(name = "membro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idMembro")
@ToString
public class Membro {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMembro;

    private String nome;
    
    private String cargo;

    public Membro(MembroRequestDTO dto){
        this.nome = dto.nome();
        this.cargo = dto.cargo();
    }

}
