-- Fase 2: tablas de dominio del sistema de indicadores fundamentales.
-- Las fechas/horas se almacenan en TIMESTAMPTZ (UTC interno).

CREATE TABLE indice (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    sector VARCHAR(50) NOT NULL
);

CREATE TABLE indicador (
    id VARCHAR(60) PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    alias_ff VARCHAR(200) NOT NULL,
    sector VARCHAR(50) NOT NULL,
    impacto VARCHAR(10) NOT NULL,
    frecuencia VARCHAR(100) NOT NULL,
    hora_et VARCHAR(20) NOT NULL,
    fuente_nombre VARCHAR(100) NOT NULL,
    fuente_tipo VARCHAR(20) NOT NULL,
    fuente_url VARCHAR(300) NOT NULL,
    fuente_notas VARCHAR(500),
    regla_direccion VARCHAR(30) NOT NULL,
    regla_peso INTEGER NOT NULL,
    regla_tolerancia_pct NUMERIC NOT NULL
);

CREATE TABLE indicador_indice (
    id BIGSERIAL PRIMARY KEY,
    indicador_id VARCHAR(60) NOT NULL REFERENCES indicador(id),
    indice_codigo VARCHAR(20) NOT NULL REFERENCES indice(codigo),
    relevancia VARCHAR(10) NOT NULL,
    CONSTRAINT uq_indicador_indice UNIQUE (indicador_id, indice_codigo)
);

CREATE INDEX idx_indicador_indice_indice ON indicador_indice (indice_codigo);

CREATE TABLE evento (
    id BIGSERIAL PRIMARY KEY,
    indicador_id VARCHAR(60) NOT NULL REFERENCES indicador(id),
    fecha_hora_utc TIMESTAMPTZ NOT NULL,
    forecast NUMERIC,
    previous NUMERIC,
    CONSTRAINT uq_evento UNIQUE (indicador_id, fecha_hora_utc)
);

CREATE INDEX idx_evento_indicador ON evento (indicador_id);

CREATE TABLE refresco (
    id BIGSERIAL PRIMARY KEY,
    iniciado_en_utc TIMESTAMPTZ NOT NULL,
    finalizado_en_utc TIMESTAMPTZ,
    estado VARCHAR(20) NOT NULL,
    detalle VARCHAR(500)
);

CREATE TABLE resultado (
    id BIGSERIAL PRIMARY KEY,
    evento_id BIGINT NOT NULL REFERENCES evento(id),
    refresco_id BIGINT NOT NULL REFERENCES refresco(id),
    actual NUMERIC,
    CONSTRAINT uq_resultado UNIQUE (evento_id, refresco_id)
);

CREATE INDEX idx_resultado_evento ON resultado (evento_id);
CREATE INDEX idx_resultado_refresco ON resultado (refresco_id);

CREATE TABLE tendencia_diaria (
    id BIGSERIAL PRIMARY KEY,
    indice_codigo VARCHAR(20) NOT NULL REFERENCES indice(codigo),
    fecha DATE NOT NULL,
    puntaje NUMERIC NOT NULL,
    clasificacion VARCHAR(20) NOT NULL,
    CONSTRAINT uq_tendencia UNIQUE (indice_codigo, fecha)
);
