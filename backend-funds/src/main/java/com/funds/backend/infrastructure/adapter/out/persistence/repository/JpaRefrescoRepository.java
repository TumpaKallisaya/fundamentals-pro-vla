package com.funds.backend.infrastructure.adapter.out.persistence.repository;

import com.funds.backend.infrastructure.adapter.out.persistence.entity.RefrescoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data de {@link RefrescoEntity}. */
public interface JpaRefrescoRepository extends JpaRepository<RefrescoEntity, Long> {
}
