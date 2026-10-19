package com.funds.backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Indice;
import com.funds.backend.domain.port.out.IndiceRepository;
import com.funds.backend.infrastructure.adapter.out.persistence.mapper.IndiceMapper;
import com.funds.backend.infrastructure.adapter.out.persistence.repository.JpaIndiceRepository;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa {@link IndiceRepository} sobre Spring Data JPA. */
@Repository
public class IndiceRepositoryAdapter implements IndiceRepository {

    private final JpaIndiceRepository jpaRepository;

    public IndiceRepositoryAdapter(JpaIndiceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Indice> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(IndiceMapper::toDomain);
    }

    @Override
    public Indice save(Indice indice) {
        return IndiceMapper.toDomain(jpaRepository.save(IndiceMapper.toEntity(indice)));
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }

    @Override
    public List<Indice> findAll() {
        return jpaRepository.findAll().stream().map(IndiceMapper::toDomain).toList();
    }
}
