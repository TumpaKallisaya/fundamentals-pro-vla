# Misión — Sistema IndicadoresFundamentales

## Declaración de misión

Proveer a un trader scalper, de forma **inmediata** y bajo demanda, el resultado de las
noticias/eventos fundamentales que afectan los mercados financieros y una **proyección de
tendencia del día** (bajista o alcista), calculada con reglas deterministas a partir de los
resultados presentados.

## Problema que resuelve

- El trader ya tiene una estrategia de ejecución (opera en temporalidades de 4h, 1h y 15 min
  y ejecuta en 1 minuto), pero **no cuenta con un contexto inmediato** de los resultados de
  noticias fundamentales.
- Consultar manualmente múltiples APIs y páginas web es lento y disperso; el trader necesita
  una lectura consolidada en un solo lugar, al instante.

## Usuario objetivo / Stakeholders

- **Trader scalper** (usuario principal): revisa 4h, 1h y 15 min y ejecuta en 1 minuto.
- Requiere:
  - Contexto inmediato de noticias fundamentales que afectan los mercados.
  - Resultados presentados en relación a las noticias fundamentales.
  - Interpretación proyectada (bajista/alcista) para el mercado, integrada y actualizada.

## Objetivos del sistema

1. **Actualización bajo demanda**: al presionar un botón, el sistema consulta las APIs y
   páginas web configuradas y actualiza toda la información.
2. **Resultados por evento**: presentar el valor real, el pronóstico/consenso y el valor
   anterior de cada indicador fundamental.
3. **Interpretación automática**: clasificar cada evento en tres estados — **mejor de lo
   esperado (verde)**, **dentro de lo esperado (amarillo)** y **peor de lo esperado (rojo)** —
   según real vs. pronóstico, con tolerancia configurable por indicador.
4. **Tendencia proyectada del día**: agregar la interpretación de todos los eventos del día
   en una proyección integrada (alcista / neutral / bajista) mediante puntaje ponderado por
   impacto.
5. **Refresco incremental**: cada pulsación del botón actualiza los resultados faltantes y
   recalcula la tendencia proyectada del día.

## Alcance

### En alcance (actual)

- Sector de **índices financieros**: **NASDAQ 100 (NAS100)** y **S&P 500 (SP500)**.
- Eventos fundamentales de **alto y medio impacto** de la economía de EE.UU. (p. ej. CPI,
  NFP/desempleo, FOMC/tasas, PPI, PMI, ventas minoristas, PIB, confianza del consumidor).
- Zona horaria de presentación: **America/New_York (ET)**.
- Interpretación basada en **reglas por evento (actual vs. pronóstico)**.
- Actualización **manual** (botón).

### Fuera de alcance (por ahora)

- Ejecución de órdenes / trading automatizado.
- Señales de entrada/salida de la estrategia del trader.
- Fuentes de noticias de texto libre (no calendario) en la primera versión.

### Proyectado (expansión futura)

- **Monedas (forex)** y **criptomonedas** como sectores adicionales.
- La arquitectura debe permitir agregar nuevos sectores e indicadores sin rediseñar el core.

## Principios

- **Inmediatez**: la información y su interpretación deben estar disponibles al instante tras
  la actualización.
- **Determinismo y trazabilidad**: toda lectura bajista/alcista debe poder explicarse a partir
  de la regla y los datos que la originaron.
- **Extensibilidad por catálogo**: los indicadores, sus fuentes y sus reglas se definen como
  datos/configuración, no como código rígido.
- **Consolidación**: una sola vista que resume todos los resultados y la tendencia del día.

## Criterios de éxito

- Con un clic, el sistema consulta las fuentes y muestra los resultados actualizados en ET.
- Cada evento muestra real, pronóstico y anterior, con su estado (mejor/esperado/peor)
  justificado.
- La tendencia proyectada del día se recalcula al instante ante cada actualización.
- El catálogo permite añadir un indicador o sector nuevo sin tocar la lógica central.
