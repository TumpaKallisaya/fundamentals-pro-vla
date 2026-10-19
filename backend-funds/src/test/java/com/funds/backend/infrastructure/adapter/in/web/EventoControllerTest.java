package com.funds.backend.infrastructure.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.funds.backend.application.service.ConsultarEventosService;
import com.funds.backend.application.service.dto.EventoDto;

/**
 * Prueba unitaria del controlador {@link EventoController}.
 */
@ExtendWith(MockitoExtension.class)
class EventoControllerTest {

    @Mock
    private ConsultarEventosService consultarEventosService;

    @InjectMocks
    private EventoController controller;

    @Test
    void consultarDelegaConRangoEIndice() {
        LocalDate desde = LocalDate.of(2026, 11, 1);
        LocalDate hasta = LocalDate.of(2026, 11, 30);
        when(consultarEventosService.consultar(eq(desde), eq(hasta), eq("NAS100"))).thenReturn(List.of());

        List<EventoDto> resultado = controller.consultar(desde, hasta, "NAS100");

        assertThat(resultado).isEmpty();
        verify(consultarEventosService).consultar(desde, hasta, "NAS100");
    }
}
