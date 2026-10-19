# Plan — Fase 2: Modelo de datos y persistencia (backend)

Serie de grupos de tareas numerados. Cada grupo es un incremento verificable. Todo sigue la
arquitectura hexagonal obligatoria (`../tech-stack.md`).

## Grupo 1 — Entidades de dominio (puras, sin framework)

1. Crear en `domain.model` las entidades de dominio (solo JDK, sin JPA): `Indicador`, `Indice`,
   `IndicadorIndice`, `Evento`, `Resultado`, `TendenciaDiaria` y `Refresco`.
2. Definir los value objects `Fuente` (nombre, tipo, url, notas) y `Regla` (dirección, peso,
   tolerancia) y los enums `Impacto`, `Direccion`, `Relevancia`, `EstadoRefresco` y
   `ClasificacionTendencia`.

## Grupo 2 — Puertos de salida (repositorios)

1. Crear en `domain.port.out` las interfaces: `IndicadorRepository`, `IndiceRepository`,
   `EventoRepository`, `ResultadoRepository`, `TendenciaDiariaRepository` y `RefrescoRepository`.
2. Definir métodos mínimos (búsqueda por id/clave natural, `save`, `exists`) sin exponer JPA.

## Grupo 3 — Migración Flyway (esquema)

1. Crear `V2__create_tablas.sql` con las tablas `indice`, `indicador`, `indicador_indice`,
   `evento`, `resultado`, `tendencia_diaria` y `refresco`.
2. Definir PKs, FKs, índices y restricciones únicas: `(indicador_id, fecha_hora_utc)` en `evento`,
   `(indice_id, fecha)` en `tendencia_diaria`, `(evento_id, refresco_id)` en `resultado` y
   `(indicador_id, indice_id)` en `indicador_indice`.
3. Fechas/horas en columnas `TIMESTAMPTZ` (UTC interno).

## Grupo 4 — Entidades JPA, repositorios Spring Data y adaptadores de salida

1. Crear en `infrastructure.adapter.out.persistence` las entidades JPA (`@Entity`/`@Embeddable`) y
   los repositorios Spring Data (`JpaRepository`).
2. Crear los mappers dominio ↔ entidad JPA.
3. Crear los adaptadores que implementan los puertos de `domain.port.out` delegando en Spring Data.

## Grupo 5 — Seed del catálogo (Fase 1 → BD)

1. Copiar `indicadores.seed.json` a `backend-funds/src/main/resources/db/seed/indicadores.json`.
2. Crear un seeder (caso de uso en `application.service` + `ApplicationRunner` en
   `infrastructure.config`) que lea el JSON y siembre `Indice`, `Indicador` y `IndicadorIndice`
   (relevancia) de forma idempotente (upsert).
3. Verificar que carga 2 índices y 23 indicadores con sus relevancias.

## Grupo 6 — Pruebas

1. Prueba unitaria del seeder (parseo y mapeo del JSON) con **Mockito** sobre los repositorios.
2. Prueba de integración de persistencia contra PostgreSQL: guardar y recuperar cada entidad y
   validar que Flyway aplica `V1` y `V2` sin errores.

## Grupo 7 — Documentación y validación

1. Actualizar `requirements.md` y `validation.md` con decisiones y criterios.
2. Verificar coherencia con `tech-stack.md` (hexagonal estricto, UTC) y `mission.md`.
