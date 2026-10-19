package com.funds.backend.infrastructure.adapter.out.persistence.mapper;

import com.funds.backend.domain.model.Evento;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.EventoEntity;

/** Mapea entre la entidad de dominio {@link Evento} y la entidad JPA {@link EventoEntity}. */
public final class EventoMapper {

    private EventoMapper() {
    }

    public static Evento toDomain(EventoEntity entity) {
        return new Evento(
                entity.getId(),
                entity.getIndicadorId(),
                entity.getFechaHoraUtc(),
                entity.getForecast(),
                entity.getPrevious()
        );
    }

    public static EventoEntity toEntity(Evento domain) {
        return new EventoEntity(
                domain.id(),
                domain.indicadorId(),
                domain.fechaHoraUtc(),
                domain.forecast(),
                domain.previous()
        );
    }
}
