# Validación — Fase 5: API REST (backend)

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] Existe la carpeta `specs/2026-10-19-api-rest/` con `plan.md`, `requirements.md` y
  `validation.md`.
- [ ] La migración `V4__resultado_estado.sql` añade `estado` y `desviacion` a `resultado`.
- [ ] `Resultado` (dominio, JPA y mapper) incluye `estado` y `desviacion`.
- [ ] `CalcularTendenciasService` persiste `estado` y `desviacion`.
- [ ] Existe `RefrescoService` que orquesta sincronización + resolución + cálculo con un único
  `Refresco`.
- [ ] Existen los endpoints `POST /api/refresco`, `GET /api/eventos`, `GET /api/tendencias` y
  `GET /api/refresco/ultimo`.
- [ ] El filtro `desde`/`hasta` (ET, por defecto mes en curso) y `indice` funcionan.
- [ ] `mvn test` pasa (pruebas unitarias de controladores y `RefrescoService` con Mockito).
- [ ] El backend arranca y los endpoints responden (HTTP 200) con datos o listas vacías.

## Verificación manual (comandos)

```
# backend: compilar y probar
cd backend-funds
./mvnw test

# con el backend arrancado:
curl -X POST http://localhost:8080/api/refresco
curl "http://localhost:8080/api/eventos"
curl "http://localhost:8080/api/tendencias?indice=NAS100"
curl "http://localhost:8080/api/refresco/ultimo"
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- La API respeta la **arquitectura hexagonal** (controladores como adaptadores de entrada, sin
  lógica de negocio).
- Las respuestas son coherentes con el modelo de dominio y con el catálogo de Fase 1.
- CORS ya cubre `/api/**` para el origen del frontend.
- El commit contiene solo los cambios de esta fase (backend: migración, servicios, controladores,
  DTOs y pruebas), sin tocar fases posteriores.
