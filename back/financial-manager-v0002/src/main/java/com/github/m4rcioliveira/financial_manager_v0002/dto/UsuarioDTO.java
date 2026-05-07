package com.github.m4rcioliveira.financial_manager_v0002.dto;

import com.github.m4rcioliveira.financial_manager_v0002.enums.RoleNameEnum;

import java.util.List;

public record UsuarioDTO(

        String email,
        String password,
        List<RoleNameEnum> role

) {
}
