package com.funds.backend.infrastructure.adapter.out.persistence.mapper;

import com.funds.backend.domain.model.TendenciaDiaria;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.TendenciaDiariaEntity;

/** Mapea entre la entidad de dominio {@link TendenciaDiaria} y la entidad JPA {@link TendenciaDiariaEntity}. */
public final class TendenciaDiariaMapper {

    private TendenciaDiariaMapper() {
    }

    public static TendenciaDiaria toDomain(TendenciaDiariaEntity entity) {
        return new TendenciaDiaria(
                entity.getId(),
                entity.getIndiceCodigo(),
                entity.getFecha(),
                entity.getPuntaje(),
                entity.getClasificacion()
        );
    }

    public static TendenciaDiariaEntity toEntity(TendenciaDiaria domain) {
        return new TendenciaDiariaEntity(
                domain.id(),
                domain.indiceCodigo(),
                domain.fecha(),
                domain.puntaje(),
                domain.clasificacion()
        );
    }
}
