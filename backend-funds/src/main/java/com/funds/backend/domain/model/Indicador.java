package com.funds.backend.domain.model;

/**
 * Catálogo de indicadores fundamentales. Clave natural: {@code id} (slug).
 */
public record Indicador(
        String id,
        String nombre,
        String aliasFf,
        String sector,
        Impacto impacto,
        String frecuencia,
        String horaEt,
        Fuente fuente,
        Regla regla
) {
}
