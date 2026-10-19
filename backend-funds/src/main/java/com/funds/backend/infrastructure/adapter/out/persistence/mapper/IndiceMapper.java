package com.funds.backend.infrastructure.adapter.out.persistence.mapper;

import com.funds.backend.domain.model.Indice;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.IndiceEntity;

/** Mapea entre la entidad de dominio {@link Indice} y la entidad JPA {@link IndiceEntity}. */
public final class IndiceMapper {

    private IndiceMapper() {
    }

    public static Indice toDomain(IndiceEntity entity) {
        return new Indice(entity.getCodigo(), entity.getNombre(), entity.getSector());
    }

    public static IndiceEntity toEntity(Indice domain) {
        return new IndiceEntity(domain.codigo(), domain.nombre(), domain.sector());
    }
}
