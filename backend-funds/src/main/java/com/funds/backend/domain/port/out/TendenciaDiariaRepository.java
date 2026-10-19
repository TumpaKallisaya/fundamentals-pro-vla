package com.funds.backend.domain.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.TendenciaDiaria;

/** Puerto de salida: persistencia de tendencias diarias. */
public interface TendenciaDiariaRepository {

    TendenciaDiaria save(TendenciaDiaria tendenciaDiaria);

    Optional<TendenciaDiaria> findByIndiceCodigoAndFecha(String indiceCodigo, LocalDate fecha);

    List<TendenciaDiaria> findByFechaBetween(LocalDate desde, LocalDate hasta);
}
