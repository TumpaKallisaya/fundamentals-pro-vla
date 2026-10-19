package com.funds.backend.domain.port.out;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Evento;

/** Puerto de salida: persistencia de eventos de calendario. */
public interface EventoRepository {

    Evento save(Evento evento);

    Optional<Evento> findByIndicadorIdAndFechaHoraUtc(String indicadorId, Instant fechaHoraUtc);

    List<Evento> findByFechaHoraUtcBetween(Instant desde, Instant hasta);
}
