package com.funds.backend.infrastructure.adapter.out.web;

import java.math.BigDecimal;

/**
 * Entrada del catálogo de series: mapea un indicador a su serie FRED/BLS y su transformación.
 */
public record FredSerie(
        String indicadorId,
        String serieId,
        String transformacion,
        BigDecimal escala
) {
}
