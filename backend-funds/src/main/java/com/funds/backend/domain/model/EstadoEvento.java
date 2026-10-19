package com.funds.backend.domain.model;

/**
 * Clasificación por evento según real vs. pronóstico.
 */
public enum EstadoEvento {
    /** Mejor de lo esperado (favorable al índice). */
    VERDE,
    /** Dentro de lo esperado (dentro de tolerancia). */
    AMARILLO,
    /** Peor de lo esperado (desfavorable al índice). */
    ROJO,
    /** Sin datos suficientes (actual/pronóstico nulos). */
    PENDIENTE
}
