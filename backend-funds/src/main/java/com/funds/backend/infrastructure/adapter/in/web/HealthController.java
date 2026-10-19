package com.funds.backend.infrastructure.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.funds.backend.domain.model.HealthStatus;
import com.funds.backend.domain.port.in.HealthUseCase;

/**
 * Adaptador de entrada (driving adapter): expone por HTTP/REST el caso de uso de salud.
 *
 * <p>Única responsabilidad: traducir la petición HTTP al puerto de entrada
 * {@link HealthUseCase} y devolver la respuesta serializada. No contiene lógica de negocio.</p>
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthUseCase healthUseCase;

    public HealthController(HealthUseCase healthUseCase) {
        this.healthUseCase = healthUseCase;
    }

    @GetMapping("/health")
    public HealthStatus health() {
        return healthUseCase.check();
    }
}
