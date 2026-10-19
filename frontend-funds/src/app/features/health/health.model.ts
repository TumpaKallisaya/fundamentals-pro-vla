/**
 * Modelos de la feature `health`.
 */

/** Respuesta del endpoint de salud del backend (`GET /api/health`). */
export interface HealthResponse {
  service: string;
  status: string;
  timestamp: string;
}
