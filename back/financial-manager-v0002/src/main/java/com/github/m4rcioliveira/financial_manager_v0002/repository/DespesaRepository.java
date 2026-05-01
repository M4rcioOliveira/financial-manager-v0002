package com.github.m4rcioliveira.financial_manager_v0002.repository;

import com.github.m4rcioliveira.financial_manager_v0002.enums.PagamentoStatusEnum;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, UUID> {

    //Pagamento Unico
    List<Despesa> findAllByIdAndStatusPagamentoIn(
            UUID id,
            List<PagamentoStatusEnum> status
    );


    List<Despesa> findByDataVencimentoGreaterThanEqualAndDataVencimentoLessThan(
            LocalDate inicio,
            LocalDate fim
    );

}
