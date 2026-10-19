package com.funds.backend.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.funds.backend.domain.port.out.IndicadorRepository;
import com.funds.backend.domain.port.out.IndiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de integración de persistencia: verifica que Flyway aplica el esquema y que el seeder
 * carga el catálogo (2 índices y 23 indicadores) contra PostgreSQL.
 */
@SpringBootTest
class SeedIntegrationTest {

    @Autowired
    private IndiceRepository indiceRepository;

    @Autowired
    private IndicadorRepository indicadorRepository;

    @Test
    void catalogoSeSiembraConDosIndicesYVeintitresIndicadores() {
        assertThat(indiceRepository.findAll()).hasSize(2);
        assertThat(indicadorRepository.count()).isEqualTo(23L);
    }
}
