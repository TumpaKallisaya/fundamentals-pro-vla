package com.funds.backend.domain.model;

import java.math.BigDecimal;

/**
 * Valor observado (actual) de un evento, capturado durante un refresco concreto, junto con su
 * clasificación (estado) y desviación frente al pronóstico.
 */
public record Resultado(
        Long id,
        Long eventoId,
        Long refrescoId,
        BigDecimal actual,
        EstadoEvento estado,
        BigDecimal desviacion
) {
}
