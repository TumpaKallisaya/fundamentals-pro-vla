package com.funds.backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;

import com.funds.backend.domain.model.IndicadorIndice;
import com.funds.backend.domain.port.out.IndicadorIndiceRepository;
import com.funds.backend.infrastructure.adapter.out.persistence.mapper.IndicadorIndiceMapper;
import com.funds.backend.infrastructure.adapter.out.persistence.repository.JpaIndicadorIndiceRepository;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa {@link IndicadorIndiceRepository} sobre Spring Data JPA. */
@Repository
public class IndicadorIndiceRepositoryAdapter implements IndicadorIndiceRepository {

    private final JpaIndicadorIndiceRepository jpaRepository;

    public IndicadorIndiceRepositoryAdapter(JpaIndicadorIndiceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public IndicadorIndice save(IndicadorIndice indicadorIndice) {
        return IndicadorIndiceMapper.toDomain(jpaRepository.save(IndicadorIndiceMapper.toEntity(indicadorIndice)));
    }

    @Override
    public boolean existsByIndicadorIdAndIndiceCodigo(String indicadorId, String indiceCodigo) {
        return jpaRepository.existsByIndicadorIdAndIndiceCodigo(indicadorId, indiceCodigo);
    }

    @Override
    public List<IndicadorIndice> findByIndicadorId(String indicadorId) {
        return jpaRepository.findByIndicadorId(indicadorId).stream()
                .map(IndicadorIndiceMapper::toDomain)
                .toList();
    }
}
