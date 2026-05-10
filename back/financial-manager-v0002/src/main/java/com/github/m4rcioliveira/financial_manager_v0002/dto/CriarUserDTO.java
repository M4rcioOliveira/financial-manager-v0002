package com.github.m4rcioliveira.financial_manager_v0002.dto;

import com.github.m4rcioliveira.financial_manager_v0002.enums.RoleNameEnum;

import java.util.List;

public record CriarUserDTO(

        String email,
        String password,
        String telegramId,
        List<RoleNameEnum> roles

) {
}
