package com.funds.backend.infrastructure.adapter.out.persistence.mapper;

import com.funds.backend.domain.model.IndicadorIndice;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.IndicadorIndiceEntity;

/** Mapea entre la entidad de dominio {@link IndicadorIndice} y la entidad JPA {@link IndicadorIndiceEntity}. */
public final class IndicadorIndiceMapper {

    private IndicadorIndiceMapper() {
    }

    public static IndicadorIndice toDomain(IndicadorIndiceEntity entity) {
        return new IndicadorIndice(
                entity.getId(),
                entity.getIndicadorId(),
                entity.getIndiceCodigo(),
                entity.getRelevancia()
        );
    }

    public static IndicadorIndiceEntity toEntity(IndicadorIndice domain) {
        return new IndicadorIndiceEntity(
                domain.id(),
                domain.indicadorId(),
                domain.indiceCodigo(),
                domain.relevancia()
        );
    }
}
