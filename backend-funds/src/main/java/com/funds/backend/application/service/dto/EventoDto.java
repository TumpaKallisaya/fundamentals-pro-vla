package com.funds.backend.application.service.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.funds.backend.domain.model.EstadoEvento;

/** DTO de respuesta: evento con su resultado y clasificación. */
public record EventoDto(
        Long id,
        String indicadorId,
        String nombre,
        Instant fechaHoraUtc,
        BigDecimal forecast,
        BigDecimal previous,
        BigDecimal actual,
        EstadoEvento estado,
        BigDecimal desviacion
) {
}
