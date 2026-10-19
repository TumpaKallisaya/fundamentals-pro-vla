export type EstadoEvento = 'VERDE' | 'AMARILLO' | 'ROJO' | 'PENDIENTE';
export type ClasificacionTendencia = 'ALCISTA' | 'NEUTRAL' | 'BAJISTA';

/** Evento con su resultado y clasificación (espejo del backend). */
export interface EventoDto {
  id: number;
  indicadorId: string;
  nombre: string;
  fechaHoraUtc: string;
  forecast: number | null;
  previous: number | null;
  actual: number | null;
  estado: EstadoEvento;
  desviacion: number | null;
}

/** Tendencia diaria por índice (espejo del backend). */
export interface TendenciaDto {
  indiceCodigo: string;
  fecha: string;
  puntaje: number;
  clasificacion: ClasificacionTendencia;
}

/** Estado de un refresco (espejo del backend). */
export interface RefrescoDto {
  id: number;
  estado: string;
  detalle: string | null;
  iniciadoEnUtc: string;
  finalizadoEnUtc: string | null;
}
