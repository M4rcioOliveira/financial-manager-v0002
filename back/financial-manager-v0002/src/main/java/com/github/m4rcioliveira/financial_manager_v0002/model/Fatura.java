package com.github.m4rcioliveira.financial_manager_v0002.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal total;

    private Integer mes;

    private Integer ano;

    @OneToMany(mappedBy = "fatura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Despesa> despesas = new ArrayList<>();

    public void addDespesa(Despesa despesa) {
        despesas.add(despesa);
        despesa.setFatura(this);
    }

    public void removeDespesa(Despesa despesa) {
        despesas.remove(despesa);
        despesa.setFatura(null);
    }

}
