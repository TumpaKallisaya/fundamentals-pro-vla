package com.funds.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.funds.backend.domain.model.Direccion;
import com.funds.backend.domain.model.EstadoEvento;
import com.funds.backend.domain.model.Regla;

/**
 * Prueba unitaria del clasificador de eventos (determinista).
 */
class ClasificadorEventoServiceTest {

    private static Regla alcista() {
        return new Regla(Direccion.MAYOR_ES_ALCISTA, 3, new BigDecimal("10"));
    }

    private static Regla bajista() {
        return new Regla(Direccion.MAYOR_ES_BAJISTA, 3, new BigDecimal("10"));
    }

    @Test
    void pendienteCuandoFaltanDatos() {
        assertThat(ClasificadorEventoService.clasificar(null, new BigDecimal("100"), alcista()))
                .isEqualTo(EstadoEvento.PENDIENTE);
        assertThat(ClasificadorEventoService.clasificar(new BigDecimal("100"), null, alcista()))
                .isEqualTo(EstadoEvento.PENDIENTE);
    }

    @Test
    void amarilloDentroDeTolerancia() {
        assertThat(ClasificadorEventoService.clasificar(new BigDecimal("105"), new BigDecimal("100"), alcista()))
                .isEqualTo(EstadoEvento.AMARILLO);
        assertThat(ClasificadorEventoService.clasificar(new BigDecimal("95"), new BigDecimal("100"), alcista()))
                .isEqualTo(EstadoEvento.AMARILLO);
    }

    @Test
    void verdeRojoSegunDireccion() {
        // Alcista: mayor (fuera de tolerancia) → VERDE; menor → ROJO
        assertThat(ClasificadorEventoService.clasificar(new BigDecimal("120"), new BigDecimal("100"), alcista()))
                .isEqualTo(EstadoEvento.VERDE);
        assertThat(ClasificadorEventoService.clasificar(new BigDecimal("80"), new BigDecimal("100"), alcista()))
                .isEqualTo(EstadoEvento.ROJO);

        // Bajista: mayor (fuera de tolerancia) → ROJO; menor → VERDE
        assertThat(ClasificadorEventoService.clasificar(new BigDecimal("120"), new BigDecimal("100"), bajista()))
                .isEqualTo(EstadoEvento.ROJO);
        assertThat(ClasificadorEventoService.clasificar(new BigDecimal("80"), new BigDecimal("100"), bajista()))
                .isEqualTo(EstadoEvento.VERDE);
    }
}
