package com.funds.backend.infrastructure.adapter.out.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.ObjectMapper;

/**
 * Prueba unitaria del parseo y transformación del adaptador FRED.
 */
class FredBlsValorActualAdapterTest {

    private FredBlsValorActualAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FredBlsValorActualAdapter(RestClient.create(), new ObjectMapper(), "");
    }

    @Test
    void parseaObservacionesIgnorandoValoresFaltantes() throws Exception {
        String json = """
                {"observations":[
                  {"date":"2026-10-01","value":"315.2"},
                  {"date":"2026-09-01","value":"314.8"},
                  {"date":"2026-08-01","value":"."}
                ]}
                """;
        List<BigDecimal> valores = adapter.parsearObservaciones(json);
        assertThat(valores).containsExactly(new BigDecimal("315.2"), new BigDecimal("314.8"));
    }

    @Test
    void transformaCambioConEscala() {
        FredSerie serie = new FredSerie("nonfarm_payrolls", "PAYEMS", "CAMBIO", new BigDecimal("1000"));
        List<BigDecimal> valores = List.of(new BigDecimal("159000"), new BigDecimal("158800"));
        assertThat(adapter.aplicarTransformacion(serie, valores)).isEqualByComparingTo("200000");
    }

    @Test
    void transformaPctCambio() {
        FredSerie serie = new FredSerie("cpi", "CPIAUCSL", "PCT_CAMBIO", BigDecimal.ONE);
        List<BigDecimal> valores = List.of(new BigDecimal("110"), new BigDecimal("100"));
        assertThat(adapter.aplicarTransformacion(serie, valores)).isEqualByComparingTo("10");
    }

    @Test
    void transformaDirecta() {
        FredSerie serie = new FredSerie("tasa_desempleo", "UNRATE", "DIRECTA", BigDecimal.ONE);
        List<BigDecimal> valores = List.of(new BigDecimal("4.2"), new BigDecimal("4.1"));
        assertThat(adapter.aplicarTransformacion(serie, valores)).isEqualByComparingTo("4.2");
    }
}
