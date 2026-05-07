package com.github.m4rcioliveira.financial_manager_v0002.security.config;

import com.github.m4rcioliveira.financial_manager_v0002.constantes.ArquiteturaConstantes;
import com.github.m4rcioliveira.financial_manager_v0002.security.filter.UserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserAuthenticationFilter userAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ).authorizeHttpRequests(auth -> auth
                .requestMatchers(ArquiteturaConstantes.BASE_PATH_REQUEST_MAPPING + ArquiteturaConstantes.PATH_USER).permitAll() //Pode ser usado algo como /** para liberar demais todas rotas
                .requestMatchers(ArquiteturaConstantes.BASE_PATH_REQUEST_MAPPING + ArquiteturaConstantes.PATH_USER + "/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMINISTRATOR")
                .anyRequest().authenticated()
        ).addFilterBefore(
                userAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
