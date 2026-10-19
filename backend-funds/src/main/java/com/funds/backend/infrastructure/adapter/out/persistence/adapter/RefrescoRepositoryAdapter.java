package com.funds.backend.infrastructure.adapter.out.persistence.adapter;

import java.util.Optional;

import com.funds.backend.domain.model.Refresco;
import com.funds.backend.domain.port.out.RefrescoRepository;
import com.funds.backend.infrastructure.adapter.out.persistence.mapper.RefrescoMapper;
import com.funds.backend.infrastructure.adapter.out.persistence.repository.JpaRefrescoRepository;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa {@link RefrescoRepository} sobre Spring Data JPA. */
@Repository
public class RefrescoRepositoryAdapter implements RefrescoRepository {

    private final JpaRefrescoRepository jpaRepository;

    public RefrescoRepositoryAdapter(JpaRefrescoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Refresco save(Refresco refresco) {
        return RefrescoMapper.toDomain(jpaRepository.save(RefrescoMapper.toEntity(refresco)));
    }

    @Override
    public Optional<Refresco> findById(Long id) {
        return jpaRepository.findById(id).map(RefrescoMapper::toDomain);
    }
}
