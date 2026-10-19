package com.funds.backend.application.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.EventoCalendario;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.port.out.CalendarioEconomicoPort;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;

/**
 * Caso de uso: sincroniza el calendario económico desde la fuente externa y persiste los eventos.
 * No crea el {@code Refresco}; la orquestación la hace {@link RefrescoService}.
 */
@Service
public class SincronizarCalendarioService {

    private static final Logger log = LoggerFactory.getLogger(SincronizarCalendarioService.class);

    private final CalendarioEconomicoPort calendarioPort;
    private final IndicadorRepository indicadorRepository;
    private final EventoRepository eventoRepository;

    public SincronizarCalendarioService(CalendarioEconomicoPort calendarioPort,
                                        IndicadorRepository indicadorRepository,
                                        EventoRepository eventoRepository) {
        this.calendarioPort = calendarioPort;
        this.indicadorRepository = indicadorRepository;
        this.eventoRepository = eventoRepository;
    }

    /** Sincroniza el calendario y devuelve el número de eventos guardados. */
    public int sincronizar() {
        List<EventoCalendario> eventos = calendarioPort.obtenerEventos();
        int guardados = 0;
        int descartados = 0;
        for (EventoCalendario evento : eventos) {
            Optional<Indicador> indicador = indicadorRepository.findByAliasFf(evento.titulo());
            if (indicador.isEmpty()) {
                descartados++;
                continue;
            }
            String indicadorId = indicador.get().id();
            if (eventoRepository.findByIndicadorIdAndFechaHoraUtc(indicadorId, evento.fechaHoraUtc()).isEmpty()) {
                eventoRepository.save(new Evento(
                        null, indicadorId, evento.fechaHoraUtc(), evento.forecast(), evento.previous()
                ));
                guardados++;
            }
        }
        log.info("Sincronización completada: {} eventos guardados, {} descartados.", guardados, descartados);
        return guardados;
    }
}
