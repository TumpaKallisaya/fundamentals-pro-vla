package com.funds.backend.application.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.funds.backend.domain.model.ClasificacionTendencia;

/** DTO de respuesta: tendencia diaria por índice. */
public record TendenciaDto(
        String indiceCodigo,
        LocalDate fecha,
        BigDecimal puntaje,
        ClasificacionTendencia clasificacion
) {
}
