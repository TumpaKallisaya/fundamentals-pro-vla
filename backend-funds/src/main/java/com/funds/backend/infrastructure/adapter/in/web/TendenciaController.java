package com.funds.backend.infrastructure.adapter.in.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.funds.backend.application.service.ConsultarTendenciasService;
import com.funds.backend.application.service.dto.TendenciaDto;

/**
 * Adaptador de entrada: consulta de tendencia diaria por índice.
 */
@RestController
@RequestMapping("/api")
public class TendenciaController {

    private static final ZoneId ET = ZoneId.of("America/New_York");

    private final ConsultarTendenciasService consultarTendenciasService;

    public TendenciaController(ConsultarTendenciasService consultarTendenciasService) {
        this.consultarTendenciasService = consultarTendenciasService;
    }

    @GetMapping("/tendencias")
    public List<TendenciaDto> consultar(
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta,
            @RequestParam(required = false) String indice) {
        LocalDate hoy = LocalDate.now(ET);
        LocalDate desdeEfectivo = desde != null ? desde : hoy.withDayOfMonth(1);
        LocalDate hastaEfectivo = hasta != null ? hasta : hoy.withDayOfMonth(hoy.lengthOfMonth());
        return consultarTendenciasService.consultar(desdeEfectivo, hastaEfectivo, indice);
    }
}
