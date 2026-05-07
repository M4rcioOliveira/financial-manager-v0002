package com.github.m4rcioliveira.financial_manager_v0002.dto;

import com.github.m4rcioliveira.financial_manager_v0002.enums.RoleNameEnum;

public record NovoUsuarioDTO(

        String email,
        String password,
        RoleNameEnum role

) {
}
