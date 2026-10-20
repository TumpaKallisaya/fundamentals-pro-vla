package com.funds.backend.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.Impacto;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.Resultado;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.ResultadoRepository;
import com.funds.backend.domain.port.out.ValorActualPort;

/**
 * Prueba unitaria del caso de uso {@link BackfillHistoricoService}.
 */
@ExtendWith(MockitoExtension.class)
class BackfillHistoricoServiceTest {

    @Mock
    private ValorActualPort valorActualPort;
    @Mock
    private IndicadorRepository indicadorRepository;
    @Mock
    private EventoRepository eventoRepository;
    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private BackfillHistoricoService service;

    @Test
    void backfillPersisteResultadosHistoricos() {
        LocalDate hoy = LocalDate.of(2026, 10, 20);
        Indicador indicador = new Indicador(
                "jobless_claims", "Jobless Claims", "Unemployment Claims", "indices",
                Impacto.MEDIO, "Semanal (jueves)", "08:30", null, null);
        when(indicadorRepository.findAll()).thenReturn(List.of(indicador));

        when(valorActualPort.obtenerUltimos(eq("jobless_claims"), anyInt()))
                .thenReturn(List.of(new BigDecimal("199000"), new BigDecimal("198000")));
        when(eventoRepository.findByIndicadorIdAndFechaHoraUtc(any(), any()))
                .thenReturn(Optional.empty());
        when(eventoRepository.save(any(Evento.class))).thenAnswer(inv -> {
            Evento e = inv.getArgument(0);
            return new Evento(100L, e.indicadorId(), e.fechaHoraUtc(), e.forecast(), e.previous());
        });

        int persistidos = service.backfill(1L, hoy);

        assertThat(persistidos).isGreaterThanOrEqualTo(2);
        verify(resultadoRepository, atLeast(2)).save(any(Resultado.class));
    }

    @Test
    void backfillIgnoraIndicadorIrregular() {
        LocalDate hoy = LocalDate.of(2026, 10, 20);
        Indicador fomc = new Indicador(
                "fomc_tasas", "FOMC", "FOMC Statement", "indices",
                Impacto.ALTO, "8 veces al año", "14:00", null, null);
        when(indicadorRepository.findAll()).thenReturn(List.of(fomc));

        int persistidos = service.backfill(1L, hoy);

        assertThat(persistidos).isZero();
        verify(resultadoRepository, org.mockito.Mockito.never()).save(any(Resultado.class));
    }
}
