package com.funds.backend.infrastructure.adapter.out.persistence.repository;

import java.time.Instant;
import java.util.Optional;

import com.funds.backend.infrastructure.adapter.out.persistence.entity.EventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data de {@link EventoEntity}. */
public interface JpaEventoRepository extends JpaRepository<EventoEntity, Long> {

    Optional<EventoEntity> findByIndicadorIdAndFechaHoraUtc(String indicadorId, Instant fechaHoraUtc);
}
