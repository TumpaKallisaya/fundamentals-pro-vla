package com.funds.backend.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.funds.backend.application.service.dto.TendenciaDto;
import com.funds.backend.domain.port.out.TendenciaDiariaRepository;

/**
 * Caso de uso de consulta: devuelve la tendencia diaria por índice para un rango de fechas.
 */
@Service
public class ConsultarTendenciasService {

    private final TendenciaDiariaRepository tendenciaDiariaRepository;

    public ConsultarTendenciasService(TendenciaDiariaRepository tendenciaDiariaRepository) {
        this.tendenciaDiariaRepository = tendenciaDiariaRepository;
    }

    public List<TendenciaDto> consultar(LocalDate desde, LocalDate hasta, String indice) {
        return tendenciaDiariaRepository.findByFechaBetween(desde, hasta).stream()
                .filter(t -> indice == null || t.indiceCodigo().equals(indice))
                .map(t -> new TendenciaDto(t.indiceCodigo(), t.fecha(), t.puntaje(), t.clasificacion()))
                .toList();
    }
}
