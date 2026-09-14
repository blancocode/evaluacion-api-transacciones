package com.evaluacion.transacciones.dto;

import java.math.BigDecimal;

public record TransaccionRequest(
        String operacion,
        BigDecimal importe,
        String cliente,
        String secreto
) {}
