package com.funds.backend.domain.port.in;

import com.funds.backend.domain.model.HealthStatus;

/**
 * Puerto de entrada (driving port): caso de uso de consulta de salud del servicio.
 *
 * <p>Define el contrato que el dominio ofrece a los adaptadores de entrada
 * (p. ej. controladores REST). Su implementación vive en la capa de aplicación.</p>
 */
public interface HealthUseCase {

    /**
     * Devuelve el estado de salud actual del servicio.
     *
     * @return estado de salud con nombre, estado e instante de consulta
     */
    HealthStatus check();
}
