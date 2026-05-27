import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi, describe, beforeEach, it, expect } from 'vitest';
import { Busqueda } from './busqueda';
import { AvionService } from '../services/avion-service';
import { ToastService } from '../services/toast-service';
import { HistorialService } from '../services/historial-service';
import { JwtService } from '../services/jwt-service';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { TipoBusqueda } from '../enums/tipo-busqueda';
import { Avion } from '../models/avion';

describe('Busqueda', () => {
  let component: Busqueda;
  let fixture: ComponentFixture<Busqueda>;


  let mockAvionService: any;
  let mockToastService: any;
  let mockHistorialService: any;
  let mockJwtService: any;

  let urlCodigoSimulado = 'ABC';
  let urlTipoSimulado = '1';

  beforeEach(async () => {

    mockAvionService = {
      buscarPorArrIata: vi.fn(),
      buscarPorArrIcao: vi.fn(),
      buscarPorFlightIata: vi.fn(),
      buscarPorFlightIcao: vi.fn(),
    };
    mockToastService = {
      mostrar: vi.fn(),
    };
    mockHistorialService = {
      crear: vi.fn(),
    };
    mockJwtService = {
      getIdUsuario: vi.fn(),
    };


    mockAvionService.buscarPorArrIata.mockReturnValue(of([]));
    mockHistorialService.crear.mockReturnValue(of('success'));
    mockJwtService.getIdUsuario.mockReturnValue(123);

    await TestBed.configureTestingModule({
      declarations: [Busqueda],
      providers: [
        { provide: AvionService, useValue: mockAvionService },
        { provide: ToastService, useValue: mockToastService },
        { provide: HistorialService, useValue: mockHistorialService },
        { provide: JwtService, useValue: mockJwtService },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => {
                  if (key === 'codigo') return urlCodigoSimulado;
                  if (key === 'tipo') return urlTipoSimulado;
                  return null;
                },
              },
            },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Busqueda);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    urlCodigoSimulado = 'ABC';
    urlTipoSimulado = '1';
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  describe('definirTipoBusqueda', () => {
    it('should return IATA if tipoBusqueda is 1 and length is 3', () => {
      expect(component.definirTipoBusqueda('ABC', 1)).toBe(TipoBusqueda.IATA);
    });

    it('should return ICAO if tipoBusqueda is 1 and length is not 3', () => {
      expect(component.definirTipoBusqueda('ABCD', 1)).toBe(TipoBusqueda.ICAO);
    });

    it('should return FLIGHT_IATA if tipoBusqueda is not 1 and length is 5', () => {
      expect(component.definirTipoBusqueda('12345', 2)).toBe(TipoBusqueda.FLIGHT_IATA);
    });

    it('should return FLIGHT_ICAO if tipoBusqueda is not 1 and length is not 5', () => {
      expect(component.definirTipoBusqueda('123456', 2)).toBe(TipoBusqueda.FLIGHT_ICAO);
    });
  });

  describe('cargarAviones', () => {
    it('should load planes successfully', () => {
      const data: Avion[] = [{ id: 1 } as any];
      mockAvionService.buscarPorArrIata.mockReturnValue(of(data));

      component.cargarAviones('ABC', TipoBusqueda.IATA);


      expect(component.cargando).toBe(false);
      expect(component.aviones).toEqual(data);
      expect(component.cargaExitosa).toBe(true);
    });

    it('should handle empty response', () => {
      mockAvionService.buscarPorArrIata.mockReturnValue(of([]));

      component.cargarAviones('ABC', TipoBusqueda.IATA);

      expect(component.cargando).toBe(false);
      expect(component.mensajeError).toBe(
        'No se han encontrado aviones que coincidan con tu busqueda',
      );
      expect(component.cargaExitosa).toBe(false);
    });

    it('should handle service error', () => {
      mockAvionService.buscarPorArrIata.mockReturnValue(throwError(() => new Error('Error')));

      component.cargarAviones('ABC', TipoBusqueda.IATA);

      expect(mockToastService.mostrar).toHaveBeenCalledWith('Error al realizar la busqueda', false);
    });
  });

  describe('guardarHistorial', () => {
    it('should not save if user ID is null', () => {
      mockJwtService.getIdUsuario.mockReturnValue(null);

      component.guardarHistorial('ABC', TipoBusqueda.IATA);

      expect(mockHistorialService.crear).not.toHaveBeenCalled();
    });

    it('should save history on success', () => {
      mockJwtService.getIdUsuario.mockReturnValue(77);
      mockHistorialService.crear.mockReturnValue(of('success'));

      component.guardarHistorial('ABC', TipoBusqueda.IATA);

      expect(mockHistorialService.crear).toHaveBeenCalledWith({
        idUsuario: 77,
        busqueda: 'ABC',
        tipoBusqueda: TipoBusqueda.IATA,
      });
      expect(mockToastService.mostrar).toHaveBeenCalledWith(
        'La busqueda ha sido agregada a tu historial exitosamente',
        true
      );
    });

    it('should handle error on save history', () => {
      mockJwtService.getIdUsuario.mockReturnValue(77);
      mockHistorialService.crear.mockReturnValue(throwError(() => new Error('Error')));

      component.guardarHistorial('ABC', TipoBusqueda.IATA);

      expect(mockToastService.mostrar).toHaveBeenCalledWith(
        'Error al agregar tu busqueda a tu historial',
        false,
      );
    });
  });
});
