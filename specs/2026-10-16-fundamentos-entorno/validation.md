# Validación — Fase 0: Fundamentos y entorno

Cómo determinar si la implementación fue exitosa y puede integrarse al código principal.

## Criterios de éxito (Definition of Done)

- [ ] El workspace es un repositorio Git con `main` como rama, remoto `origin` configurado al
  repositorio externo y un commit inicial.
- [ ] Existen `backend-funds/` y `frontend-funds/` como carpetas hermanas de `specs/`.
- [ ] `backend-funds` compila (`mvn package`) sin errores.
- [ ] `/actuator/health` y `GET /api/health` responden `UP` con el backend arrancado.
- [ ] El backend se conecta a PostgreSQL 18.6 levantado por Docker Compose.
- [ ] Flyway aplica la migración inicial sin errores (tabla `flyway_schema_history` presente).
- [ ] `frontend-funds` compila (`ng build`) sin errores y sirve la página de inicio.
- [ ] La página de inicio muestra el estado "conectado" al backend (consume `GET /api/health`).
- [ ] La zona horaria del backend está configurada a America/New_York.
- [ ] El backend sigue **arquitectura hexagonal** (paquetes `domain`/`application`/`infrastructure`).
- [ ] Cada controlador del backend tiene **prueba unitaria con Mockito** y `mvn test` pasa.
- [ ] El frontend sigue **arquitectura Feature-Based** (carpetas `core/`/`shared/`/`features/`).
- [ ] `tech-stack.md` declara ambas arquitecturas como **obligatorias** para cualquier agente.
- [ ] Los archivos `.md` de la Fase 0 (`plan`, `requirements`, `validation`, `roadmap`, `README`)
  están sincronizados con estos requisitos.
- [ ] El `README.md` documenta cómo levantar el entorno completo.

## Verificación manual (comandos)

### Backend

```
cd backend-funds
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/health
```

### Base de datos

```
docker compose up -d
docker compose ps
```

### Frontend

```
cd frontend-funds
npm install
ng serve
# abrir http://localhost:4200 y verificar que muestra "conectado" al backend
```

### Pruebas unitarias

```
# backend (Mockito)
cd backend-funds
./mvnw test          # Windows: mvnw.cmd test

# frontend (Vitest)
cd frontend-funds
ng test --watch=false
```

## Criterios para integrar al principal

- Todos los checks anteriores están marcados.
- El código compila y corre de forma aislada, sin depender de fases futuras.
- El commit contiene solo los cambios de esta fase (backend, frontend, compose, docs, gitignore).
- El remoto `origin` apunta al repositorio externo y el commit se empuja correctamente.
- No se versionan credenciales reales: usar variables de entorno o un `.env` de ejemplo.
