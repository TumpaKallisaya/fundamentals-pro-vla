package com.funds.backend.infrastructure.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;

import com.funds.backend.domain.model.HealthStatus;
import com.funds.backend.domain.port.in.HealthUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Prueba unitaria del adaptador de entrada {@link HealthController}.
 *
 * <p>Usa Mockito para simular el puerto de entrada {@link HealthUseCase} y verificar
 * que el controlador delega correctamente y devuelve el estado sin añadir lógica.</p>
 */
@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @Mock
    private HealthUseCase healthUseCase;

    @InjectMocks
    private HealthController healthController;

    @Test
    void healthDelegatesToUseCaseAndReturnsItsResult() {
        ZonedDateTime now = ZonedDateTime.now();
        HealthStatus expected = new HealthStatus("backend-funds", "UP", now);
        when(healthUseCase.check()).thenReturn(expected);

        HealthStatus actual = healthController.health();

        assertThat(actual).isEqualTo(expected);
        verify(healthUseCase).check();
    }

    @Test
    void healthReturnsUpStatus() {
        when(healthUseCase.check())
                .thenReturn(new HealthStatus("backend-funds", "UP", ZonedDateTime.now()));

        HealthStatus actual = healthController.health();

        assertThat(actual.service()).isEqualTo("backend-funds");
        assertThat(actual.status()).isEqualTo("UP");
        assertThat(actual.timestamp()).isNotNull();
    }
}
