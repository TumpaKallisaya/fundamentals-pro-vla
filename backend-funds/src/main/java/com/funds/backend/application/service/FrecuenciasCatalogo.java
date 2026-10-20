package com.funds.backend.application.service;

import java.time.DayOfWeek;
import java.util.Map;

import com.funds.backend.domain.model.Frecuencia;
import com.funds.backend.domain.model.TipoFrecuencia;

/**
 * Catálogo de frecuencias estructuradas por indicador (configuración, para el cálculo de la
 * próxima fecha de publicación). Los días del mes son aproximados; la fecha exacta la aporta
 * Forex Factory cuando está disponible.
 */
public final class FrecuenciasCatalogo {

    private FrecuenciasCatalogo() {
    }

    private static final Map<String, Frecuencia> FRECUENCIAS = Map.ofEntries(
            Map.entry("fomc_tasas", new Frecuencia(TipoFrecuencia.IRREGULAR, null, null)),
            Map.entry("nonfarm_payrolls", new Frecuencia(TipoFrecuencia.MENSUAL, null, 5)),
            Map.entry("tasa_desempleo", new Frecuencia(TipoFrecuencia.MENSUAL, null, 5)),
            Map.entry("salarios_hora", new Frecuencia(TipoFrecuencia.MENSUAL, null, 5)),
            Map.entry("cpi", new Frecuencia(TipoFrecuencia.MENSUAL, null, 13)),
            Map.entry("core_cpi", new Frecuencia(TipoFrecuencia.MENSUAL, null, 13)),
            Map.entry("pce", new Frecuencia(TipoFrecuencia.MENSUAL, null, 28)),
            Map.entry("core_pce", new Frecuencia(TipoFrecuencia.MENSUAL, null, 28)),
            Map.entry("pib", new Frecuencia(TipoFrecuencia.TRIMESTRAL, null, null)),
            Map.entry("ism_manufacturero", new Frecuencia(TipoFrecuencia.MENSUAL, null, 1)),
            Map.entry("ism_servicios", new Frecuencia(TipoFrecuencia.MENSUAL, null, 3)),
            Map.entry("ventas_minoristas", new Frecuencia(TipoFrecuencia.MENSUAL, null, 13)),
            Map.entry("jobless_claims", new Frecuencia(TipoFrecuencia.SEMANAL, DayOfWeek.THURSDAY, null)),
            Map.entry("adp", new Frecuencia(TipoFrecuencia.MENSUAL, null, 3)),
            Map.entry("jolts", new Frecuencia(TipoFrecuencia.MENSUAL, null, 7)),
            Map.entry("ppi", new Frecuencia(TipoFrecuencia.MENSUAL, null, 13)),
            Map.entry("bienes_duraderos", new Frecuencia(TipoFrecuencia.MENSUAL, null, 25)),
            Map.entry("inventarios_mayoristas", new Frecuencia(TipoFrecuencia.MENSUAL, null, 10)),
            Map.entry("balanza_comercial", new Frecuencia(TipoFrecuencia.MENSUAL, null, 5)),
            Map.entry("umich", new Frecuencia(TipoFrecuencia.MENSUAL, null, 13)),
            Map.entry("confianza_consumidor", new Frecuencia(TipoFrecuencia.MENSUAL, null, 27)),
            Map.entry("produccion_industrial", new Frecuencia(TipoFrecuencia.MENSUAL, null, 15)),
            Map.entry("ventas_viviendas", new Frecuencia(TipoFrecuencia.MENSUAL, null, 24))
    );

    public static Frecuencia de(String indicadorId) {
        return FRECUENCIAS.get(indicadorId);
    }
}
