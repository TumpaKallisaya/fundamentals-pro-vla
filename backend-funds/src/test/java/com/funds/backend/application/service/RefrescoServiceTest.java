package com.funds.backend.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.funds.backend.application.service.dto.RefrescoDto;
import com.funds.backend.domain.model.EstadoRefresco;
import com.funds.backend.domain.model.Refresco;
import com.funds.backend.domain.port.out.RefrescoRepository;

/**
 * Prueba unitaria del caso de uso {@link RefrescoService}.
 */
@ExtendWith(MockitoExtension.class)
class RefrescoServiceTest {

    @Mock
    private RefrescoRepository refrescoRepository;
    @Mock
    private SincronizarCalendarioService sincronizarCalendarioService;
    @Mock
    private CalcularTendenciasService calcularTendenciasService;

    @InjectMocks
    private RefrescoService service;

    @Test
    void refrescarOrquestaElFlujoCompleto() {
        when(sincronizarCalendarioService.sincronizar()).thenReturn(5);
        when(refrescoRepository.save(any(Refresco.class))).thenAnswer(inv -> {
            Refresco r = inv.getArgument(0);
            return new Refresco(1L, r.iniciadoEnUtc(), r.finalizadoEnUtc(), r.estado(), r.detalle());
        });

        RefrescoDto dto = service.refrescar();

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.estado()).isEqualTo(EstadoRefresco.COMPLETADO);
        verify(calcularTendenciasService).calcular(eq(1L), any(LocalDate.class));
        verify(refrescoRepository, times(2)).save(any(Refresco.class));
    }

    @Test
    void refrescarDevuelveErrorSiFalla() {
        when(refrescoRepository.save(any(Refresco.class))).thenAnswer(inv -> {
            Refresco r = inv.getArgument(0);
            return new Refresco(1L, r.iniciadoEnUtc(), r.finalizadoEnUtc(), r.estado(), r.detalle());
        });
        when(sincronizarCalendarioService.sincronizar()).thenThrow(new IllegalStateException("429"));

        RefrescoDto dto = service.refrescar();

        assertThat(dto.estado()).isEqualTo(EstadoRefresco.ERROR);
        assertThat(dto.detalle()).isEqualTo("429");
    }
}
