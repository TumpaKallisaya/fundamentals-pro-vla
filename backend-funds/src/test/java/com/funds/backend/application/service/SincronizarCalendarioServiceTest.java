package com.funds.backend.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.EventoCalendario;
import com.funds.backend.domain.model.Impacto;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.port.out.CalendarioEconomicoPort;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;

/**
 * Prueba unitaria del caso de uso {@link SincronizarCalendarioService}.
 */
@ExtendWith(MockitoExtension.class)
class SincronizarCalendarioServiceTest {

    @Mock
    private CalendarioEconomicoPort calendarioPort;
    @Mock
    private IndicadorRepository indicadorRepository;
    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private SincronizarCalendarioService service;

    @Test
    void sincronizaYPersisteEventosDeIndicadoresCatalogados() {
        Instant fecha = Instant.parse("2026-11-02T15:00:00Z");
        EventoCalendario eventoCalendario = new EventoCalendario(
                "CPI m/m", "USD", fecha, Impacto.ALTO, new BigDecimal("0.2"), new BigDecimal("0.3"));
        when(calendarioPort.obtenerEventos()).thenReturn(List.of(eventoCalendario));

        Indicador indicador = new Indicador(
                "cpi", "CPI (m/m)", "CPI m/m", "indices", Impacto.ALTO, "Mensual", "08:30", null, null);
        when(indicadorRepository.findByAliasFf("CPI m/m")).thenReturn(Optional.of(indicador));
        when(eventoRepository.findByIndicadorIdAndFechaHoraUtc("cpi", fecha)).thenReturn(Optional.empty());

        int guardados = service.sincronizar();

        assertThat(guardados).isEqualTo(1);
        ArgumentCaptor<Evento> captor = ArgumentCaptor.forClass(Evento.class);
        verify(eventoRepository).save(captor.capture());
        assertThat(captor.getValue().indicadorId()).isEqualTo("cpi");
        assertThat(captor.getValue().forecast()).isEqualByComparingTo(new BigDecimal("0.2"));
    }

    @Test
    void descartaEventosDeIndicadoresNoCatalogados() {
        Instant fecha = Instant.parse("2026-11-02T15:00:00Z");
        EventoCalendario eventoCalendario = new EventoCalendario(
                "Algo Desconocido", "USD", fecha, Impacto.ALTO, null, null);
        when(calendarioPort.obtenerEventos()).thenReturn(List.of(eventoCalendario));
        when(indicadorRepository.findByAliasFf("Algo Desconocido")).thenReturn(Optional.empty());

        int guardados = service.sincronizar();

        assertThat(guardados).isZero();
        verify(eventoRepository, never()).save(any(Evento.class));
    }
}
