# Plan — Fase 3: Conectores de fuentes externas (backend)

Serie de grupos de tareas numerados. Todo sigue la arquitectura hexagonal obligatoria
(`../tech-stack.md`).

## Grupo 1 — Modelo y puerto del conector

1. Crear en `domain.model` el modelo `EventoCalendario` (titulo, pais, fechaHoraUtc, impacto,
   forecast, previous).
2. Crear en `domain.port.out` el puerto `CalendarioEconomicoPort` con `obtenerEventos()`.
3. Ampliar `IndicadorRepository` (puerto + JPA + adaptador) con `findByAliasFf(String)` para el
   mapeo alias → indicador.

## Grupo 2 — Adaptador de Forex Factory

1. Crear `infrastructure.adapter.out.web.ForexFactoryCalendarioAdapter`, que implementa el puerto
   usando `RestClient`.
2. Configurar un bean `RestClient` con timeouts de conexión/lectura (`RestClientConfig`).
3. Parsear el JSON del feed y normalizar: filtrar `country == USD` e `impact` High/Medium,
   parsear `date` (offset US/Eastern) a `Instant` (UTC) y `forecast`/`previous` a `BigDecimal`.

## Grupo 3 — Resiliencia (caché, reintentos, timeouts)

1. Caché en memoria con TTL de 30–60 min en el adaptador.
2. Reintentos con backoff exponencial ante HTTP 429/5xx (máx. 3 intentos).
3. Timeouts de conexión y lectura configurables.

## Grupo 4 — Caso de uso de sincronización

1. Crear `application.service.SincronizarCalendarioService`: crea un `Refresco` (EN_PROCESO),
   consulta el puerto, mapea alias → indicador (catálogo), persiste/actualiza `Evento` y cierra
   el `Refresco` (COMPLETADO / PARCIAL / ERROR).

## Grupo 5 — Pruebas

1. Prueba unitaria del adaptador (parseo/normalización) con un JSON de muestra (fixture).
2. Prueba unitaria de `SincronizarCalendarioService` con **Mockito** (puerto + repositorios).
3. Verificación manual del fetch real contra Forex Factory.

## Grupo 6 — Documentación y validación

1. Actualizar `requirements.md` y `validation.md` con decisiones y criterios.
2. Verificar coherencia con `tech-stack.md` (hexagonal) y `mission.md` (solo EE.UU., alto/medio).
