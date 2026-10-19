package com.funds.backend.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.funds.backend.domain.model.HealthStatus;
import org.junit.jupiter.api.Test;

/**
 * Prueba unitaria del servicio de aplicación {@link HealthService}.
 */
class HealthServiceTest {

    private final HealthService healthService = new HealthService();

    @Test
    void checkReturnsServiceNameAndUpStatus() {
        HealthStatus status = healthService.check();

        assertThat(status.service()).isEqualTo("backend-funds");
        assertThat(status.status()).isEqualTo("UP");
        assertThat(status.timestamp()).isNotNull();
    }
}
