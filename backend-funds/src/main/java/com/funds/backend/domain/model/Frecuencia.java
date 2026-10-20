package com.funds.backend.domain.model;

import java.time.DayOfWeek;

/**
 * Frecuencia estructurada de publicación de un indicador.
 *
 * @param tipo      tipo de frecuencia
 * @param diaSemana día de la semana (solo para {@code SEMANAL})
 * @param diaMes    día aproximado del mes, 1–28 (solo para {@code MENSUAL})
 */
public record Frecuencia(
        TipoFrecuencia tipo,
        DayOfWeek diaSemana,
        Integer diaMes
) {
}
