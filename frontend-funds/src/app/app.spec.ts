import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { App } from './app';
import { HealthService } from './features/health/health.service';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        {
          provide: HealthService,
          useValue: { check: () => of({ service: 'backend-funds', status: 'UP', timestamp: '' }) },
        },
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render title', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('IndicadoresFundamentales');
  });
});
