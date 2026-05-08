package com.github.m4rcioliveira.financial_manager_v0002.repository;

import com.github.m4rcioliveira.financial_manager_v0002.enums.PagamentoStatusEnum;
import com.github.m4rcioliveira.financial_manager_v0002.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, UUID> {

    Optional<Despesa> findByIdAndUserIdAndStatusPagamentoIn(
            UUID id,
            UUID userId,
            List<PagamentoStatusEnum> status
    );

    List<Despesa> findAllByUserIdAndDataVencimentoGreaterThanEqualAndDataVencimentoLessThan(
            UUID userId,
            LocalDate inicio,
            LocalDate fim
    );

    List<Despesa> findAllByUserId(UUID id);

}
