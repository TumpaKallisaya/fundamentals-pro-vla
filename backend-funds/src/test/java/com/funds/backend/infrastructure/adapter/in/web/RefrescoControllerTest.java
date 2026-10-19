package com.funds.backend.infrastructure.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.funds.backend.application.service.RefrescoService;
import com.funds.backend.application.service.dto.RefrescoDto;
import com.funds.backend.domain.model.EstadoRefresco;

/**
 * Prueba unitaria del controlador {@link RefrescoController}.
 */
@ExtendWith(MockitoExtension.class)
class RefrescoControllerTest {

    @Mock
    private RefrescoService refrescoService;

    @InjectMocks
    private RefrescoController controller;

    @Test
    void refrescarDelegaEnElServicio() {
        RefrescoDto dto = new RefrescoDto(1L, EstadoRefresco.COMPLETADO, "ok",
                Instant.now(), Instant.now());
        when(refrescoService.refrescar()).thenReturn(dto);

        assertThat(controller.refrescar()).isEqualTo(dto);
    }

    @Test
    void ultimoDevuelve200CuandoExiste() {
        RefrescoDto dto = new RefrescoDto(1L, EstadoRefresco.COMPLETADO, "ok",
                Instant.now(), Instant.now());
        when(refrescoService.ultimo()).thenReturn(Optional.of(dto));

        ResponseEntity<RefrescoDto> response = controller.ultimo();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void ultimoDevuelve404CuandoNoExiste() {
        when(refrescoService.ultimo()).thenReturn(Optional.empty());

        ResponseEntity<RefrescoDto> response = controller.ultimo();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
