package com.funds.backend.infrastructure.adapter.out.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.TendenciaDiaria;
import com.funds.backend.domain.port.out.TendenciaDiariaRepository;
import com.funds.backend.infrastructure.adapter.out.persistence.mapper.TendenciaDiariaMapper;
import com.funds.backend.infrastructure.adapter.out.persistence.repository.JpaTendenciaDiariaRepository;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa {@link TendenciaDiariaRepository} sobre Spring Data JPA. */
@Repository
public class TendenciaDiariaRepositoryAdapter implements TendenciaDiariaRepository {

    private final JpaTendenciaDiariaRepository jpaRepository;

    public TendenciaDiariaRepositoryAdapter(JpaTendenciaDiariaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TendenciaDiaria save(TendenciaDiaria tendenciaDiaria) {
        return TendenciaDiariaMapper.toDomain(jpaRepository.save(TendenciaDiariaMapper.toEntity(tendenciaDiaria)));
    }

    @Override
    public Optional<TendenciaDiaria> findByIndiceCodigoAndFecha(String indiceCodigo, LocalDate fecha) {
        return jpaRepository.findByIndiceCodigoAndFecha(indiceCodigo, fecha)
                .map(TendenciaDiariaMapper::toDomain);
    }

    @Override
    public List<TendenciaDiaria> findByFechaBetween(LocalDate desde, LocalDate hasta) {
        return jpaRepository.findByFechaBetween(desde, hasta).stream()
                .map(TendenciaDiariaMapper::toDomain)
                .toList();
    }
}
