package com.funds.backend.domain.port.out;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: obtiene el valor "actual" observado de un indicador desde la fuente oficial
 * (FRED/BLS).
 */
public interface ValorActualPort {

    /**
     * Devuelve el valor "actual" (observado) de un indicador, si está disponible.
     *
     * @param indicadorId  identificador del indicador
     * @param fechaHoraUtc instante del evento (UTC); se usa como referencia para la observación
     * @return el valor actual en la misma unidad que el pronóstico, o {@code Optional.empty()}
     */
    Optional<BigDecimal> obtenerActual(String indicadorId, Instant fechaHoraUtc);

    /**
     * Devuelve los últimos valores "actuales" (observados) del indicador, el más reciente primero.
     *
     * @param indicadorId identificador del indicador
     * @param cantidad    número máximo de valores a devolver
     * @return lista de valores (más reciente primero), posiblemente vacía
     */
    List<BigDecimal> obtenerUltimos(String indicadorId, int cantidad);
}
