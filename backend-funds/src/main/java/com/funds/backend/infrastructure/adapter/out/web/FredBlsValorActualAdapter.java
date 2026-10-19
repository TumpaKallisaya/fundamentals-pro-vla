package com.funds.backend.infrastructure.adapter.out.web;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import com.funds.backend.domain.port.out.ValorActualPort;

/**
 * Adaptador de salida: obtiene el valor "actual" de los indicadores desde la API de FRED
 * (St. Louis Fed), usando el catálogo de series {@code db/fred/series.json}.
 */
@Component
public class FredBlsValorActualAdapter implements ValorActualPort {

    private static final Logger log = LoggerFactory.getLogger(FredBlsValorActualAdapter.class);

    private static final String FRED_URL = "https://api.stlouisfed.org/fred/series/observations";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final Map<String, FredSerie> series;

    public FredBlsValorActualAdapter(RestClient restClient,
                                     ObjectMapper objectMapper,
                                     @Value("${app.fuentes.fred.api-key:}") String apiKey) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.series = cargarSeries();
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("FRED apiKey no configurada; no se resolverá el valor 'actual'.");
        }
    }

    @Override
    public Optional<BigDecimal> obtenerActual(String indicadorId, Instant fechaHoraUtc) {
        FredSerie serie = series.get(indicadorId);
        if (serie == null || apiKey == null || apiKey.isBlank()) {
            return Optional.empty();
        }
        try {
            String body = restClient.get()
                    .uri(FRED_URL + "?series_id=" + serie.serieId()
                            + "&api_key=" + apiKey
                            + "&file_type=json&sort_order=desc&limit=2")
                    .retrieve()
                    .body(String.class);
            List<BigDecimal> valores = parsearObservaciones(body);
            if (valores.size() < 2) {
                return Optional.empty();
            }
            return Optional.of(aplicarTransformacion(serie, valores));
        } catch (Exception e) {
            log.warn("No se pudo obtener el actual de FRED para {}: {}", indicadorId, e.getMessage());
            return Optional.empty();
        }
    }

    /** Parsea las observaciones de FRED (orden descendente) a valores numéricos. */
    List<BigDecimal> parsearObservaciones(String json) throws IOException {
        JsonNode root = objectMapper.readTree(json);
        JsonNode observations = root.get("observations");
        List<BigDecimal> valores = new ArrayList<>();
        if (observations != null) {
            for (JsonNode obs : observations) {
                String value = obs.get("value").asText();
                if (value == null || value.isBlank() || ".".equals(value)) {
                    continue;
                }
                valores.add(new BigDecimal(value));
            }
        }
        return valores;
    }

    /** Aplica la transformación configurada (DIRECTA/CAMBIO/PCT_CAMBIO) y la escala. */
    BigDecimal aplicarTransformacion(FredSerie serie, List<BigDecimal> valores) {
        BigDecimal actual = valores.get(0);
        BigDecimal anterior = valores.get(1);
        BigDecimal resultado = switch (serie.transformacion()) {
            case "CAMBIO" -> actual.subtract(anterior);
            case "PCT_CAMBIO" -> actual.subtract(anterior)
                    .divide(anterior, 10, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            default -> actual; // DIRECTA
        };
        BigDecimal escala = serie.escala() == null ? BigDecimal.ONE : serie.escala();
        return resultado.multiply(escala);
    }

    private Map<String, FredSerie> cargarSeries() {
        try (InputStream in = new ClassPathResource("db/fred/series.json").getInputStream()) {
            FredSerie[] arr = objectMapper.readValue(in, FredSerie[].class);
            Map<String, FredSerie> map = new HashMap<>();
            for (FredSerie s : arr) {
                map.put(s.indicadorId(), s);
            }
            return Map.copyOf(map);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer db/fred/series.json", e);
        }
    }
}
