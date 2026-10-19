package com.funds.backend.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Ocurrencia de calendario de un indicador: fecha/hora (UTC), pronóstico y valor anterior.
 */
public record Evento(
        Long id,
        String indicadorId,
        Instant fechaHoraUtc,
        BigDecimal forecast,
        BigDecimal previous
) {
}
