package com.funds.backend.domain.model;

import java.math.BigDecimal;

/**
 * Value object: regla de interpretación de un indicador.
 *
 * @param direccion     dirección favorable (efecto de un valor mayor al pronóstico)
 * @param peso          ponderación por impacto (p. ej. Alto=3, Medio=1)
 * @param toleranciaPct umbral ±X% sobre el pronóstico dentro del cual el resultado es "esperado"
 */
public record Regla(
        Direccion direccion,
        int peso,
        BigDecimal toleranciaPct
) {
}
