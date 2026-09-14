package com.evaluacion.transacciones.config;

import com.evaluacion.transacciones.entity.Usuario;
import com.evaluacion.transacciones.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarUsuario(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByUsuario("admin").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsuario("admin");
                usuario.setPassword(passwordEncoder.encode("Admin123*"));
                repository.save(usuario);
            }
        };
    }
}
