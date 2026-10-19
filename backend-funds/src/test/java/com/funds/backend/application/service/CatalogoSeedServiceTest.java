package com.funds.backend.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import tools.jackson.databind.ObjectMapper;
import com.funds.backend.domain.model.Direccion;
import com.funds.backend.domain.model.Impacto;
import com.funds.backend.domain.model.Indicador;
import com.funds.backend.domain.model.IndicadorIndice;
import com.funds.backend.domain.model.Indice;
import com.funds.backend.domain.model.Relevancia;
import com.funds.backend.domain.port.out.IndicadorIndiceRepository;
import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.IndiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Prueba unitaria del caso de uso de siembra {@link CatalogoSeedService}.
 */
@ExtendWith(MockitoExtension.class)
class CatalogoSeedServiceTest {

    @Mock
    private IndiceRepository indiceRepository;
    @Mock
    private IndicadorRepository indicadorRepository;
    @Mock
    private IndicadorIndiceRepository indicadorIndiceRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CatalogoSeedService service;

    @Test
    void seedInsertaIndicesIndicadorYRelaciones() {
        SeedIndicador item = seedIndicador("cpi", "Alto", "mayor_es_bajista", "alta", "media");

        service.seed(List.of(item));

        verify(indiceRepository).save(new Indice("NAS100", "Nasdaq 100", "indices"));
        verify(indiceRepository).save(new Indice("SP500", "S&P 500", "indices"));

        ArgumentCaptor<Indicador> indicadorCaptor = ArgumentCaptor.forClass(Indicador.class);
        verify(indicadorRepository).save(indicadorCaptor.capture());
        Indicador indicador = indicadorCaptor.getValue();
        assertThat(indicador.id()).isEqualTo("cpi");
        assertThat(indicador.nombre()).isEqualTo("CPI (m/m)");
        assertThat(indicador.impacto()).isEqualTo(Impacto.ALTO);
        assertThat(indicador.regla().direccion()).isEqualTo(Direccion.MAYOR_ES_BAJISTA);
        assertThat(indicador.regla().peso()).isEqualTo(3);
        assertThat(indicador.regla().toleranciaPct()).isEqualByComparingTo(new BigDecimal("15"));

        ArgumentCaptor<IndicadorIndice> relCaptor = ArgumentCaptor.forClass(IndicadorIndice.class);
        verify(indicadorIndiceRepository, times(2)).save(relCaptor.capture());
        List<IndicadorIndice> relaciones = relCaptor.getAllValues();
        assertThat(relaciones).extracting(IndicadorIndice::indiceCodigo)
                .containsExactlyInAnyOrder("NAS100", "SP500");
        assertThat(relaciones).extracting(IndicadorIndice::relevancia)
                .containsExactlyInAnyOrder(Relevancia.ALTA, Relevancia.MEDIA);
    }

    @Test
    void seedEsIdempotente() {
        when(indiceRepository.existsByCodigo("NAS100")).thenReturn(true);
        when(indiceRepository.existsByCodigo("SP500")).thenReturn(true);
        when(indicadorRepository.existsById("cpi")).thenReturn(true);
        when(indicadorIndiceRepository.existsByIndicadorIdAndIndiceCodigo("cpi", "NAS100")).thenReturn(true);
        when(indicadorIndiceRepository.existsByIndicadorIdAndIndiceCodigo("cpi", "SP500")).thenReturn(true);

        service.seed(List.of(seedIndicador("cpi", "Alto", "mayor_es_bajista", "alta", "media")));

        verify(indiceRepository, never()).save(any());
        verify(indicadorRepository, never()).save(any());
        verify(indicadorIndiceRepository, never()).save(any());
    }

    private SeedIndicador seedIndicador(String id, String impacto, String direccion, String relNas, String relSp) {
        return new SeedIndicador(
                id,
                "CPI (m/m)",
                "CPI m/m",
                "indices",
                impacto,
                "Mensual",
                "08:30",
                List.of(
                        new SeedIndicador.SeedIndice("NAS100", relNas),
                        new SeedIndicador.SeedIndice("SP500", relSp)
                ),
                new SeedIndicador.SeedFuente("Forex Factory", "api",
                        "https://nfs.faireconomy.media/ff_calendar_thisweek.json", "notas"),
                new SeedIndicador.SeedRegla(direccion, 3, new BigDecimal("15"))
        );
    }
}
