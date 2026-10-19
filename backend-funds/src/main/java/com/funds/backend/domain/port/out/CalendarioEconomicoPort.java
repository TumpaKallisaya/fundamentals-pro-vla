package com.funds.backend.domain.port.out;

import java.util.List;

import com.funds.backend.domain.model.EventoCalendario;

/**
 * Puerto de salida: obtiene el calendario económico de la fuente externa configurada
 * (Forex Factory), ya normalizado al modelo de dominio.
 */
public interface CalendarioEconomicoPort {

    /**
     * Devuelve los eventos de calendario de alto/medio impacto de EE.UU.
     *
     * @return lista de eventos normalizados (posiblemente vacía)
     */
    List<EventoCalendario> obtenerEventos();
}
