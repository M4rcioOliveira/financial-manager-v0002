package com.github.m4rcioliveira.financial_manager_v0002.model;

import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private BigDecimal valorTotal;

    private BigDecimal valorParcela;

    private Boolean fixa;

    private Integer qtdParcelas;

    private Boolean parcelada;

    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria;

    private Boolean paga;

    @ManyToOne
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;


}
