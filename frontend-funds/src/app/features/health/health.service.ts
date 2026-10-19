import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HealthResponse } from './health.model';

/**
 * Servicio de la feature `health`: consulta el estado del backend.
 */
@Injectable({ providedIn: 'root' })
export class HealthService {
  private readonly http = inject(HttpClient);

  check() {
    return this.http.get<HealthResponse>(`${environment.apiUrl}/api/health`);
  }
}
