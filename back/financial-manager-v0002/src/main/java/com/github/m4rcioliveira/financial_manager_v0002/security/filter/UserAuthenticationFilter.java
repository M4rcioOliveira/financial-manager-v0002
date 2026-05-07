package com.github.m4rcioliveira.financial_manager_v0002.security.filter;

import com.github.m4rcioliveira.financial_manager_v0002.constantes.ArquiteturaConstantes;
import com.github.m4rcioliveira.financial_manager_v0002.exception.NotFoundException;
import com.github.m4rcioliveira.financial_manager_v0002.model.User;
import com.github.m4rcioliveira.financial_manager_v0002.repository.UserRepository;
import com.github.m4rcioliveira.financial_manager_v0002.security.details.UserDetailsImpl;
import com.github.m4rcioliveira.financial_manager_v0002.security.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    private final UserRepository userRepository;

    private static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            ArquiteturaConstantes.BASE_PATH_REQUEST_MAPPING + ArquiteturaConstantes.PATH_USER,
            ArquiteturaConstantes.BASE_PATH_REQUEST_MAPPING + ArquiteturaConstantes.PATH_USER + "/login"
    };

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // Verifica se o endpoint requer autenticação antes de processar a requisição
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request); // Recupera o token do cabeçalho Authorization da requisição
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token); // Obtém o assunto (neste caso, o nome de usuário) do token
                Optional<User> user = userRepository.findByEmail(subject);

                // Busca o usuário pelo email (que é o assunto do token)
                UserDetailsImpl userDetails = new UserDetailsImpl(user.orElseThrow(() -> new NotFoundException("Usuário não encontrado"))); // Cria um UserDetails com o usuário encontrado

                // Cria um objeto de autenticação do Spring Security
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                // Define o objeto de autenticação no contexto de segurança do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new NotFoundException("O token está ausente.");
            }
        }
        filterChain.doFilter(request, response); // Continua o processamento da requisição
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.info("RequestURI que chegou {}", requestURI);
        return !Arrays.asList(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }

}
