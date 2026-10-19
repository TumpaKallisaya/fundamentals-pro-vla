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

import com.funds.backend.application.service.ConsultarTendenciasService;
import com.funds.backend.application.service.dto.TendenciaDto;

/**
 * Prueba unitaria del controlador {@link TendenciaController}.
 */
@ExtendWith(MockitoExtension.class)
class TendenciaControllerTest {

    @Mock
    private ConsultarTendenciasService consultarTendenciasService;

    @InjectMocks
    private TendenciaController controller;

    @Test
    void consultarDelegaConIndice() {
        when(consultarTendenciasService.consultar(any(), any(), eq("NAS100"))).thenReturn(List.of());

        List<TendenciaDto> resultado = controller.consultar(
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), "NAS100");

        assertThat(resultado).isEmpty();
        verify(consultarTendenciasService).consultar(
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), "NAS100");
    }
}
