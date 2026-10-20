package com.funds.backend.application.service.dto;

import java.time.LocalDate;

import com.funds.backend.domain.model.Impacto;

/** DTO de respuesta: indicador con su impacto y su próxima fecha de publicación. */
public record IndicadorDto(
        String id,
        String nombre,
        Impacto impacto,
        String aliasFf,
        LocalDate siguienteFecha
) {
}
