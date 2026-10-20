import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DashboardService } from './dashboard.service';
import {
  ClasificacionTendencia,
  EventoDto,
  IndicadorDto,
  RefrescoDto,
  TendenciaDto,
} from './dashboard.model';

const ET = 'America/New_York';

/**
 * Dashboard: resultados por indicador (agrupados por impacto) y tendencia diaria por índice,
 * en los últimos 20 días calendario.
 */
@Component({
  selector: 'app-dashboard',
  imports: [MatCardModule, MatButtonModule, MatProgressSpinnerModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  private readonly service = inject(DashboardService);

  readonly indicadores = signal<IndicadorDto[]>([]);
  readonly eventos = signal<EventoDto[]>([]);
  readonly tendencias = signal<TendenciaDto[]>([]);
  readonly ultimoRefresco = signal<RefrescoDto | null>(null);
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);

  readonly fechas = computed(() => this.ultimos20Dias());

  readonly altoImpacto = computed(() => this.indicadores().filter((i) => i.impacto === 'ALTO'));
  readonly medioImpacto = computed(() => this.indicadores().filter((i) => i.impacto === 'MEDIO'));

  readonly tendenciaFilas = computed(() => {
    const fechas = this.fechas();
    return (['NAS100', 'SP500'] as const).map((indice) => ({
      indice,
      celdas: fechas.map((f) => this.clasificacionDe(indice, f)),
    }));
  });

  constructor() {
    this.cargar();
  }

  actualizar(): void {
    this.error.set(null);
    this.cargando.set(true);
    this.service.refrescar().subscribe({
      next: () => this.cargar(),
      error: () => {
        this.error.set('No se pudo ejecutar el refresco');
        this.cargando.set(false);
      },
    });
  }

  valorDe(indicadorId: string, fecha: string): number | null {
    const evento = this.eventos().find(
      (e) => e.indicadorId === indicadorId && this.fechaEt(e.fechaHoraUtc) === fecha,
    );
    return evento ? evento.actual : null;
  }

  claseTendencia(clasificacion: ClasificacionTendencia | null): string {
    switch (clasificacion) {
      case 'ALCISTA':
        return 'celda-alcista';
      case 'BAJISTA':
        return 'celda-bajista';
      case 'NEUTRAL':
        return 'celda-neutral';
      default:
        return 'celda-vacia';
    }
  }

  formatearSiguiente(fecha: string | null): string {
    if (!fecha) {
      return '—';
    }
    const [y, m, d] = fecha.split('-');
    return `${d}/${m}/${y}`;
  }

  formatoDia(fecha: string): string {
    const [, m, d] = fecha.split('-');
    return `${d}/${m}`;
  }

  formatearValor(valor: number | null): string {
    if (valor === null || valor === undefined) {
      return '';
    }
    if (Number.isInteger(valor) && Math.abs(valor) >= 1000) {
      return valor.toLocaleString('en-US');
    }
    return String(valor);
  }

  private cargar(): void {
    this.cargando.set(true);
    this.error.set(null);
    const fechas = this.fechas();
    const desde = fechas[0];
    const hasta = fechas[fechas.length - 1];

    forkJoin([
      this.service.consultarIndicadores(),
      this.service.consultarEventos(desde, hasta),
      this.service.consultarTendencias(desde, hasta),
      this.service.ultimoRefresco(),
    ]).subscribe({
      next: ([indicadores, eventos, tendencias, ultimo]) => {
        this.indicadores.set(indicadores);
        this.eventos.set(eventos);
        this.tendencias.set(tendencias);
        this.ultimoRefresco.set(ultimo);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo consultar el backend');
        this.cargando.set(false);
      },
    });
  }

  private clasificacionDe(indice: string, fecha: string): ClasificacionTendencia | null {
    const t = this.tendencias().find((x) => x.indiceCodigo === indice && x.fecha === fecha);
    return t ? t.clasificacion : null;
  }

  private ultimos20Dias(): string[] {
    const lista: string[] = [];
    const hoy = this.hoyEt();
    const fin = new Date(`${hoy}T00:00:00`);
    for (let i = 19; i >= 0; i--) {
      const d = new Date(fin);
      d.setDate(d.getDate() - i);
      lista.push(this.formatoLocal(d));
    }
    return lista;
  }

  private formatoLocal(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const dia = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${dia}`;
  }

  private fechaEt(instant: string): string {
    return new Intl.DateTimeFormat('en-CA', { timeZone: ET }).format(new Date(instant));
  }

  private hoyEt(): string {
    return new Intl.DateTimeFormat('en-CA', { timeZone: ET }).format(new Date());
  }
}
