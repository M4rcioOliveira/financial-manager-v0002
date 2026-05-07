package com.github.m4rcioliveira.financial_manager_v0002.service;

import com.github.m4rcioliveira.financial_manager_v0002.dto.CriarUserDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.JwtTokenDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.LoginUserDTO;
import com.github.m4rcioliveira.financial_manager_v0002.enums.RoleNameEnum;
import com.github.m4rcioliveira.financial_manager_v0002.model.Role;
import com.github.m4rcioliveira.financial_manager_v0002.model.User;
import com.github.m4rcioliveira.financial_manager_v0002.repository.UserRepository;
import com.github.m4rcioliveira.financial_manager_v0002.security.config.SecurityConfiguration;
import com.github.m4rcioliveira.financial_manager_v0002.security.details.UserDetailsImpl;
import com.github.m4rcioliveira.financial_manager_v0002.security.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;

    public JwtTokenDTO autenticarUser(LoginUserDTO loginUserDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDTO.email(), loginUserDTO.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtTokenDTO(jwtTokenService.generateToken(userDetails));
    }

    public void criarUser(CriarUserDTO criarUserDTO) {

        User newUser = User.builder()
                .email(criarUserDTO.email())
                .password(securityConfiguration.passwordEncoder().encode(criarUserDTO.password()))
                .roles(mapperRoles(criarUserDTO.roles()))
                .build();

        userRepository.save(newUser);
    }

    public List<Role> mapperRoles(List<RoleNameEnum> rolesDTO) {
        return rolesDTO.stream()
                .map(roleName -> Role.builder()
                        .name(roleName)
                        .build())
                .toList();
    }

}
