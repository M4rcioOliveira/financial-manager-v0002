package com.github.m4rcioliveira.financial_manager_v0002.repository;

import com.github.m4rcioliveira.financial_manager_v0002.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
