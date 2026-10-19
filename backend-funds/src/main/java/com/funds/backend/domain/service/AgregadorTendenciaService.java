package com.funds.backend.domain.service;

import java.math.BigDecimal;
import java.util.List;

import com.funds.backend.domain.model.ClasificacionTendencia;
import com.funds.backend.domain.model.EstadoEvento;
import com.funds.backend.domain.model.Relevancia;

/**
 * Servicio de dominio (puro, determinista): agrega los resultados del día en un puntaje ponderado
 * por impacto y relevancia, y lo clasifica en ALCISTA/NEUTRAL/BAJISTA.
 */
public final class AgregadorTendenciaService {

    private AgregadorTendenciaService() {
    }

    /** Aporte de un evento clasificado al puntaje de un índice. */
    public record Aporte(EstadoEvento estado, int peso, Relevancia relevancia) {

        public int signo() {
            return switch (estado) {
                case VERDE -> 1;
                case ROJO -> -1;
                default -> 0;
            };
        }
    }

    /**
     * Calcula el puntaje ponderado del día: suma de (signo × peso × factor de relevancia).
     */
    public static BigDecimal calcularPuntaje(List<Aporte> aportes) {
        return aportes.stream()
                .map(a -> BigDecimal.valueOf((long) a.signo() * a.peso()).multiply(factorRelevancia(a.relevancia())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Clasifica el puntaje: &gt; 0 → ALCISTA, &lt; 0 → BAJISTA, = 0 → NEUTRAL.
     */
    public static ClasificacionTendencia clasificar(BigDecimal puntaje) {
        int cmp = puntaje.compareTo(BigDecimal.ZERO);
        if (cmp > 0) {
            return ClasificacionTendencia.ALCISTA;
        }
        if (cmp < 0) {
            return ClasificacionTendencia.BAJISTA;
        }
        return ClasificacionTendencia.NEUTRAL;
    }

    private static BigDecimal factorRelevancia(Relevancia relevancia) {
        return switch (relevancia) {
            case ALTA -> BigDecimal.ONE;
            case MEDIA -> new BigDecimal("0.5");
            case BAJA -> BigDecimal.ZERO;
        };
    }
}
