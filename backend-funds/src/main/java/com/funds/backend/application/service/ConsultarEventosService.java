package com.funds.backend.application.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funds.backend.application.service.dto.EventoDto;
import com.funds.backend.domain.model.EstadoEvento;
import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.Resultado;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorIndiceRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.ResultadoRepository;

/**
 * Caso de uso de consulta: devuelve los eventos de un rango de fechas (ET) con su resultado y
 * clasificación, filtrable por índice.
 */
@Service
public class ConsultarEventosService {

    private static final ZoneId ET = ZoneId.of("America/New_York");

    private final EventoRepository eventoRepository;
    private final ResultadoRepository resultadoRepository;
    private final IndicadorRepository indicadorRepository;
    private final IndicadorIndiceRepository indicadorIndiceRepository;

    public ConsultarEventosService(EventoRepository eventoRepository,
                                   ResultadoRepository resultadoRepository,
                                   IndicadorRepository indicadorRepository,
                                   IndicadorIndiceRepository indicadorIndiceRepository) {
        this.eventoRepository = eventoRepository;
        this.resultadoRepository = resultadoRepository;
        this.indicadorRepository = indicadorRepository;
        this.indicadorIndiceRepository = indicadorIndiceRepository;
    }

    public List<EventoDto> consultar(LocalDate desde, LocalDate hasta, String indice) {
        Instant desdeUtc = desde.atStartOfDay(ET).toInstant();
        Instant hastaUtc = hasta.plusDays(1).atStartOfDay(ET).toInstant();

        List<EventoDto> resultado = new ArrayList<>();
        for (Evento evento : eventoRepository.findByFechaHoraUtcBetween(desdeUtc, hastaUtc)) {
            Optional<Indicador> indicadorOpt = indicadorRepository.findById(evento.indicadorId());
            if (indicadorOpt.isEmpty()) {
                continue;
            }
            Indicador indicador = indicadorOpt.get();
            if (indice != null && !tieneIndice(evento.indicadorId(), indice)) {
                continue;
            }

            Optional<Resultado> res = resultadoRepository.findLatestByEventoId(evento.id());
            resultado.add(new EventoDto(
                    evento.id(),
                    evento.indicadorId(),
                    indicador.nombre(),
                    evento.fechaHoraUtc(),
                    evento.forecast(),
                    evento.previous(),
                    res.map(Resultado::actual).orElse(null),
                    res.map(Resultado::estado).orElse(EstadoEvento.PENDIENTE),
                    res.map(Resultado::desviacion).orElse(null)
            ));
        }
        return resultado;
    }

    private boolean tieneIndice(String indicadorId, String indice) {
        return indicadorIndiceRepository.findByIndicadorId(indicadorId).stream()
                .anyMatch(rel -> rel.indiceCodigo().equals(indice));
    }
}
