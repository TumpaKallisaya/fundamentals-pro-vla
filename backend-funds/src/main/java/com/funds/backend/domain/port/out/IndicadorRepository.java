package com.funds.backend.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Indicador;

/** Puerto de salida: persistencia de indicadores. */
public interface IndicadorRepository {

    Optional<Indicador> findById(String id);

    Indicador save(Indicador indicador);

    boolean existsById(String id);

    List<Indicador> findAll();

    long count();
}
