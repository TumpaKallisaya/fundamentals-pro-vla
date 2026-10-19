# Plan — Fase 1: Catálogo de indicadores y fuentes

Serie de grupos de tareas numerados. Cada grupo es un incremento verificable.

## Grupo 1 — Modelo y formato del catálogo

1. Definir los campos por indicador (ver `requirements.md`): `id`, `nombre`, `alias_ff`, `sector`,
   `impacto`, `frecuencia`, `hora_et`, `indices` (con relevancia), `fuente` y `regla`
   (dirección, peso, tolerancia).
2. Definir el formato del seed machine-readable (**JSON**) alineado con el modelo, listo para
   importar en la Fase 2 (Spring/Jackson).

## Grupo 2 — Listado de indicadores (alto + medio, EE.UU.)

1. Listar los **12 indicadores de impacto alto** (FOMC/tasas, NFP, desempleo, salarios, CPI y
   Core CPI, PCE y Core PCE, PIB, ISM manufacturero y de servicios, ventas minoristas).
2. Listar los **11 indicadores de impacto medio** (Jobless Claims, ADP, JOLTS, PPI, bienes
   duraderos, inventarios mayoristas, balanza comercial, UMich, confianza del consumidor,
   producción industrial, ventas de viviendas).
3. Por cada indicador: asignar `id` normalizado (`snake_case`) y `alias_ff` (título tal como
   aparece en el feed de Forex Factory) para el mapeo en Fase 3.

## Grupo 3 — Relevancia por índice (NAS100 / SP500)

1. Asignar relevancia (`alta` / `media` / `baja`) de cada indicador para **NAS100** y para
   **SP500**, reflejando la distinta sensibilidad (p. ej. NAS100 más sensible a tasas/tecnología;
   SP500 más sensible a consumo amplio).
2. Documentar la justificación de cada relevancia en el catálogo.

## Grupo 4 — Reglas de interpretación (valores por defecto)

1. Proponer la **dirección favorable** por indicador (`mayor_es_alcista` | `mayor_es_bajista`
   para el índice).
2. Proponer el **peso** por impacto (Alto = 3, Medio = 1) y la **tolerancia** `±X%` por indicador.
3. Marcar todos los valores como "propuestos por defecto" y revisarlos con el trader antes de
   cerrar la fase.

## Grupo 5 — Documento de catálogo (Markdown)

1. Crear el documento legible del catálogo en
   `specs/2026-10-19-catalogo-indicadores-fuentes/catalogo-indicadores.md`.
2. Incluir por indicador: sector, impacto, frecuencia/hora (ET), índices (relevancia), fuente y
   regla de interpretación.

## Grupo 6 — Seed machine-readable (JSON)

1. Crear `specs/2026-10-19-catalogo-indicadores-fuentes/indicadores.seed.json` con el catálogo
   completo.
2. Validar estructura: campos obligatorios, `id` únicos, valores de `impacto` y `relevancia`
   dentro del conjunto permitido.

## Grupo 7 — Documentación de fuentes y decisiones

1. Documentar la fuente (Forex Factory, feed JSON) con URL, campos, límites y la limitación
   conocida (no incluye `actual`).
2. Actualizar `requirements.md` y `validation.md` con decisiones y criterios de aceptación.
3. Verificar coherencia con `mission.md` (solo EE.UU., índices NAS100/SP500) y `tech-stack.md`.
