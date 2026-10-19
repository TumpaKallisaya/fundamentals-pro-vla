package com.funds.backend.infrastructure.adapter.out.persistence.repository;

import java.util.List;

import com.funds.backend.infrastructure.adapter.out.persistence.entity.ResultadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data de {@link ResultadoEntity}. */
public interface JpaResultadoRepository extends JpaRepository<ResultadoEntity, Long> {

    List<ResultadoEntity> findByEventoId(Long eventoId);
}
