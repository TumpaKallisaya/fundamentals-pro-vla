-- Fase 5: persistir la clasificación del resultado.
ALTER TABLE resultado ADD COLUMN estado VARCHAR(20);
ALTER TABLE resultado ADD COLUMN desviacion NUMERIC;
