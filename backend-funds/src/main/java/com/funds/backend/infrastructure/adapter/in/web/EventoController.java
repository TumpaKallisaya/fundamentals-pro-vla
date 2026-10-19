package com.funds.backend.infrastructure.adapter.in.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.funds.backend.application.service.ConsultarEventosService;
import com.funds.backend.application.service.dto.EventoDto;

/**
 * Adaptador de entrada: consulta de eventos con su resultado y clasificación.
 */
@RestController
@RequestMapping("/api")
public class EventoController {

    private static final ZoneId ET = ZoneId.of("America/New_York");

    private final ConsultarEventosService consultarEventosService;

    public EventoController(ConsultarEventosService consultarEventosService) {
        this.consultarEventosService = consultarEventosService;
    }

    @GetMapping("/eventos")
    public List<EventoDto> consultar(
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta,
            @RequestParam(required = false) String indice) {
        LocalDate hoy = LocalDate.now(ET);
        LocalDate desdeEfectivo = desde != null ? desde : hoy.withDayOfMonth(1);
        LocalDate hastaEfectivo = hasta != null ? hasta : hoy.withDayOfMonth(hoy.lengthOfMonth());
        return consultarEventosService.consultar(desdeEfectivo, hastaEfectivo, indice);
    }
}
