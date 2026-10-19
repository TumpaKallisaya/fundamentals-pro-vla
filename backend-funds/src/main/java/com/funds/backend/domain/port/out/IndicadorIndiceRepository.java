package com.funds.backend.domain.port.out;

import java.util.List;

import com.funds.backend.domain.model.IndicadorIndice;

/** Puerto de salida: persistencia de la relación indicador ↔ índice. */
public interface IndicadorIndiceRepository {

    IndicadorIndice save(IndicadorIndice indicadorIndice);

    boolean existsByIndicadorIdAndIndiceCodigo(String indicadorId, String indiceCodigo);

    List<IndicadorIndice> findByIndicadorId(String indicadorId);
}
