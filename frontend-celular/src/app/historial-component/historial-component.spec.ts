import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi, describe, beforeEach, it, expect } from 'vitest';
import { HistorialComponent } from './historial-component';
import { HistorialService } from '../services/historial-service';
import { JwtService } from '../services/jwt-service';
import { ToastService } from '../services/toast-service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { Historial } from '../models/historial';
import { TipoBusqueda } from '../enums/tipo-busqueda';

describe('HistorialComponent', () => {
  let component: HistorialComponent;
  let fixture: ComponentFixture<HistorialComponent>;
  let mockHistorialService: any;
  let mockJwtService: any;
  let mockToastService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockHistorialService = {
      mostrar: vi.fn(),
    };
    mockJwtService = {
      getNombreUsuario: vi.fn(),
      getIdUsuario: vi.fn(),
      removerToken: vi.fn(),
    };
    mockToastService = {
      mostrar: vi.fn(),
    };
    mockRouter = {
      navigate: vi.fn(),
    };

    mockJwtService.getNombreUsuario.mockReturnValue('Usuario Prueba');
    mockJwtService.getIdUsuario.mockReturnValue(123);
    mockHistorialService.mostrar.mockReturnValue(of(new HttpResponse<Historial[]>({ body: [] })));

    await TestBed.configureTestingModule({
      declarations: [HistorialComponent],
      providers: [
        { provide: HistorialService, useValue: mockHistorialService },
        { provide: JwtService, useValue: mockJwtService },
        { provide: ToastService, useValue: mockToastService },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HistorialComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.nombreUsuario).toBe('Usuario Prueba');
  });

  describe('ngOnInit', () => {
    it('should load history if idUsuario is present', () => {
      mockJwtService.getIdUsuario.mockReturnValue(123);
      vi.spyOn(component, 'cargarHistorial');

      fixture.detectChanges();

      expect(component.cargarHistorial).toHaveBeenCalledWith(123);
    });

    it('should handle null user ID by clearing session and redirecting', () => {
      mockJwtService.getIdUsuario.mockReturnValue(null);

      fixture.detectChanges();

      expect(mockToastService.mostrar).toHaveBeenCalledWith(
        'Error al cargar el historial de usuario',
        false,
      );
      expect(mockJwtService.removerToken).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['login']);
    });
  });

  describe('cargarHistorial', () => {
    it('should set historiales list reversed on success', () => {
      const data: Historial[] = [
        { idUsuario: 123, busqueda: 'A', tipoBusqueda: TipoBusqueda.IATA },
        { idUsuario: 456, busqueda: 'B', tipoBusqueda: TipoBusqueda.ICAO },
      ];
      mockHistorialService.mostrar.mockReturnValue(of(new HttpResponse({ body: data })));

      component.cargarHistorial(123);

      expect(component.cargando).toBe(false);
      expect(component.historiales[0].busqueda).toBe('B');
      expect(component.historiales[1].busqueda).toBe('A');
    });

    it('should clear list and set error message on service failure', () => {
      mockHistorialService.mostrar.mockReturnValue(throwError(() => new Error('Error')));

      component.cargarHistorial(123);

      expect(component.cargando).toBe(false);
      expect(component.mensajeError).toBe('Error al cargar los historiales, intente nuevamente');
      expect(component.historiales.length).toBe(0);
    });
  });

  describe('definirValor', () => {
    it('should return 1 for IATA or ICAO', () => {
      expect(component.definirValor('IATA' as any)).toBe(1);
      expect(component.definirValor('ICAO' as any)).toBe(1);
    });

    it('should return 2 for other search types', () => {
      expect(component.definirValor('FLIGHT_IATA' as any)).toBe(2);
    });
  });

  describe('buscar', () => {
    it('should navigate to search view with formatted parameters', () => {
      const item: Historial = { idUsuario: 123, busqueda: 'BOG', tipoBusqueda: TipoBusqueda.IATA };

      component.buscar(item);

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/busqueda/BOG/1']);
    });
  });
});
