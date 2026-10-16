# Prompts elaborados para el sistema de indicadores para Fundamentales

## Prompt 1
Se desarrollara un sistema informático que presente los resultados de todas las noticias fundamentales que afectan los mercados financieros y la tendencia propyecta que tiene el mercado (bajista o alcista) en funcion a los resultados presentados, para el día, que de momento son específicas para el sector de indices financieros (NASDAQ y SP500) pero que debe ser proyectado para incrementar mercados financieros como monedas o criptomonedas. Lee el archivo README.md para tener un contexto de los stakeholders y espera instrucciones.

## Prompt 2

Vamos a crear una "constitución" en una carpeta "specs":
- `mission.md`
- `tech-stack.md` la tecnologia a usarse sera: backend, spring boot en su ultima versión estable (tendra como nombre del proyecto backend-funds), frontend, angular en su ultima versión (tendra como nombre del proyecto frontend-funds), base de datos, postgresql en su ultima versión.
- `roadmap.md` para un orden de implementación de alto nivel, en pequeñas fases de trabajo.

Importante: si requieres hacer preguntas para aclarar aspectos lógicos o técnicos pregunta antes de escribir a disco.

## Prompt 3

En el roadmap para la fase 6, el dashboard tendra dos secciones:
- Una donde los resultados de cada indicador, estara en una tabla para el mes donde a la izquierda estaran los indicadores y a en las columnas estara la fecha, en las columnas cada resultado sera de color rojo si salio fuera de lo esperado, amarillo si esta dentro de lo esperado, y verde si esta mejor de lo esperado.
- Otra sección sera el resultado general proyectado para el dia de cada indice, nas100 o sp500, también sera una tabla donde esten todos los resultados proyectados por dia durante el mes.

Para cada sección se podra extender las fechas de consulta para ver resultados historicos (filtros de fechas desde, hasta)
Si requiere modificarse o incrementarse otras fases hazlo.
Si requieres hacer preguntas para aclarar aspectos lógicos o técnicos pregunta antes de escribir a disco.

## Prompt 4

Busca la siguiente fase en specs/roadmap.md. Pregúntame sobre la especificación de la funcionalidad.

Crea:
 - una nueva carpeta YYYY-MM-DD-funcionalidad-nombre debajo de specs para el trabajo de esta funcionalidad
 
 - Ahí:
  - plan.md como una serie de grupos de tareas numerados.
  - requirements.md para el alcance, las decisiones y el contexto.
  - validation.md para determinar si la implementación fue exitosa y puede integrarse al código principal.

Guiate utilizando specs/mission.md y specs/tech-stack.md.

Si requieres hacer preguntas para aclarar aspectos lógicos o técnicos pregunta antes de escribir a disco.