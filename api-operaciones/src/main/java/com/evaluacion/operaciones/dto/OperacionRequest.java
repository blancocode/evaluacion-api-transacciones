package com.evaluacion.operaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OperacionRequest(
        @NotBlank(message = "La operación es obligatoria")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La operación solo debe contener caracteres")
        @Size(min = 3, max = 30, message = "La operación debe tener entre 3 y 30 caracteres")
        String operacion,

        @NotBlank(message = "El importe es obligatorio")
        @Pattern(regexp = "^\\d{1,10}\\.\\d{2}$", message = "El importe debe tener formato de moneda, ejemplo 100.00")
        String importe,

        @NotBlank(message = "El cliente es obligatorio")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El cliente solo debe contener caracteres")
        @Size(min = 2, max = 60, message = "El cliente debe tener entre 2 y 60 caracteres")
        String cliente,

        @NotBlank(message = "El secreto es obligatorio")
        String secreto
) {}
