package com.funds.backend.infrastructure.adapter.out.persistence.repository;

import com.funds.backend.infrastructure.adapter.out.persistence.entity.IndicadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data de {@link IndicadorEntity}. */
public interface JpaIndicadorRepository extends JpaRepository<IndicadorEntity, String> {
}
