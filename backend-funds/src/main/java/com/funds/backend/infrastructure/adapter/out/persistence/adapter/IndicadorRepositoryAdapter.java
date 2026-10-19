package com.funds.backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.infrastructure.adapter.out.persistence.mapper.IndicadorMapper;
import com.funds.backend.infrastructure.adapter.out.persistence.repository.JpaIndicadorRepository;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa {@link IndicadorRepository} sobre Spring Data JPA. */
@Repository
public class IndicadorRepositoryAdapter implements IndicadorRepository {

    private final JpaIndicadorRepository jpaRepository;

    public IndicadorRepositoryAdapter(JpaIndicadorRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Indicador> findById(String id) {
        return jpaRepository.findById(id).map(IndicadorMapper::toDomain);
    }

    @Override
    public Optional<Indicador> findByAliasFf(String aliasFf) {
        return jpaRepository.findByAliasFf(aliasFf).map(IndicadorMapper::toDomain);
    }

    @Override
    public Indicador save(Indicador indicador) {
        return IndicadorMapper.toDomain(jpaRepository.save(IndicadorMapper.toEntity(indicador)));
    }

    @Override
    public boolean existsById(String id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Indicador> findAll() {
        return jpaRepository.findAll().stream().map(IndicadorMapper::toDomain).toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
