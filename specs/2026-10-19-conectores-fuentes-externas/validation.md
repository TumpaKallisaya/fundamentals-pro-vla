# Validación — Fase 3: Conectores de fuentes externas (backend)

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] Existe la carpeta `specs/2026-10-19-conectores-fuentes-externas/` con `plan.md`,
  `requirements.md` y `validation.md`.
- [ ] Existen `domain.model.EventoCalendario` y `domain.port.out.CalendarioEconomicoPort`.
- [ ] `IndicadorRepository` expone `findByAliasFf(String)` (puerto, JPA y adaptador).
- [ ] Existe `ForexFactoryCalendarioAdapter` (implementa el puerto) con `RestClient`, caché TTL,
  reintentos con backoff y timeouts.
- [ ] La normalización: filtra `country == USD`, filtra impacto High/Medium, convierte la fecha a
  UTC (`Instant`) y parsea `forecast`/`previous` a `BigDecimal` (reglas `%`, `K`, `M`, `B`).
- [ ] Existe `SincronizarCalendarioService` que crea el `Refresco`, persiste `Evento` y cierra el
  `Refresco` con el estado correcto.
- [ ] `mvn test` pasa (pruebas unitarias del adaptador y del caso de uso con Mockito).
- [ ] El fetch real contra Forex Factory devuelve eventos y el caso de uso los persiste (manual).
- [ ] El backend arranca sin errores con el conector configurado.

## Verificación manual (comandos)

```
# backend: compilar y probar
cd backend-funds
./mvnw test          # Windows: mvnw.cmd test

# verificar eventos persistidos tras una sincronización
docker exec funds-postgres psql -U funds -d funds -c "SELECT count(*) FROM evento;"
docker exec funds-postgres psql -U funds -d funds -c "SELECT count(*) FROM refresco;"
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- El conector respeta la **arquitectura hexagonal** (puerto en `domain.port.out`, adaptador en
  `infrastructure`); el dominio no importa HTTP ni Jackson.
- La normalización respeta **UTC interno** (Fase 2) y el catálogo de Fase 1 (solo indicadores
  catalogados se persisten).
- La caché/reintentos evitan exceder el rate limit de Forex Factory.
- El commit contiene solo los cambios de esta fase (backend: puerto, adaptador, caso de uso,
  config y pruebas), sin tocar fases posteriores.
