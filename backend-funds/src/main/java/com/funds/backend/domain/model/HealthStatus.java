package com.funds.backend.domain.model;

import java.time.ZonedDateTime;

/**
 * Modelo de dominio: estado de salud del servicio.
 *
 * <p>Pertenece al núcleo (capa de dominio). No depende de ningún framework.</p>
 *
 * @param service   nombre del servicio
 * @param status    estado del servicio (p. ej. {@code UP})
 * @param timestamp instante de la consulta en la zona horaria configurada (America/New_York)
 */
public record HealthStatus(
        String service,
        String status,
        ZonedDateTime timestamp
) {
}
