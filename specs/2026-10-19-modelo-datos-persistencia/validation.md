# Validación — Fase 2: Modelo de datos y persistencia (backend)

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] Existe la carpeta `specs/2026-10-19-modelo-datos-persistencia/` con `plan.md`,
  `requirements.md` y `validation.md`.
- [ ] Las entidades de dominio (`Indicador`, `Indice`, `IndicadorIndice`, `Evento`, `Resultado`,
  `TendenciaDiaria`, `Refresco`) están en `domain.model` y **no** importan JPA/Spring.
- [ ] Existen los puertos de salida (`*Repository`) en `domain.port.out` como interfaces puras.
- [ ] La migración `V2__create_tablas.sql` crea las 7 tablas con PKs, FKs, índices y restricciones
  únicas correctas (`evento(indicador,fecha_hora_utc)`, `tendencia_diaria(indice,fecha)`,
  `resultado(evento,refresco)`, `indicador_indice(indicador,indice)`).
- [ ] Las fechas/horas se almacenan en columnas `TIMESTAMPTZ` (UTC).
- [ ] Existen las entidades JPA, repositorios Spring Data, mappers y adaptadores en
  `infrastructure.adapter.out.persistence`.
- [ ] El seeder carga **2 índices** y **23 indicadores** con sus relevancias, de forma idempotente.
- [ ] `mvn test` pasa (pruebas unitarias con Mockito + integración de persistencia).
- [ ] El backend arranca y conecta a PostgreSQL; Flyway aplica `V1` y `V2` sin errores.
- [ ] No se añade ningún endpoint REST nuevo (solo persistencia y seed).

## Verificación manual (comandos)

```
# backend: compilar y probar
cd backend-funds
./mvnw test          # Windows: mvnw.cmd test

# verificar tablas creadas y datos sembrados
docker exec funds-postgres psql -U funds -d funds -c "\dt"
docker exec funds-postgres psql -U funds -d funds -c "SELECT count(*) FROM indicador;"
docker exec funds-postgres psql -U funds -d funds -c "SELECT count(*) FROM indice;"
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- El modelo respeta la **arquitectura hexagonal estricta** documentada en `tech-stack.md`
  (dominio sin framework, puertos y adaptadores).
- El esquema es coherente con el catálogo de la Fase 1 (23 indicadores, relevancia por índice).
- El seed es idempotente (re-ejecutar no duplica datos).
- El commit contiene solo los cambios de esta fase (backend: dominio, puertos, migración,
  adaptadores, seed y pruebas), sin tocar fases posteriores.
