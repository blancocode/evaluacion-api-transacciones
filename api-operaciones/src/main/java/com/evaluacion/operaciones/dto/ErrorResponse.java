package com.evaluacion.operaciones.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String mensaje,
        Map<String, String> errores
) {}
