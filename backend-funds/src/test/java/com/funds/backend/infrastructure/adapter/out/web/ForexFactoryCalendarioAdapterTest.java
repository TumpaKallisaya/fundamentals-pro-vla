package com.funds.backend.infrastructure.adapter.out.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.ObjectMapper;

import com.funds.backend.domain.model.EventoCalendario;
import com.funds.backend.domain.model.Impacto;

/**
 * Prueba unitaria del parseo/normalización del feed de Forex Factory.
 */
class ForexFactoryCalendarioAdapterTest {

    private ForexFactoryCalendarioAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ForexFactoryCalendarioAdapter(RestClient.create(), new ObjectMapper(), "http://test");
    }

    @Test
    void normalizaFiltrandoPaisEImpacto() throws Exception {
        String json = """
                [
                  {"title":"ISM Manufacturing PMI","country":"USD","date":"2026-11-02T10:00:00-05:00","impact":"High","forecast":"47.9","previous":"46.5"},
                  {"title":"CPI m/m","country":"USD","date":"2026-11-02T08:30:00-05:00","impact":"Medium","forecast":"0.2%","previous":"0.3%"},
                  {"title":"Unemployment Rate","country":"USD","date":"2026-11-02T08:30:00-05:00","impact":"Low","forecast":"4.0%","previous":"3.9%"},
                  {"title":"CPI y/y","country":"EUR","date":"2026-11-02T08:30:00+01:00","impact":"High","forecast":"2.0%","previous":"1.9%"},
                  {"title":"Non-Farm Employment Change","country":"USD","date":"2026-11-06T08:30:00-05:00","impact":"High","forecast":"150K","previous":"142K"}
                ]
                """;

        List<EventoCalendario> eventos = adapter.parsearJson(json);

        assertThat(eventos).hasSize(3);
        assertThat(eventos).extracting(EventoCalendario::titulo)
                .containsExactly("ISM Manufacturing PMI", "CPI m/m", "Non-Farm Employment Change");

        EventoCalendario ism = eventos.get(0);
        assertThat(ism.impacto()).isEqualTo(Impacto.ALTO);
        assertThat(ism.fechaHoraUtc()).isEqualTo(Instant.parse("2026-11-02T15:00:00Z"));
        assertThat(ism.forecast()).isEqualByComparingTo(new BigDecimal("47.9"));
        assertThat(ism.previous()).isEqualByComparingTo(new BigDecimal("46.5"));

        EventoCalendario cpi = eventos.get(1);
        assertThat(cpi.impacto()).isEqualTo(Impacto.MEDIO);
        assertThat(cpi.forecast()).isEqualByComparingTo(new BigDecimal("0.2"));
        assertThat(cpi.previous()).isEqualByComparingTo(new BigDecimal("0.3"));

        EventoCalendario nfp = eventos.get(2);
        assertThat(nfp.forecast()).isEqualByComparingTo(new BigDecimal("150000"));
        assertThat(nfp.previous()).isEqualByComparingTo(new BigDecimal("142000"));
    }

    @Test
    void valoresVaciosONoParseablesSeConviertenEnNull() throws Exception {
        String json = """
                [
                  {"title":"X","country":"USD","date":"2026-11-02T08:30:00-05:00","impact":"High","forecast":"","previous":"N/A"}
                ]
                """;
        List<EventoCalendario> eventos = adapter.parsearJson(json);

        assertThat(eventos).hasSize(1);
        assertThat(eventos.get(0).forecast()).isNull();
        assertThat(eventos.get(0).previous()).isNull();
    }
}
