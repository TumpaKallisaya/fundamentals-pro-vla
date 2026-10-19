package com.funds.backend.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;
import com.funds.backend.domain.model.Direccion;
import com.funds.backend.domain.model.Fuente;
import com.funds.backend.domain.model.Impacto;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.IndicadorIndice;
import com.funds.backend.domain.model.Indice;
import com.funds.backend.domain.model.Regla;
import com.funds.backend.domain.model.Relevancia;
import com.funds.backend.domain.port.out.IndicadorIndiceRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.IndiceRepository;

/**
 * Caso de uso: siembra el catálogo de indicadores (definido en la Fase 1) en la base de datos.
 *
 * <p>Lee el seed JSON desde el classpath y persiste índices, indicadores y sus relaciones de
 * forma idempotente (no duplica datos si se re-ejecuta).</p>
 */
@Service
public class CatalogoSeedService {

    private static final String SEED_RESOURCE = "db/seed/indicadores.json";

    private static final Map<String, String> INDICE_NOMBRES = Map.of(
            "NAS100", "Nasdaq 100",
            "SP500", "S&P 500"
    );

    private final IndiceRepository indiceRepository;
    private final IndicadorRepository indicadorRepository;
    private final IndicadorIndiceRepository indicadorIndiceRepository;
    private final ObjectMapper objectMapper;

    public CatalogoSeedService(IndiceRepository indiceRepository,
                               IndicadorRepository indicadorRepository,
                               IndicadorIndiceRepository indicadorIndiceRepository,
                               ObjectMapper objectMapper) {
        this.indiceRepository = indiceRepository;
        this.indicadorRepository = indicadorRepository;
        this.indicadorIndiceRepository = indicadorIndiceRepository;
        this.objectMapper = objectMapper;
    }

    /** Lee el seed desde el classpath y siembra de forma idempotente. */
    public void seed() {
        try (InputStream in = new ClassPathResource(SEED_RESOURCE).getInputStream()) {
            SeedIndicador[] array = objectMapper.readValue(in, SeedIndicador[].class);
            seed(List.of(array));
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el seed del catálogo: " + SEED_RESOURCE, e);
        }
    }

    /** Siembra una lista de indicadores (idempotente). */
    void seed(List<SeedIndicador> items) {
        for (SeedIndicador item : items) {
            seedIndices(item);
            seedIndicador(item);
            seedRelaciones(item);
        }
    }

    private void seedIndices(SeedIndicador item) {
        for (SeedIndicador.SeedIndice si : item.indices()) {
            if (!indiceRepository.existsByCodigo(si.indice())) {
                indiceRepository.save(new Indice(si.indice(), nombreIndice(si.indice()), item.sector()));
            }
        }
    }

    private void seedIndicador(SeedIndicador item) {
        if (indicadorRepository.existsById(item.id())) {
            return;
        }
        Indicador indicador = new Indicador(
                item.id(),
                item.nombre(),
                item.aliasFf(),
                item.sector(),
                mapImpacto(item.impacto()),
                item.frecuencia(),
                item.horaEt(),
                new Fuente(item.fuente().nombre(), item.fuente().tipo(), item.fuente().url(), item.fuente().notas()),
                new Regla(mapDireccion(item.regla().direccion()), item.regla().peso(), item.regla().toleranciaPct())
        );
        indicadorRepository.save(indicador);
    }

    private void seedRelaciones(SeedIndicador item) {
        for (SeedIndicador.SeedIndice si : item.indices()) {
            if (indicadorIndiceRepository.existsByIndicadorIdAndIndiceCodigo(item.id(), si.indice())) {
                continue;
            }
            indicadorIndiceRepository.save(
                    new IndicadorIndice(null, item.id(), si.indice(), mapRelevancia(si.relevancia()))
            );
        }
    }

    private String nombreIndice(String codigo) {
        return INDICE_NOMBRES.getOrDefault(codigo, codigo);
    }

    private Impacto mapImpacto(String valor) {
        return switch (valor) {
            case "Alto" -> Impacto.ALTO;
            case "Medio" -> Impacto.MEDIO;
            default -> throw new IllegalArgumentException("Impacto desconocido: " + valor);
        };
    }

    private Relevancia mapRelevancia(String valor) {
        return switch (valor) {
            case "alta" -> Relevancia.ALTA;
            case "media" -> Relevancia.MEDIA;
            case "baja" -> Relevancia.BAJA;
            default -> throw new IllegalArgumentException("Relevancia desconocida: " + valor);
        };
    }

    private Direccion mapDireccion(String valor) {
        return switch (valor) {
            case "mayor_es_alcista" -> Direccion.MAYOR_ES_ALCISTA;
            case "mayor_es_bajista" -> Direccion.MAYOR_ES_BAJISTA;
            default -> throw new IllegalArgumentException("Direccion desconocida: " + valor);
        };
    }
}
