package com.funds.backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Tendencia diaria proyectada por índice y día (día en America/New_York).
 */
public record TendenciaDiaria(
        Long id,
        String indiceCodigo,
        LocalDate fecha,
        BigDecimal puntaje,
        ClasificacionTendencia clasificacion
) {
}
