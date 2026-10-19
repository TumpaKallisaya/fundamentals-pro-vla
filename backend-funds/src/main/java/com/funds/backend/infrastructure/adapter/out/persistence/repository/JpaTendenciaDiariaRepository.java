package com.funds.backend.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.funds.backend.infrastructure.adapter.out.persistence.entity.TendenciaDiariaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data de {@link TendenciaDiariaEntity}. */
public interface JpaTendenciaDiariaRepository extends JpaRepository<TendenciaDiariaEntity, Long> {

    Optional<TendenciaDiariaEntity> findByIndiceCodigoAndFecha(String indiceCodigo, LocalDate fecha);
}
