package com.funds.backend.application.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.funds.backend.domain.model.EstadoEvento;
import com.funds.backend.domain.model.Evento;
import com.funds.backend.domain.model.Frecuencia;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.Resultado;
import com.funds.backend.domain.model.TipoFrecuencia;
import com.funds.backend.domain.port.out.EventoRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.ResultadoRepository;
import com.funds.backend.domain.port.out.ValorActualPort;
import com.funds.backend.domain.service.CalculadorProximaFecha;

/**
 * Caso de uso: rellena el histórico de resultados (últimos días) para cada indicador, consultando
 * la fuente del "actual" (FRED) y persistiendo los valores en las fechas de publicación
 * calculadas desde la frecuencia.
 */
@Service
public class BackfillHistoricoService {

    private static final Logger log = LoggerFactory.getLogger(BackfillHistoricoService.class);

    private static final ZoneId ET = ZoneId.of("America/New_York");
    private static final int DIAS_HISTORICO = 20;
    private static final int MAX_RELEASES = 8;

    private final ValorActualPort valorActualPort;
    private final IndicadorRepository indicadorRepository;
    private final EventoRepository eventoRepository;
    private final ResultadoRepository resultadoRepository;

    public BackfillHistoricoService(ValorActualPort valorActualPort,
                                    IndicadorRepository indicadorRepository,
                                    EventoRepository eventoRepository,
                                    ResultadoRepository resultadoRepository) {
        this.valorActualPort = valorActualPort;
        this.indicadorRepository = indicadorRepository;
        this.eventoRepository = eventoRepository;
        this.resultadoRepository = resultadoRepository;
    }

    /** Rellena los resultados de los últimos 20 días para cada indicador. */
    public int backfill(Long refrescoId, LocalDate hoy) {
        int persistidos = 0;
        for (Indicador indicador : indicadorRepository.findAll()) {
            Frecuencia frecuencia = FrecuenciasCatalogo.de(indicador.id());
            if (frecuencia == null || frecuencia.tipo() == TipoFrecuencia.IRREGULAR) {
                continue;
            }
            List<LocalDate> fechas = CalculadorProximaFecha.fechasPrevias(frecuencia, hoy, MAX_RELEASES).stream()
                    .filter(f -> !f.isBefore(hoy.minusDays(DIAS_HISTORICO - 1L)))
                    .toList();
            if (fechas.isEmpty()) {
                continue;
            }
            List<BigDecimal> valores = valorActualPort.obtenerUltimos(indicador.id(), fechas.size());
            for (int i = 0; i < fechas.size() && i < valores.size(); i++) {
                BigDecimal valor = valores.get(i);
                if (valor == null) {
                    continue;
                }
                Instant fechaUtc = fechas.get(i).atTime(12, 0).atZone(ET).toInstant();
                Evento evento = eventoRepository.findByIndicadorIdAndFechaHoraUtc(indicador.id(), fechaUtc)
                        .orElseGet(() -> eventoRepository.save(
                                new Evento(null, indicador.id(), fechaUtc, null, null)
                        ));
                resultadoRepository.save(new Resultado(
                        null, evento.id(), refrescoId, valor, EstadoEvento.PENDIENTE, null
                ));
                persistidos++;
            }
        }
        log.info("Backfill histórico completado: {} resultados persistidos.", persistidos);
        return persistidos;
    }
}
