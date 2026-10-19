package com.funds.backend.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.funds.backend.domain.model.EstadoRefresco;
import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.EventoCalendario;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.Refresco;
import com.funds.backend.domain.port.out.CalendarioEconomicoPort;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.RefrescoRepository;

/**
 * Caso de uso: sincroniza el calendario económico desde la fuente externa y lo persiste,
 * registrando la operación en un {@link Refresco} para trazabilidad.
 */
@Service
public class SincronizarCalendarioService {

    private static final Logger log = LoggerFactory.getLogger(SincronizarCalendarioService.class);

    private final CalendarioEconomicoPort calendarioPort;
    private final IndicadorRepository indicadorRepository;
    private final EventoRepository eventoRepository;
    private final RefrescoRepository refrescoRepository;

    public SincronizarCalendarioService(CalendarioEconomicoPort calendarioPort,
                                        IndicadorRepository indicadorRepository,
                                        EventoRepository eventoRepository,
                                        RefrescoRepository refrescoRepository) {
        this.calendarioPort = calendarioPort;
        this.indicadorRepository = indicadorRepository;
        this.eventoRepository = eventoRepository;
        this.refrescoRepository = refrescoRepository;
    }

    /** Sincroniza el calendario y persiste los eventos nuevos. */
    public void sincronizar() {
        Refresco refresco = refrescoRepository.save(
                new Refresco(null, Instant.now(), null, EstadoRefresco.EN_PROCESO, null)
        );
        try {
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
            refrescoRepository.save(new Refresco(
                    refresco.id(), refresco.iniciadoEnUtc(), Instant.now(), EstadoRefresco.COMPLETADO,
                    "Eventos guardados: " + guardados + ", descartados: " + descartados
            ));
            log.info("Sincronización completada: {} eventos guardados, {} descartados.", guardados, descartados);
        } catch (Exception e) {
            log.error("Error durante la sincronización del calendario", e);
            refrescoRepository.save(new Refresco(
                    refresco.id(), refresco.iniciadoEnUtc(), Instant.now(), EstadoRefresco.ERROR, e.getMessage()
            ));
            throw e;
        }
    }
}
