package com.evaluacion.transacciones.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Transacciones",
                version = "1.0.0",
                description = "API interna para persistir, consultar y actualizar transacciones"
        )
)
public class OpenApiConfig {
}
