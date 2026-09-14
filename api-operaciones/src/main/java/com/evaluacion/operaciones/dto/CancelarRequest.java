package com.evaluacion.operaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CancelarRequest(
        @NotNull(message = "El id es obligatorio") Long id,
        @NotBlank(message = "La referencia es obligatoria") String referencia,
        @NotBlank(message = "El estatus es obligatorio")
        @Pattern(regexp = "(?i)^cancelar$", message = "El estatus debe contener el valor cancelar")
        String estatus
) {}
