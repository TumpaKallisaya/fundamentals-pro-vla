package com.funds.backend.domain.port.out;

import java.util.Optional;

import com.funds.backend.domain.model.Refresco;

/** Puerto de salida: persistencia de refrescos. */
public interface RefrescoRepository {

    Refresco save(Refresco refresco);

    Optional<Refresco> findById(Long id);

    Optional<Refresco> findLatest();
}
