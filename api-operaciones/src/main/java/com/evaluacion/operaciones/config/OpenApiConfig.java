package com.evaluacion.operaciones.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Operaciones",
                version = "1.0.0",
                description = "API de entrada para validar y procesar operaciones"
        )
)
public class OpenApiConfig {
}
