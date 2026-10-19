import { Component, OnInit, signal } from '@angular/core';
import { HealthService } from './features/health/health.service';

type ConnectionState = 'loading' | 'connected' | 'error';

@Component({
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App implements OnInit {
  protected readonly title = 'IndicadoresFundamentales';
  protected readonly state = signal<ConnectionState>('loading');
  protected readonly serviceName = signal('');

  constructor(private readonly health: HealthService) {}

  ngOnInit(): void {
    this.health.check().subscribe({
      next: (res) => {
        this.serviceName.set(res.service);
        this.state.set('connected');
      },
      error: () => this.state.set('error'),
    });
  }
}
