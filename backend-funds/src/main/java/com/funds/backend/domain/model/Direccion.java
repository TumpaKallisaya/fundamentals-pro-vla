package com.funds.backend.domain.model;

/** Dirección favorable de un indicador: cómo se interpreta un valor mayor al pronóstico. */
public enum Direccion {
    /** Un valor mayor al pronóstico favorece al índice (alcista). */
    MAYOR_ES_ALCISTA,
    /** Un valor mayor al pronóstico perjudica al índice (bajista). */
    MAYOR_ES_BAJISTA
}
