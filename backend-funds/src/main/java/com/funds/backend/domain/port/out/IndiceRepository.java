package com.funds.backend.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Indice;

/** Puerto de salida: persistencia de índices. */
public interface IndiceRepository {

    Optional<Indice> findByCodigo(String codigo);

    Indice save(Indice indice);

    boolean existsByCodigo(String codigo);

    List<Indice> findAll();
}
