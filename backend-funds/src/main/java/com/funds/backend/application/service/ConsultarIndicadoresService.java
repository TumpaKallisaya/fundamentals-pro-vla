package com.funds.backend.application.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.funds.backend.application.service.dto.IndicadorDto;
import com.funds.backend.domain.model.Frecuencia;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.Impacto;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.service.CalculadorProximaFecha;

/**
 * Caso de uso de consulta: devuelve los indicadores con su impacto y su próxima fecha de
 * publicación, ordenados por impacto (alto primero) y nombre.
 */
@Service
public class ConsultarIndicadoresService {

    private static final ZoneId ET = ZoneId.of("America/New_York");

    private final IndicadorRepository indicadorRepository;

    public ConsultarIndicadoresService(IndicadorRepository indicadorRepository) {
        this.indicadorRepository = indicadorRepository;
    }

    public List<IndicadorDto> consultar() {
        LocalDate hoy = LocalDate.now(ET);
        return indicadorRepository.findAll().stream()
                .map(i -> toDto(i, hoy))
                .sorted(Comparator
                        .comparing(IndicadorDto::impacto, Comparator.comparing(Impacto::ordinal))
                        .thenComparing(IndicadorDto::nombre))
                .toList();
    }

    private IndicadorDto toDto(Indicador indicador, LocalDate hoy) {
        Frecuencia frecuencia = FrecuenciasCatalogo.de(indicador.id());
        LocalDate siguiente = CalculadorProximaFecha.siguienteFecha(frecuencia, hoy).orElse(null);
        return new IndicadorDto(
                indicador.id(),
                indicador.nombre(),
                indicador.impacto(),
                indicador.aliasFf(),
                siguiente
        );
    }
}
