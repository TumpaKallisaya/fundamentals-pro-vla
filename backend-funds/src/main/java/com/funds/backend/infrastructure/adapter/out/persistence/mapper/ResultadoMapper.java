package com.funds.backend.infrastructure.adapter.out.persistence.mapper;

import com.funds.backend.domain.model.Resultado;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.ResultadoEntity;

/** Mapea entre la entidad de dominio {@link Resultado} y la entidad JPA {@link ResultadoEntity}. */
public final class ResultadoMapper {

    private ResultadoMapper() {
    }

    public static Resultado toDomain(ResultadoEntity entity) {
        return new Resultado(
                entity.getId(),
                entity.getEventoId(),
                entity.getRefrescoId(),
                entity.getActual()
        );
    }

    public static ResultadoEntity toEntity(Resultado domain) {
        return new ResultadoEntity(
                domain.id(),
                domain.eventoId(),
                domain.refrescoId(),
                domain.actual()
        );
    }
}
