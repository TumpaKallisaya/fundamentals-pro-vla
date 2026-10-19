package com.funds.backend.domain.model;

import java.math.BigDecimal;

/**
 * Valor observado (actual) de un evento, capturado durante un refresco concreto.
 * El modelo es histórico: una fila por evento y por refresco.
 */
public record Resultado(
        Long id,
        Long eventoId,
        Long refrescoId,
        BigDecimal actual
) {
}
