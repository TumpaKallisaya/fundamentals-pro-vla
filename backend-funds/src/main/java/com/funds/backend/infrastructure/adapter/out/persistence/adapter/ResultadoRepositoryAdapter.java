package com.funds.backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Resultado;
import com.funds.backend.domain.port.out.ResultadoRepository;
import com.funds.backend.infrastructure.adapter.out.persistence.mapper.ResultadoMapper;
import com.funds.backend.infrastructure.adapter.out.persistence.repository.JpaResultadoRepository;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa {@link ResultadoRepository} sobre Spring Data JPA. */
@Repository
public class ResultadoRepositoryAdapter implements ResultadoRepository {

    private final JpaResultadoRepository jpaRepository;

    public ResultadoRepositoryAdapter(JpaResultadoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Resultado save(Resultado resultado) {
        return ResultadoMapper.toDomain(jpaRepository.save(ResultadoMapper.toEntity(resultado)));
    }

    @Override
    public List<Resultado> findByEventoId(Long eventoId) {
        return jpaRepository.findByEventoId(eventoId).stream()
                .map(ResultadoMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Resultado> findLatestByEventoId(Long eventoId) {
        return jpaRepository.findTopByEventoIdOrderByIdDesc(eventoId)
                .map(ResultadoMapper::toDomain);
    }
}
