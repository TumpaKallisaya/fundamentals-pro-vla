package com.funds.backend.infrastructure.adapter.out.persistence.adapter;

import java.time.Instant;
import java.util.Optional;

import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.infrastructure.adapter.out.persistence.mapper.EventoMapper;
import com.funds.backend.infrastructure.adapter.out.persistence.repository.JpaEventoRepository;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa {@link EventoRepository} sobre Spring Data JPA. */
@Repository
public class EventoRepositoryAdapter implements EventoRepository {

    private final JpaEventoRepository jpaRepository;

    public EventoRepositoryAdapter(JpaEventoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Evento save(Evento evento) {
        return EventoMapper.toDomain(jpaRepository.save(EventoMapper.toEntity(evento)));
    }

    @Override
    public Optional<Evento> findByIndicadorIdAndFechaHoraUtc(String indicadorId, Instant fechaHoraUtc) {
        return jpaRepository.findByIndicadorIdAndFechaHoraUtc(indicadorId, fechaHoraUtc)
                .map(EventoMapper::toDomain);
    }
}
