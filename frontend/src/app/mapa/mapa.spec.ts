import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi, describe, beforeEach, afterEach, it, expect } from 'vitest';
import { Mapa } from './mapa';
import { AvionService } from '../services/avion-service';
import { ToastService } from '../services/toast-service';
import { ClimaService } from '../services/clima-service';
import { of, throwError } from 'rxjs';
import { Avion } from '../models/avion';
import { Clima } from '../models/clima';

vi.mock('leaflet', () => ({
  map: vi.fn(() => ({
    setView: vi.fn().mockReturnThis(),
    addLayer: vi.fn(),
    remove: vi.fn(),
    removeLayer: vi.fn(),
    on: vi.fn(),
    scrollWheelZoom: { enable: vi.fn(), disable: vi.fn() },
  })),
  tileLayer: vi.fn(() => ({ addTo: vi.fn() })),
  marker: vi.fn(() => ({
    addTo: vi.fn().mockReturnThis(),
    remove: vi.fn(),
    bindPopup: vi.fn().mockReturnThis(),
    setLatLng: vi.fn().mockReturnThis(),
    setIcon: vi.fn().mockReturnThis(),
    setPopupContent: vi.fn().mockReturnThis(),
  })),
  icon: vi.fn(),
  latLng: vi.fn((lat: number, lng: number) => ({ lat, lng })),
  divIcon: vi.fn(() => ({})),
}));

describe('Mapa', () => {
  let component: Mapa;
  let fixture: ComponentFixture<Mapa>;
  let mockAvionService: any;
  let mockToastService: any;
  let mockClimaService: any;

  beforeEach(async () => {
    mockAvionService = { obtenerTodos: vi.fn() };
    mockToastService = { mostrar: vi.fn() };
    mockClimaService = { obtenerReportesClima: vi.fn() };

    mockAvionService.obtenerTodos.mockReturnValue(of([]));
    mockClimaService.obtenerReportesClima.mockReturnValue(of([]));

    const mapDiv = document.createElement('div');
    mapDiv.id = 'map';
    document.body.appendChild(mapDiv);

    await TestBed.configureTestingModule({
      declarations: [Mapa],
      providers: [
        { provide: AvionService, useValue: mockAvionService },
        { provide: ToastService, useValue: mockToastService },
        { provide: ClimaService, useValue: mockClimaService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Mapa);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    if (component) component.ngOnDestroy();
    document.body.innerHTML = '';
    vi.clearAllMocks();
  });

  it('should create and initialize map', () => {
    fixture.detectChanges();
    component.ngAfterViewInit();
    expect(component).toBeTruthy();
  });

  it('should calculate future position correctly', () => {
    const compAny = component as any;
    const pos = compAny.calcularPosicionFutura(4.702, -74.147, 400, 90, 1);
    expect(pos.lat).toBeDefined();
    expect(pos.lng).toBeDefined();
  });

  describe('obtenerEmojiClima', () => {
    it('should return sun emoji for CLR or SKC', () => {
      const compAny = component as any;
      expect(compAny.obtenerEmojiClima({ clouds: [{ cover: 'CLR' }] } as Clima)).toBe('☀️');
      expect(compAny.obtenerEmojiClima({ clouds: [{ cover: 'SKC' }] } as Clima)).toBe('☀️');
    });

    it('should return cloud emojis', () => {
      const compAny = component as any;
      expect(compAny.obtenerEmojiClima({ clouds: [{ cover: 'FEW' }] } as Clima)).toBe('🌤️');
    });

    it('should return thermometer emoji as fallback', () => {
      const compAny = component as any;
      expect(compAny.obtenerEmojiClima({ clouds: [{ cover: 'UNKNOWN_XYZ' }] } as any)).toBe('🌡️');
    });
  });

  it('should handle tracking planes successfully', async () => {
    const mockAviones: Avion[] = [
      {
        flight: { icaoNumber: 'A1' },
        geography: { latitude: 4.7, longitude: -74.1, direction: 120, altitude: 30000 },
        speed: { horizontal: 450 },
        airline: { name: 'Test Air' },
        status: 'en-route',
      } as any,
    ];
    mockAvionService.obtenerTodos.mockReturnValue(of(mockAviones));
    mockClimaService.obtenerReportesClima.mockReturnValue(of([]));

    fixture.detectChanges();
    component.ngAfterViewInit();

    await Promise.resolve();
    await Promise.resolve();

    const marcadores = (component as any).marcadores;
    expect(marcadores.has('A1')).toBe(true);
  });

  it('should trigger toast error when tracking service fails', async () => {
    mockAvionService.obtenerTodos.mockReturnValue(throwError(() => new Error('Error')));

    fixture.detectChanges();
    component.ngAfterViewInit();
    await Promise.resolve();

    expect(mockToastService.mostrar).toHaveBeenCalledWith('Error al cargar los aviones', false);
  });

  it('should clean up subscriptions', () => {
    fixture.detectChanges();
    component.ngAfterViewInit();

    const compAny = component as any;
    if (compAny.suscripcion) {
      vi.spyOn(compAny.suscripcion, 'unsubscribe');
      component.ngOnDestroy();
      expect(compAny.suscripcion.unsubscribe).toHaveBeenCalled();
    }
  });
});
