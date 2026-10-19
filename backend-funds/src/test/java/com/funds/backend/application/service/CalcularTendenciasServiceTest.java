package com.funds.backend.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.funds.backend.domain.model.ClasificacionTendencia;
import com.funds.backend.domain.model.Direccion;
import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.Impacto;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.IndicadorIndice;
import com.funds.backend.domain.model.Refresco;
import com.funds.backend.domain.model.Regla;
import com.funds.backend.domain.model.Relevancia;
import com.funds.backend.domain.model.Resultado;
import com.funds.backend.domain.model.TendenciaDiaria;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorIndiceRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.RefrescoRepository;
import com.funds.backend.domain.port.out.ResultadoRepository;
import com.funds.backend.domain.port.out.TendenciaDiariaRepository;
import com.funds.backend.domain.port.out.ValorActualPort;

/**
 * Prueba unitaria del caso de uso {@link CalcularTendenciasService}.
 */
@ExtendWith(MockitoExtension.class)
class CalcularTendenciasServiceTest {

    @Mock
    private ValorActualPort valorActualPort;
    @Mock
    private EventoRepository eventoRepository;
    @Mock
    private IndicadorRepository indicadorRepository;
    @Mock
    private IndicadorIndiceRepository indicadorIndiceRepository;
    @Mock
    private ResultadoRepository resultadoRepository;
    @Mock
    private TendenciaDiariaRepository tendenciaDiariaRepository;
    @Mock
    private RefrescoRepository refrescoRepository;

    @InjectMocks
    private CalcularTendenciasService service;

    @Test
    void calculaTendenciaDiariaBajistaParaCpiAlto() {
        LocalDate fecha = LocalDate.of(2026, 11, 6);
        Instant fechaUtc = fecha.atStartOfDay(ZoneId.of("America/New_York")).toInstant();

        Evento evento = new Evento(1L, "cpi", fechaUtc, new BigDecimal("0.2"), new BigDecimal("0.3"));
        when(eventoRepository.findByFechaHoraUtcBetween(any(), any())).thenReturn(List.of(evento));

        Indicador indicador = new Indicador("cpi", "CPI (m/m)", "CPI m/m", "indices", Impacto.ALTO,
                "Mensual", "08:30", null, new Regla(Direccion.MAYOR_ES_BAJISTA, 3, new BigDecimal("15")));
        when(indicadorRepository.findById("cpi")).thenReturn(Optional.of(indicador));

        when(valorActualPort.obtenerActual(eq("cpi"), any())).thenReturn(Optional.of(new BigDecimal("0.3")));

        when(indicadorIndiceRepository.findByIndicadorId("cpi")).thenReturn(List.of(
                new IndicadorIndice(1L, "cpi", "NAS100", Relevancia.ALTA),
                new IndicadorIndice(2L, "cpi", "SP500", Relevancia.ALTA)
        ));

        when(tendenciaDiariaRepository.findByIndiceCodigoAndFecha(any(), eq(fecha))).thenReturn(Optional.empty());

        when(refrescoRepository.save(any(Refresco.class))).thenAnswer(inv -> {
            Refresco r = inv.getArgument(0);
            return new Refresco(1L, r.iniciadoEnUtc(), r.finalizadoEnUtc(), r.estado(), r.detalle());
        });

        service.calcular(fecha);

        verify(resultadoRepository).save(any(Resultado.class));

        ArgumentCaptor<TendenciaDiaria> captor = ArgumentCaptor.forClass(TendenciaDiaria.class);
        verify(tendenciaDiariaRepository, times(2)).save(captor.capture());
        List<TendenciaDiaria> tendencias = captor.getAllValues();

        assertThat(tendencias).extracting(TendenciaDiaria::indiceCodigo)
                .containsExactlyInAnyOrder("NAS100", "SP500");
        // CPI actual 0.3 > forecast 0.2, dirección bajista → ROJO → -1 × peso 3 × relevancia 1 = -3 → BAJISTA
        assertThat(tendencias).allSatisfy(t -> {
            assertThat(t.puntaje()).isEqualByComparingTo("-3");
            assertThat(t.clasificacion()).isEqualTo(ClasificacionTendencia.BAJISTA);
        });
    }
}
