package com.funds.backend.domain.model;

/**
 * Catálogo de índices financieros (p. ej. NAS100, SP500). Clave natural: {@code codigo}.
 */
public record Indice(
        String codigo,
        String nombre,
        String sector
) {
}
