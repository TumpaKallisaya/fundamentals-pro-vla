package com.funds.backend.application.service.dto;

import java.time.Instant;

import com.funds.backend.domain.model.EstadoRefresco;

/** DTO de respuesta: estado de un refresco. */
public record RefrescoDto(
        Long id,
        EstadoRefresco estado,
        String detalle,
        Instant iniciadoEnUtc,
        Instant finalizadoEnUtc
) {
}
