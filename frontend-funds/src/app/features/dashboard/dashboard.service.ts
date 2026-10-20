import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { catchError, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EventoDto, IndicadorDto, RefrescoDto, TendenciaDto } from './dashboard.model';

/**
 * Servicio de la feature `dashboard`: consume la API REST del backend.
 */
@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.apiUrl;

  refrescar() {
    return this.http.post<RefrescoDto>(`${this.base}/api/refresco`, {});
  }

  consultarIndicadores() {
    return this.http.get<IndicadorDto[]>(`${this.base}/api/indicadores`);
  }

  consultarEventos(desde: string, hasta: string) {
    const params = new HttpParams().set('desde', desde).set('hasta', hasta);
    return this.http.get<EventoDto[]>(`${this.base}/api/eventos`, { params });
  }

  consultarTendencias(desde: string, hasta: string) {
    const params = new HttpParams().set('desde', desde).set('hasta', hasta);
    return this.http.get<TendenciaDto[]>(`${this.base}/api/tendencias`, { params });
  }

  ultimoRefresco() {
    return this.http.get<RefrescoDto>(`${this.base}/api/refresco/ultimo`).pipe(
      catchError(() => of(null)),
    );
  }
}
