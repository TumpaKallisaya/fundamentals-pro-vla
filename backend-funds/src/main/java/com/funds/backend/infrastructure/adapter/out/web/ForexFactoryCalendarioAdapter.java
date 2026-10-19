package com.funds.backend.infrastructure.adapter.out.web;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.ObjectMapper;

import com.funds.backend.domain.model.EventoCalendario;
import com.funds.backend.domain.model.Impacto;
import com.funds.backend.domain.port.out.CalendarioEconomicoPort;

/**
 * Adaptador de salida: consulta el calendario económico de Forex Factory (feed JSON público),
 * lo normaliza al modelo de dominio y aplica caché con TTL y reintentos con backoff.
 */
@Component
public class ForexFactoryCalendarioAdapter implements CalendarioEconomicoPort {

    private static final Logger log = LoggerFactory.getLogger(ForexFactoryCalendarioAdapter.class);

    private static final ZoneId ET = ZoneId.of("America/New_York");
    private static final int MAX_INTENTOS = 3;
    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String url;

    private volatile List<EventoCalendario> cached;
    private volatile Instant cachedAt;

    public ForexFactoryCalendarioAdapter(
            RestClient restClient,
            ObjectMapper objectMapper,
            @Value("${app.fuentes.forexfactory.url:https://nfs.faireconomy.media/ff_calendar_thisweek.json}") String url) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.url = url;
    }

    @Override
    public List<EventoCalendario> obtenerEventos() {
        if (cached != null && cachedAt != null && Instant.now().isBefore(cachedAt.plus(CACHE_TTL))) {
            return cached;
        }
        List<EventoCalendario> eventos = fetchConReintentos();
        cached = eventos;
        cachedAt = Instant.now();
        return eventos;
    }

    /** Parsea y normaliza el JSON del feed (expuesto para pruebas). */
    List<EventoCalendario> parsearJson(String json) throws Exception {
        FfEvent[] eventos = objectMapper.readValue(json, FfEvent[].class);
        return eventos == null ? List.of() : normalizar(eventos);
    }

    private List<EventoCalendario> fetchConReintentos() {
        Exception ultimo = null;
        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            try {
                String body = restClient.get().uri(url).retrieve().body(String.class);
                return parsearJson(body);
            } catch (Exception e) {
                ultimo = e;
                if (intento < MAX_INTENTOS) {
                    long backoffMs = 1000L * (1L << (intento - 1));
                    log.warn("Error al consultar Forex Factory (intento {}). Reintentando en {} ms: {}",
                            intento, backoffMs, e.getMessage());
                    dormir(backoffMs);
                }
            }
        }
        throw new IllegalStateException(
                "No se pudo obtener el calendario de Forex Factory tras " + MAX_INTENTOS + " intentos", ultimo);
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<EventoCalendario> normalizar(FfEvent[] eventos) {
        return Arrays.stream(eventos)
                .map(this::mapear)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<EventoCalendario> mapear(FfEvent evento) {
        if (!"USD".equals(evento.country())) {
            return Optional.empty();
        }
        Impacto impacto = mapearImpacto(evento.impact());
        if (impacto == null) {
            return Optional.empty();
        }
        Instant fecha = parsearFecha(evento.date());
        if (fecha == null) {
            return Optional.empty();
        }
        return Optional.of(new EventoCalendario(
                evento.title(),
                evento.country(),
                fecha,
                impacto,
                parsearValor(evento.forecast()),
                parsearValor(evento.previous())
        ));
    }

    private Impacto mapearImpacto(String impacto) {
        return switch (impacto) {
            case "High" -> Impacto.ALTO;
            case "Medium" -> Impacto.MEDIO;
            default -> null;
        };
    }

    private Instant parsearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(fecha).toInstant();
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(fecha).atZone(ET).toInstant();
            } catch (DateTimeParseException e2) {
                log.warn("Fecha no parseable: {}", fecha);
                return null;
            }
        }
    }

    private BigDecimal parsearValor(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String s = valor.trim();
        BigDecimal multiplicador = BigDecimal.ONE;
        if (s.endsWith("%")) {
            s = s.substring(0, s.length() - 1).trim();
        }
        if (s.endsWith("K") || s.endsWith("k")) {
            multiplicador = new BigDecimal("1000");
            s = s.substring(0, s.length() - 1).trim();
        } else if (s.endsWith("M") || s.endsWith("m")) {
            multiplicador = new BigDecimal("1000000");
            s = s.substring(0, s.length() - 1).trim();
        } else if (s.endsWith("B") || s.endsWith("b")) {
            multiplicador = new BigDecimal("1000000000");
            s = s.substring(0, s.length() - 1).trim();
        }
        try {
            return new BigDecimal(s).multiply(multiplicador);
        } catch (NumberFormatException e) {
            log.warn("Valor no parseable: {}", valor);
            return null;
        }
    }

    /** DTO del feed: un evento individual del calendario. */
    public record FfEvent(String title, String country, String date, String impact, String forecast, String previous) {
    }
}
