package com.funds.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.funds.backend.domain.model.ClasificacionTendencia;
import com.funds.backend.domain.model.EstadoEvento;
import com.funds.backend.domain.model.Relevancia;

/**
 * Prueba unitaria del agregador de tendencia diaria (determinista).
 */
class AgregadorTendenciaServiceTest {

    @Test
    void puntajePositivoEsAlcista() {
        var aportes = List.of(new AgregadorTendenciaService.Aporte(EstadoEvento.VERDE, 3, Relevancia.ALTA));
        BigDecimal puntaje = AgregadorTendenciaService.calcularPuntaje(aportes);
        assertThat(puntaje).isEqualByComparingTo("3");
        assertThat(AgregadorTendenciaService.clasificar(puntaje)).isEqualTo(ClasificacionTendencia.ALCISTA);
    }

    @Test
    void puntajeNegativoEsBajista() {
        var aportes = List.of(new AgregadorTendenciaService.Aporte(EstadoEvento.ROJO, 3, Relevancia.ALTA));
        BigDecimal puntaje = AgregadorTendenciaService.calcularPuntaje(aportes);
        assertThat(puntaje).isEqualByComparingTo("-3");
        assertThat(AgregadorTendenciaService.clasificar(puntaje)).isEqualTo(ClasificacionTendencia.BAJISTA);
    }

    @Test
    void amarilloYRelevanciaBajaNoAportan() {
        var aportes = List.of(
                new AgregadorTendenciaService.Aporte(EstadoEvento.AMARILLO, 3, Relevancia.ALTA),
                new AgregadorTendenciaService.Aporte(EstadoEvento.VERDE, 3, Relevancia.BAJA)
        );
        BigDecimal puntaje = AgregadorTendenciaService.calcularPuntaje(aportes);
        assertThat(puntaje).isEqualByComparingTo("0");
        assertThat(AgregadorTendenciaService.clasificar(puntaje)).isEqualTo(ClasificacionTendencia.NEUTRAL);
    }

    @Test
    void relevanciaMediaPesaLaMitad() {
        var aportes = List.of(new AgregadorTendenciaService.Aporte(EstadoEvento.VERDE, 2, Relevancia.MEDIA));
        assertThat(AgregadorTendenciaService.calcularPuntaje(aportes)).isEqualByComparingTo("1");
    }
}
