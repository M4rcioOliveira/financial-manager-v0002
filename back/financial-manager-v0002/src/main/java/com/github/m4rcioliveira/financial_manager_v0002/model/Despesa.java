package com.github.m4rcioliveira.financial_manager_v0002.model;

import com.github.m4rcioliveira.financial_manager_v0002.enums.CategoriaEnum;
import com.github.m4rcioliveira.financial_manager_v0002.enums.PagamentoStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_despesa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID idUnico;

    private String nome;

    private String descricao;

    private BigDecimal valorTotal;

    private BigDecimal valorParcela;

    private Boolean fixa;

    private Integer qtdParcelas;

    private LocalDate dataVencimento;

    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria;

    @Enumerated(EnumType.STRING)
    private PagamentoStatusEnum statusPagamento;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
