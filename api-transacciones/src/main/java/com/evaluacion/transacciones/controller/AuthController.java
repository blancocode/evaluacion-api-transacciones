package com.evaluacion.transacciones.controller;

import com.evaluacion.transacciones.dto.LoginRequest;
import com.evaluacion.transacciones.dto.LoginResponse;
import com.evaluacion.transacciones.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interno/auth")
@Tag(name = "Autenticación interna")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Validar credenciales del usuario")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.validar(request);
    }
}
