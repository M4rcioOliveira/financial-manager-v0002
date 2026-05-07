package com.github.m4rcioliveira.financial_manager_v0002.model;

import com.github.m4rcioliveira.financial_manager_v0002.enums.RoleNameEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RoleNameEnum name;

}
