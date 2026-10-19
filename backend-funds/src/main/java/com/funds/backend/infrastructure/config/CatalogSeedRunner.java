package com.funds.backend.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.funds.backend.application.service.CatalogoSeedService;

/**
 * Ejecuta la siembra del catálogo de indicadores al arrancar la aplicación.
 */
@Component
public class CatalogSeedRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogSeedRunner.class);

    private final CatalogoSeedService catalogoSeedService;

    public CatalogSeedRunner(CatalogoSeedService catalogoSeedService) {
        this.catalogoSeedService = catalogoSeedService;
    }

    @Override
    public void run(ApplicationArguments args) {
        catalogoSeedService.seed();
        log.info("Catálogo de indicadores sembrado correctamente.");
    }
}
