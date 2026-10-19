import { TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { DashboardComponent } from './dashboard.component';
import { DashboardService } from './dashboard.service';

describe('DashboardComponent', () => {
  let service: {
    consultarEventos: ReturnType<typeof vi.fn>;
    consultarTendencias: ReturnType<typeof vi.fn>;
    ultimoRefresco: ReturnType<typeof vi.fn>;
    refrescar: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    service = {
      consultarEventos: vi.fn().mockReturnValue(of([])),
      consultarTendencias: vi.fn().mockReturnValue(of([])),
      ultimoRefresco: vi.fn().mockReturnValue(of(null)),
      refrescar: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        { provide: DashboardService, useValue: service },
        provideNoopAnimations(),
      ],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(DashboardComponent);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should load events on init', () => {
    TestBed.createComponent(DashboardComponent).detectChanges();
    expect(service.consultarEventos).toHaveBeenCalled();
  });
});
