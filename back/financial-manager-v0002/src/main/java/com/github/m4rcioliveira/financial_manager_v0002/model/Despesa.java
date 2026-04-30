package com.github.m4rcioliveira.financial_manager_v0002.model;

import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String descricao;

    private String valor;

    private String fixa;

    private String qtdParcelas;

    private String parcelada;

    private LocalDateTime dataVencimento;

    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria;

    private Boolean paga;

    @ManyToOne
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;


}
