package com.funds.backend.application.service;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de lectura del seed JSON (Fase 1). Los valores textuales (`impacto`, `relevancia`,
 * `direccion`) se mapean a enums de dominio en el {@link CatalogoSeedService}.
 */
public record SeedIndicador(
        @JsonProperty("id") String id,
        @JsonProperty("nombre") String nombre,
        @JsonProperty("alias_ff") String aliasFf,
        @JsonProperty("sector") String sector,
        @JsonProperty("impacto") String impacto,
        @JsonProperty("frecuencia") String frecuencia,
        @JsonProperty("hora_et") String horaEt,
        @JsonProperty("indices") List<SeedIndice> indices,
        @JsonProperty("fuente") SeedFuente fuente,
        @JsonProperty("regla") SeedRegla regla
) {

    public record SeedIndice(
            @JsonProperty("indice") String indice,
            @JsonProperty("relevancia") String relevancia
    ) {
    }

    public record SeedFuente(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("tipo") String tipo,
            @JsonProperty("url") String url,
            @JsonProperty("notas") String notas
    ) {
    }

    public record SeedRegla(
            @JsonProperty("direccion") String direccion,
            @JsonProperty("peso") int peso,
            @JsonProperty("tolerancia_pct") BigDecimal toleranciaPct
    ) {
    }
}
