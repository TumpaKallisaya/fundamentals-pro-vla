package com.funds.backend.infrastructure.adapter.out.persistence.repository;

import java.util.Optional;

import com.funds.backend.infrastructure.adapter.out.persistence.entity.IndiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data de {@link IndiceEntity}. */
public interface JpaIndiceRepository extends JpaRepository<IndiceEntity, String> {

    Optional<IndiceEntity> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
