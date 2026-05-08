package com.github.m4rcioliveira.financial_manager_v0002.security.util;

import com.github.m4rcioliveira.financial_manager_v0002.security.details.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class AutenticacaoUtil {

    private AutenticacaoUtil() {
    }

    public static UserDetailsImpl getAuthenticatedUser() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserDetailsImpl user)) {
            throw new RuntimeException("Usuário não autenticado");
        }

        return user;
    }

    public static UUID getAuthenticatedUserId() {

        return getAuthenticatedUser().getId();
    }

}
