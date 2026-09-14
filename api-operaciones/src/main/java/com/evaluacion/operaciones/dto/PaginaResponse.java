package com.evaluacion.operaciones.dto;

import java.util.List;

public record PaginaResponse<T>(
        List<T> contenido,
        int pagina,
        int registrosPorPagina,
        long totalRegistros,
        int totalPaginas,
        boolean ultimaPagina
) {}
