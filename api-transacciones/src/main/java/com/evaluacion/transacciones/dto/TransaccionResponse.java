package com.evaluacion.transacciones.dto;

import java.math.BigDecimal;

public record TransaccionResponse(
        Long id,
        String estatus,
        String referencia,
        String operacion,
        BigDecimal importe,
        String cliente
) {}
