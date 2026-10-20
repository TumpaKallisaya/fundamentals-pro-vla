package com.funds.backend.domain.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Frecuencia;
import com.funds.backend.domain.model.TipoFrecuencia;

/**
 * Servicio de dominio (puro, determinista): calcula la próxima fecha de publicación de un
 * indicador a partir de su frecuencia estructurada.
 */
public final class CalculadorProximaFecha {

    private CalculadorProximaFecha() {
    }

    /**
     * Devuelve la próxima fecha de publicación (estrictamente posterior a {@code hoy}).
     *
     * @return la fecha, o {@code Optional.empty()} si la frecuencia es irregular/desconocida
     */
    public static Optional<LocalDate> siguienteFecha(Frecuencia frecuencia, LocalDate hoy) {
        if (frecuencia == null || frecuencia.tipo() == null) {
            return Optional.empty();
        }
        return switch (frecuencia.tipo()) {
            case SEMANAL -> proximoDiaSemana(frecuencia.diaSemana(), hoy);
            case MENSUAL -> proximoDiaMes(frecuencia.diaMes(), hoy);
            case TRIMESTRAL -> proximoCierreTrimestre(hoy);
            case IRREGULAR -> Optional.empty();
        };
    }

    private static Optional<LocalDate> proximoDiaSemana(DayOfWeek dia, LocalDate hoy) {
        if (dia == null) {
            return Optional.empty();
        }
        LocalDate siguiente = hoy.plusDays(1);
        while (siguiente.getDayOfWeek() != dia) {
            siguiente = siguiente.plusDays(1);
        }
        return Optional.of(siguiente);
    }

    private static Optional<LocalDate> proximoDiaMes(Integer diaMes, LocalDate hoy) {
        if (diaMes == null) {
            return Optional.empty();
        }
        int dia = Math.max(1, Math.min(diaMes, 28));
        int ultimoDiaMes = hoy.lengthOfMonth();
        if (dia <= ultimoDiaMes && dia > hoy.getDayOfMonth()) {
            return Optional.of(hoy.withDayOfMonth(dia));
        }
        LocalDate siguienteMes = hoy.withDayOfMonth(1).plusMonths(1);
        int diaEfectivo = Math.min(dia, siguienteMes.lengthOfMonth());
        return Optional.of(siguienteMes.withDayOfMonth(diaEfectivo));
    }

    private static Optional<LocalDate> proximoCierreTrimestre(LocalDate hoy) {
        int mes = hoy.getMonthValue();
        int mesCierre = ((mes - 1) / 3 + 1) * 3; // 3, 6, 9 o 12
        LocalDate cierre = LocalDate.of(hoy.getYear(), mesCierre, 1)
                .withDayOfMonth(LocalDate.of(hoy.getYear(), mesCierre, 1).lengthOfMonth());
        if (!cierre.isAfter(hoy)) {
            int anio = mesCierre == 12 ? hoy.getYear() + 1 : hoy.getYear();
            int mesSig = mesCierre == 12 ? 3 : mesCierre + 3;
            cierre = LocalDate.of(anio, mesSig, 1)
                    .withDayOfMonth(LocalDate.of(anio, mesSig, 1).lengthOfMonth());
        }
        return Optional.of(cierre);
    }

    /**
     * Devuelve las últimas {@code cantidad} fechas de publicación (la más reciente primero),
     * todas menores o iguales a {@code hoy}.
     */
    public static List<LocalDate> fechasPrevias(Frecuencia frecuencia, LocalDate hoy, int cantidad) {
        if (frecuencia == null || cantidad <= 0) {
            return List.of();
        }
        List<LocalDate> fechas = new ArrayList<>();
        switch (frecuencia.tipo()) {
            case SEMANAL -> {
                if (frecuencia.diaSemana() == null) {
                    break;
                }
                LocalDate d = hoy;
                while (d.getDayOfWeek() != frecuencia.diaSemana()) {
                    d = d.minusDays(1);
                }
                for (int i = 0; i < cantidad; i++) {
                    fechas.add(d);
                    d = d.minusDays(7);
                }
            }
            case MENSUAL -> {
                if (frecuencia.diaMes() == null) {
                    break;
                }
                int dia = Math.max(1, Math.min(frecuencia.diaMes(), 28));
                LocalDate d = hoy.withDayOfMonth(Math.min(dia, hoy.lengthOfMonth()));
                if (d.isAfter(hoy)) {
                    d = d.minusMonths(1);
                }
                for (int i = 0; i < cantidad; i++) {
                    fechas.add(d);
                    d = d.minusMonths(1);
                }
            }
            case TRIMESTRAL -> {
                LocalDate d = cierreTrimestre(hoy);
                if (d.isAfter(hoy)) {
                    d = d.minusMonths(3);
                }
                for (int i = 0; i < cantidad; i++) {
                    fechas.add(d);
                    d = d.minusMonths(3);
                }
            }
            case IRREGULAR -> {
                // sin fechas predecibles
            }
        }
        return fechas;
    }

    private static LocalDate cierreTrimestre(LocalDate fecha) {
        int mes = ((fecha.getMonthValue() - 1) / 3 + 1) * 3;
        return LocalDate.of(fecha.getYear(), mes, 1)
                .withDayOfMonth(LocalDate.of(fecha.getYear(), mes, 1).lengthOfMonth());
    }
}
