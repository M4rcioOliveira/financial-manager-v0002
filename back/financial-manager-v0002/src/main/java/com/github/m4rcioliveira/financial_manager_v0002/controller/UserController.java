package com.github.m4rcioliveira.financial_manager_v0002.controller;

import com.github.m4rcioliveira.financial_manager_v0002.constantes.ArquiteturaConstantes;
import com.github.m4rcioliveira.financial_manager_v0002.dto.JwtTokenDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.LoginUserDTO;
import com.github.m4rcioliveira.financial_manager_v0002.dto.NovoUserDTO;
import com.github.m4rcioliveira.financial_manager_v0002.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ArquiteturaConstantes.BASE_PATH_REQUEST_MAPPING + "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> authenticateUser(@RequestBody LoginUserDTO loginUserDto) {
        JwtTokenDTO token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody NovoUserDTO createUserDto) {
        userService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
