package com.funds.backend.infrastructure.adapter.out.persistence.repository;

import java.util.List;

import com.funds.backend.infrastructure.adapter.out.persistence.entity.IndicadorIndiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data de {@link IndicadorIndiceEntity}. */
public interface JpaIndicadorIndiceRepository extends JpaRepository<IndicadorIndiceEntity, Long> {

    boolean existsByIndicadorIdAndIndiceCodigo(String indicadorId, String indiceCodigo);

    List<IndicadorIndiceEntity> findByIndicadorId(String indicadorId);
}
