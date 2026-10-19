package com.funds.backend.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Evento de calendario económico devuelto por una fuente externa (normalizado por el adaptador).
 *
 * <p>Representa un evento de alto/medio impacto de EE.UU. con su pronóstico y valor anterior.
 * El valor {@code actual} no está disponible en el feed gratuito de Forex Factory (Fase 1).</p>
 */
public record EventoCalendario(
        String titulo,
        String pais,
        Instant fechaHoraUtc,
        Impacto impacto,
        BigDecimal forecast,
        BigDecimal previous
) {
}
