package com.funds.backend.domain.model;

/**
 * Value object: fuente de datos de un indicador.
 *
 * @param nombre nombre de la fuente (p. ej. "Forex Factory")
 * @param tipo   tipo de fuente (p. ej. "api")
 * @param url    endpoint o URL de la fuente
 * @param notas  notas o limitaciones de la fuente
 */
public record Fuente(
        String nombre,
        String tipo,
        String url,
        String notas
) {
}
