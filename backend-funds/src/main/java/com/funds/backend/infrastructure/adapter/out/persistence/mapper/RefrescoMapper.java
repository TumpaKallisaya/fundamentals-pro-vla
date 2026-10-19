package com.funds.backend.infrastructure.adapter.out.persistence.mapper;

import com.funds.backend.domain.model.Refresco;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.RefrescoEntity;

/** Mapea entre la entidad de dominio {@link Refresco} y la entidad JPA {@link RefrescoEntity}. */
public final class RefrescoMapper {

    private RefrescoMapper() {
    }

    public static Refresco toDomain(RefrescoEntity entity) {
        return new Refresco(
                entity.getId(),
                entity.getIniciadoEnUtc(),
                entity.getFinalizadoEnUtc(),
                entity.getEstado(),
                entity.getDetalle()
        );
    }

    public static RefrescoEntity toEntity(Refresco domain) {
        return new RefrescoEntity(
                domain.id(),
                domain.iniciadoEnUtc(),
                domain.finalizadoEnUtc(),
                domain.estado(),
                domain.detalle()
        );
    }
}
