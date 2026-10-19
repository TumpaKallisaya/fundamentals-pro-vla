package com.funds.backend.application.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.funds.backend.domain.model.ClasificacionTendencia;
import com.funds.backend.domain.model.EstadoEvento;
import com.funds.backend.domain.model.EstadoRefresco;
import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.IndicadorIndice;
import com.funds.backend.domain.model.Refresco;
import com.funds.backend.domain.model.Resultado;
import com.funds.backend.domain.model.TendenciaDiaria;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorIndiceRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.RefrescoRepository;
import com.funds.backend.domain.port.out.ResultadoRepository;
import com.funds.backend.domain.port.out.TendenciaDiariaRepository;
import com.funds.backend.domain.port.out.ValorActualPort;
import com.funds.backend.domain.service.AgregadorTendenciaService;
import com.funds.backend.domain.service.ClasificadorEventoService;

/**
 * Caso de uso: para un día (ET), resuelve el valor "actual" de cada evento (FRED/BLS), lo
 * persiste como {@link Resultado}, lo clasifica y calcula la {@link TendenciaDiaria} por índice.
 */
@Service
public class CalcularTendenciasService {

    private static final Logger log = LoggerFactory.getLogger(CalcularTendenciasService.class);
    private static final ZoneId ET = ZoneId.of("America/New_York");

    private final ValorActualPort valorActualPort;
    private final EventoRepository eventoRepository;
    private final IndicadorRepository indicadorRepository;
    private final IndicadorIndiceRepository indicadorIndiceRepository;
    private final ResultadoRepository resultadoRepository;
    private final TendenciaDiariaRepository tendenciaDiariaRepository;
    private final RefrescoRepository refrescoRepository;

    public CalcularTendenciasService(ValorActualPort valorActualPort,
                                     EventoRepository eventoRepository,
                                     IndicadorRepository indicadorRepository,
                                     IndicadorIndiceRepository indicadorIndiceRepository,
                                     ResultadoRepository resultadoRepository,
                                     TendenciaDiariaRepository tendenciaDiariaRepository,
                                     RefrescoRepository refrescoRepository) {
        this.valorActualPort = valorActualPort;
        this.eventoRepository = eventoRepository;
        this.indicadorRepository = indicadorRepository;
        this.indicadorIndiceRepository = indicadorIndiceRepository;
        this.resultadoRepository = resultadoRepository;
        this.tendenciaDiariaRepository = tendenciaDiariaRepository;
        this.refrescoRepository = refrescoRepository;
    }

    /** Calcula y persiste las tendencias del día {@code fechaEt} (America/New_York). */
    public void calcular(LocalDate fechaEt) {
        Refresco refresco = refrescoRepository.save(
                new Refresco(null, Instant.now(), null, EstadoRefresco.EN_PROCESO, null)
        );
        try {
            Instant desde = fechaEt.atStartOfDay(ET).toInstant();
            Instant hasta = fechaEt.plusDays(1).atStartOfDay(ET).toInstant();
            List<Evento> eventos = eventoRepository.findByFechaHoraUtcBetween(desde, hasta);

            Map<String, List<AgregadorTendenciaService.Aporte>> aportesPorIndice = new HashMap<>();
            int resueltos = 0;
            int sinActual = 0;

            for (Evento evento : eventos) {
                Optional<Indicador> indicadorOpt = indicadorRepository.findById(evento.indicadorId());
                if (indicadorOpt.isEmpty()) {
                    continue;
                }
                Indicador indicador = indicadorOpt.get();

                BigDecimal actual = valorActualPort
                        .obtenerActual(evento.indicadorId(), evento.fechaHoraUtc())
                        .orElse(null);
                resultadoRepository.save(new Resultado(null, evento.id(), refresco.id(), actual));
                if (actual != null) {
                    resueltos++;
                } else {
                    sinActual++;
                }

                EstadoEvento estado = ClasificadorEventoService.clasificar(
                        actual, evento.forecast(), indicador.regla());

                for (IndicadorIndice rel : indicadorIndiceRepository.findByIndicadorId(evento.indicadorId())) {
                    aportesPorIndice
                            .computeIfAbsent(rel.indiceCodigo(), k -> new ArrayList<>())
                            .add(new AgregadorTendenciaService.Aporte(estado, indicador.regla().peso(), rel.relevancia()));
                }
            }

            for (Map.Entry<String, List<AgregadorTendenciaService.Aporte>> entry : aportesPorIndice.entrySet()) {
                BigDecimal puntaje = AgregadorTendenciaService.calcularPuntaje(entry.getValue());
                ClasificacionTendencia clasificacion = AgregadorTendenciaService.clasificar(puntaje);
                Optional<TendenciaDiaria> existente =
                        tendenciaDiariaRepository.findByIndiceCodigoAndFecha(entry.getKey(), fechaEt);
                Long id = existente.map(TendenciaDiaria::id).orElse(null);
                tendenciaDiariaRepository.save(new TendenciaDiaria(id, entry.getKey(), fechaEt, puntaje, clasificacion));
            }

            refrescoRepository.save(new Refresco(
                    refresco.id(), refresco.iniciadoEnUtc(), Instant.now(), EstadoRefresco.COMPLETADO,
                    "Tendencias calculadas. Eventos: " + eventos.size()
                            + ", actual resuelto: " + resueltos + ", sin actual: " + sinActual
            ));
            log.info("Tendencias calculadas para {}: {} eventos, {} con actual.", fechaEt, eventos.size(), resueltos);
        } catch (Exception e) {
            log.error("Error al calcular tendencias para {}", fechaEt, e);
            refrescoRepository.save(new Refresco(
                    refresco.id(), refresco.iniciadoEnUtc(), Instant.now(), EstadoRefresco.ERROR, e.getMessage()
            ));
            throw e;
        }
    }
}
