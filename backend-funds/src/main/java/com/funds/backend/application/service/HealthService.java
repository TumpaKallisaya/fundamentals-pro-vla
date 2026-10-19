package com.funds.backend.application.service;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.funds.backend.domain.model.HealthStatus;
import com.funds.backend.domain.port.in.HealthUseCase;

/**
 * Implementación del caso de uso de salud (capa de aplicación).
 *
 * <p>Orquesta la lógica del puerto de entrada {@link HealthUseCase}. En esta fase
 * devuelve un estado fijo; en fases posteriores los servicios de aplicación
 * coordinarán puertos de salida (repositorios, conectores) para sus casos de uso.</p>
 */
@Service
public class HealthService implements HealthUseCase {

    private static final String SERVICE_NAME = "backend-funds";

    @Override
    public HealthStatus check() {
        return new HealthStatus(SERVICE_NAME, "UP", ZonedDateTime.now());
    }
}
