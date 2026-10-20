package com.funds.backend.infrastructure.adapter.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.funds.backend.application.service.ConsultarIndicadoresService;
import com.funds.backend.application.service.dto.IndicadorDto;

/**
 * Adaptador de entrada: consulta de indicadores con su impacto y próxima fecha de publicación.
 */
@RestController
@RequestMapping("/api")
public class IndicadorController {

    private final ConsultarIndicadoresService consultarIndicadoresService;

    public IndicadorController(ConsultarIndicadoresService consultarIndicadoresService) {
        this.consultarIndicadoresService = consultarIndicadoresService;
    }

    @GetMapping("/indicadores")
    public List<IndicadorDto> consultar() {
        return consultarIndicadoresService.consultar();
    }
}
