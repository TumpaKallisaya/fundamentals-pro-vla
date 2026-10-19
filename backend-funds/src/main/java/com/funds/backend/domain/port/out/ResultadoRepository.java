package com.funds.backend.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.funds.backend.domain.model.Resultado;

/** Puerto de salida: persistencia de resultados observados. */
public interface ResultadoRepository {

    Resultado save(Resultado resultado);

    List<Resultado> findByEventoId(Long eventoId);

    Optional<Resultado> findLatestByEventoId(Long eventoId);
}
