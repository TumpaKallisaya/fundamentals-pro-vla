package com.funds.backend.domain.model;

import java.time.Instant;

/**
 * Trazabilidad de una operación de refresco de datos (histórico).
 */
public record Refresco(
        Long id,
        Instant iniciadoEnUtc,
        Instant finalizadoEnUtc,
        EstadoRefresco estado,
        String detalle
) {
}
