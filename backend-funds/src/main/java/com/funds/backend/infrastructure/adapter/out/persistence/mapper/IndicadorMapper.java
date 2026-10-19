package com.funds.backend.infrastructure.adapter.out.persistence.mapper;

import com.funds.backend.domain.model.Fuente;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.Regla;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.FuenteEmbeddable;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.IndicadorEntity;
import com.funds.backend.infrastructure.adapter.out.persistence.entity.ReglaEmbeddable;

/** Mapea entre la entidad de dominio {@link Indicador} y la entidad JPA {@link IndicadorEntity}. */
public final class IndicadorMapper {

    private IndicadorMapper() {
    }

    public static Indicador toDomain(IndicadorEntity entity) {
        return new Indicador(
                entity.getId(),
                entity.getNombre(),
                entity.getAliasFf(),
                entity.getSector(),
                entity.getImpacto(),
                entity.getFrecuencia(),
                entity.getHoraEt(),
                toDomain(entity.getFuente()),
                toDomain(entity.getRegla())
        );
    }

    public static IndicadorEntity toEntity(Indicador domain) {
        return new IndicadorEntity(
                domain.id(),
                domain.nombre(),
                domain.aliasFf(),
                domain.sector(),
                domain.impacto(),
                domain.frecuencia(),
                domain.horaEt(),
                toEntity(domain.fuente()),
                toEntity(domain.regla())
        );
    }

    private static Fuente toDomain(FuenteEmbeddable fuente) {
        return new Fuente(fuente.getNombre(), fuente.getTipo(), fuente.getUrl(), fuente.getNotas());
    }

    private static Regla toDomain(ReglaEmbeddable regla) {
        return new Regla(regla.getDireccion(), regla.getPeso(), regla.getToleranciaPct());
    }

    private static FuenteEmbeddable toEntity(Fuente fuente) {
        return new FuenteEmbeddable(fuente.nombre(), fuente.tipo(), fuente.url(), fuente.notas());
    }

    private static ReglaEmbeddable toEntity(Regla regla) {
        return new ReglaEmbeddable(regla.direccion(), regla.peso(), regla.toleranciaPct());
    }
}
