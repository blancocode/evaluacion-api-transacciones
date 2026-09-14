package com.evaluacion.transacciones.service;

import com.evaluacion.transacciones.dto.LoginRequest;
import com.evaluacion.transacciones.dto.LoginResponse;
import com.evaluacion.transacciones.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse validar(LoginRequest request) {
        boolean valido = usuarioRepository.findByUsuario(request.usuario())
                .map(usuario -> passwordEncoder.matches(request.password(), usuario.getPassword()))
                .orElse(false);

        return valido
                ? new LoginResponse(true, "Inicio de sesión correcto")
                : new LoginResponse(false, "Usuario o password incorrectos");
    }
}
