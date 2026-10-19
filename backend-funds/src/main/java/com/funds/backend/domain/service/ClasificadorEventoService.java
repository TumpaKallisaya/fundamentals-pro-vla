package com.funds.backend.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.funds.backend.domain.model.Direccion;
import com.funds.backend.domain.model.EstadoEvento;
import com.funds.backend.domain.model.Regla;

/**
 * Servicio de dominio (puro, determinista): clasifica un resultado en VERDE/AMARILLO/ROJO/PENDIENTE
 * comparando el valor real contra el pronóstico, la dirección y la tolerancia del indicador.
 */
public final class ClasificadorEventoService {

    private ClasificadorEventoService() {
    }

    /**
     * Clasifica el resultado de un evento.
     *
     * @param actual    valor observado (puede ser null)
     * @param forecast  valor pronosticado (puede ser null)
     * @param regla     regla de interpretación del indicador
     * @return el estado del evento
     */
    public static EstadoEvento clasificar(BigDecimal actual, BigDecimal forecast, Regla regla) {
        if (actual == null || forecast == null || regla == null) {
            return EstadoEvento.PENDIENTE;
        }
        BigDecimal desviacion = actual.subtract(forecast);
        if (forecast.compareTo(BigDecimal.ZERO) == 0) {
            return desviacion.compareTo(BigDecimal.ZERO) == 0
                    ? EstadoEvento.AMARILLO
                    : porDireccion(desviacion, regla.direccion());
        }
        BigDecimal relativa = desviacion.abs().divide(forecast.abs(), 10, RoundingMode.HALF_UP);
        BigDecimal tolerancia = regla.toleranciaPct().divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        if (relativa.compareTo(tolerancia) <= 0) {
            return EstadoEvento.AMARILLO;
        }
        return porDireccion(desviacion, regla.direccion());
    }

    private static EstadoEvento porDireccion(BigDecimal desviacion, Direccion direccion) {
        boolean mayor = desviacion.compareTo(BigDecimal.ZERO) > 0;
        if (direccion == Direccion.MAYOR_ES_ALCISTA) {
            return mayor ? EstadoEvento.VERDE : EstadoEvento.ROJO;
        }
        return mayor ? EstadoEvento.ROJO : EstadoEvento.VERDE;
    }
}
