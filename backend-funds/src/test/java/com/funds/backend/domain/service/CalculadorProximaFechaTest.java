package com.funds.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.funds.backend.domain.model.Frecuencia;
import com.funds.backend.domain.model.TipoFrecuencia;

/**
 * Prueba unitaria del calculador de próxima fecha de publicación.
 */
class CalculadorProximaFechaTest {

    @Test
    void semanalDevuelveProximoDia() {
        // martes 20-oct-2026 → próximo jueves 22-oct
        Frecuencia semanal = new Frecuencia(TipoFrecuencia.SEMANAL, DayOfWeek.THURSDAY, null);
        LocalDate hoy = LocalDate.of(2026, 10, 20);
        assertThat(CalculadorProximaFecha.siguienteFecha(semanal, hoy))
                .contains(LocalDate.of(2026, 10, 22));
    }

    @Test
    void mensualDevuelveEsteMesSiNoPaso() {
        Frecuencia mensual = new Frecuencia(TipoFrecuencia.MENSUAL, null, 13);
        LocalDate hoy = LocalDate.of(2026, 10, 1);
        assertThat(CalculadorProximaFecha.siguienteFecha(mensual, hoy))
                .contains(LocalDate.of(2026, 10, 13));
    }

    @Test
    void mensualDevuelveSiguienteMesSiPaso() {
        Frecuencia mensual = new Frecuencia(TipoFrecuencia.MENSUAL, null, 13);
        LocalDate hoy = LocalDate.of(2026, 10, 20);
        assertThat(CalculadorProximaFecha.siguienteFecha(mensual, hoy))
                .contains(LocalDate.of(2026, 11, 13));
    }

    @Test
    void trimestralDevuelveCierre() {
        Frecuencia trimestral = new Frecuencia(TipoFrecuencia.TRIMESTRAL, null, null);
        LocalDate hoy = LocalDate.of(2026, 10, 20);
        assertThat(CalculadorProximaFecha.siguienteFecha(trimestral, hoy))
                .contains(LocalDate.of(2026, 12, 31));
    }

    @Test
    void irregularDevuelveVacio() {
        Frecuencia irregular = new Frecuencia(TipoFrecuencia.IRREGULAR, null, null);
        assertThat(CalculadorProximaFecha.siguienteFecha(irregular, LocalDate.of(2026, 10, 20)))
                .isEmpty();
    }

    @Test
    void fechasPreviasSemanales() {
        Frecuencia semanal = new Frecuencia(TipoFrecuencia.SEMANAL, DayOfWeek.THURSDAY, null);
        LocalDate hoy = LocalDate.of(2026, 10, 20); // martes
        assertThat(CalculadorProximaFecha.fechasPrevias(semanal, hoy, 3))
                .containsExactly(
                        LocalDate.of(2026, 10, 15),
                        LocalDate.of(2026, 10, 8),
                        LocalDate.of(2026, 10, 1)
                );
    }

    @Test
    void fechasPreviasMensuales() {
        Frecuencia mensual = new Frecuencia(TipoFrecuencia.MENSUAL, null, 13);
        LocalDate hoy = LocalDate.of(2026, 10, 20);
        assertThat(CalculadorProximaFecha.fechasPrevias(mensual, hoy, 3))
                .containsExactly(
                        LocalDate.of(2026, 10, 13),
                        LocalDate.of(2026, 9, 13),
                        LocalDate.of(2026, 8, 13)
                );
    }
}
