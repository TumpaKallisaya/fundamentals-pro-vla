import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { DashboardService } from './dashboard.service';
import {
  ClasificacionTendencia,
  EstadoEvento,
  EventoDto,
  RefrescoDto,
  TendenciaDto,
} from './dashboard.model';

const ET = 'America/New_York';

/**
 * Dashboard: semáforo mensual por indicador y tendencia diaria por índice.
 */
@Component({
  selector: 'app-dashboard',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatSelectModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  private readonly service = inject(DashboardService);

  readonly desde = signal<string>(this.primerDiaMesEt());
  readonly hasta = signal<string>(this.ultimoDiaMesEt());
  readonly indice = signal<string>('NAS100');

  readonly eventos = signal<EventoDto[]>([]);
  readonly tendencias = signal<TendenciaDto[]>([]);
  readonly ultimoRefresco = signal<RefrescoDto | null>(null);
  readonly cargando = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  readonly fechas = computed(() => this.rangoFechas(this.desde(), this.hasta()));

  private readonly indicadores = computed(() => {
    const mapa = new Map<string, string>();
    for (const e of this.eventos()) {
      if (!mapa.has(e.indicadorId)) {
        mapa.set(e.indicadorId, e.nombre);
      }
    }
    return [...mapa.entries()].map(([id, nombre]) => ({ id, nombre }));
  });

  readonly semaforoFilas = computed(() => {
    const fechas = this.fechas();
    return this.indicadores().map(({ id, nombre }) => ({
      nombre,
      celdas: fechas.map((f) => this.estadoDe(id, f)),
    }));
  });

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

  cambiarDesde(valor: string): void {
    this.desde.set(valor);
    this.cargar();
  }

  cambiarHasta(valor: string): void {
    this.hasta.set(valor);
    this.cargar();
  }

  cambiarIndice(valor: string): void {
    this.indice.set(valor);
    this.cargar();
  }

  claseEstado(estado: EstadoEvento | null): string {
    switch (estado) {
      case 'VERDE':
        return 'celda-verde';
      case 'AMARILLO':
        return 'celda-amarillo';
      case 'ROJO':
        return 'celda-rojo';
      default:
        return 'celda-gris';
    }
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

  simboloTendencia(clasificacion: ClasificacionTendencia | null): string {
    switch (clasificacion) {
      case 'ALCISTA':
        return '▲';
      case 'BAJISTA':
        return '▼';
      case 'NEUTRAL':
        return '—';
      default:
        return '';
    }
  }

  private cargar(): void {
    this.cargando.set(true);
    this.error.set(null);
    const desde = this.desde();
    const hasta = this.hasta();
    const indice = this.indice();

    forkJoin([
      this.service.consultarEventos(desde, hasta, indice),
      this.service.consultarTendencias(desde, hasta),
      this.service.ultimoRefresco(),
    ]).subscribe({
      next: ([eventos, tendencias, ultimo]) => {
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

  private estadoDe(indicadorId: string, fecha: string): EstadoEvento | null {
    const evento = this.eventos().find(
      (e) => e.indicadorId === indicadorId && this.fechaEt(e.fechaHoraUtc) === fecha,
    );
    return evento ? evento.estado : null;
  }

  private clasificacionDe(indice: string, fecha: string): ClasificacionTendencia | null {
    const t = this.tendencias().find((x) => x.indiceCodigo === indice && x.fecha === fecha);
    return t ? t.clasificacion : null;
  }

  private rangoFechas(desde: string, hasta: string): string[] {
    const lista: string[] = [];
    const inicio = new Date(`${desde}T00:00:00`);
    const fin = new Date(`${hasta}T00:00:00`);
    const d = new Date(inicio);
    while (d <= fin) {
      lista.push(this.formatearLocal(d));
      d.setDate(d.getDate() + 1);
    }
    return lista;
  }

  private formatearLocal(d: Date): string {
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

  private primerDiaMesEt(): string {
    return `${this.hoyEt().slice(0, 8)}01`;
  }

  private ultimoDiaMesEt(): string {
    const [y, m] = this.hoyEt().split('-');
    const ultimoDia = new Date(Number(y), Number(m), 0).getDate();
    return `${y}-${m}-${String(ultimoDia).padStart(2, '0')}`;
  }
}
