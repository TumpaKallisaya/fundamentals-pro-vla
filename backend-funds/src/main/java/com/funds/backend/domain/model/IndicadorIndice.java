package com.funds.backend.domain.model;

/**
 * Relación indicador ↔ índice con su relevancia. Referencia a los agregados por clave natural.
 */
public record IndicadorIndice(
        Long id,
        String indicadorId,
        String indiceCodigo,
        Relevancia relevancia
) {
}
