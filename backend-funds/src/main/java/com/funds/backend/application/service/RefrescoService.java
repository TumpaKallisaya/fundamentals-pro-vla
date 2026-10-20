package com.funds.backend.application.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funds.backend.application.service.dto.RefrescoDto;
import com.funds.backend.domain.model.EstadoRefresco;
import com.funds.backend.domain.model.Refresco;
import com.funds.backend.domain.port.out.RefrescoRepository;

/**
 * Caso de uso: orquesta el refresco completo (sincronizar calendario + resolver "actual" +
 * calcular tendencias) con un único {@link Refresco} de trazabilidad.
 */
@Service
public class RefrescoService {

    private static final ZoneId ET = ZoneId.of("America/New_York");

    private final RefrescoRepository refrescoRepository;
    private final SincronizarCalendarioService sincronizarCalendarioService;
    private final BackfillHistoricoService backfillHistoricoService;
    private final CalcularTendenciasService calcularTendenciasService;

    public RefrescoService(RefrescoRepository refrescoRepository,
                           SincronizarCalendarioService sincronizarCalendarioService,
                           BackfillHistoricoService backfillHistoricoService,
                           CalcularTendenciasService calcularTendenciasService) {
        this.refrescoRepository = refrescoRepository;
        this.sincronizarCalendarioService = sincronizarCalendarioService;
        this.backfillHistoricoService = backfillHistoricoService;
        this.calcularTendenciasService = calcularTendenciasService;
    }

    /** Ejecuta el refresco completo y devuelve su estado. */
    public RefrescoDto refrescar() {
        Refresco refresco = refrescoRepository.save(
                new Refresco(null, Instant.now(), null, EstadoRefresco.EN_PROCESO, null));
        try {
            int eventos = sincronizarCalendarioService.sincronizar();
            LocalDate hoy = LocalDate.now(ET);
            int historicos = backfillHistoricoService.backfill(refresco.id(), hoy);
            calcularTendenciasService.calcular(refresco.id(), hoy);
            Refresco cerrado = refrescoRepository.save(new Refresco(
                    refresco.id(), refresco.iniciadoEnUtc(), Instant.now(), EstadoRefresco.COMPLETADO,
                    "Eventos sincronizados: " + eventos + ", históricos: " + historicos));
            return toDto(cerrado);
        } catch (Exception e) {
            Refresco error = refrescoRepository.save(new Refresco(
                    refresco.id(), refresco.iniciadoEnUtc(), Instant.now(), EstadoRefresco.ERROR, e.getMessage()));
            return toDto(error);
        }
    }

    /** Devuelve el estado del último refresco, si existe. */
    public Optional<RefrescoDto> ultimo() {
        return refrescoRepository.findLatest().map(this::toDto);
    }

    private RefrescoDto toDto(Refresco refresco) {
        return new RefrescoDto(
                refresco.id(), refresco.estado(), refresco.detalle(),
                refresco.iniciadoEnUtc(), refresco.finalizadoEnUtc()
        );
    }
}
