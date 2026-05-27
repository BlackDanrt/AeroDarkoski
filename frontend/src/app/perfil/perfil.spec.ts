import 'zone.js';
import 'zone.js/testing';
import { ComponentFixture, TestBed, fakeAsync, tick, flush } from '@angular/core/testing';
import { vi, describe, beforeEach, it, expect } from 'vitest';
import { Perfil } from './perfil';
import { UsuarioService } from '../services/usuario-service';
import { JwtService } from '../services/jwt-service';
import { Router } from '@angular/router';
import { ToastService } from '../services/toast-service';
import { of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('Perfil', () => {
  let component: Perfil;
  let fixture: ComponentFixture<Perfil>;
  let mockUsuarioService: any;
  let mockJwtService: any;
  let mockRouter: any;
  let mockToastService: any;

  beforeEach(async () => {
    mockUsuarioService = {
      getById: vi.fn(),
      actualizar: vi.fn(),
    };
    mockJwtService = {
      getIdUsuario: vi.fn(),
      setToken: vi.fn(),
      removerToken: vi.fn(),
    };
    mockRouter = {
      navigate: vi.fn(),
    };
    mockToastService = {
      mostrar: vi.fn(),
    };

    mockJwtService.getIdUsuario.mockReturnValue(1);
    mockUsuarioService.getById.mockReturnValue(
      of({ id: 1, nombreUsuario: 'test', correo: 'test@test.com', rol: 'USER' } as any),
    );

    await TestBed.configureTestingModule({
      declarations: [Perfil],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: UsuarioService, useValue: mockUsuarioService },
        { provide: JwtService, useValue: mockJwtService },
        { provide: Router, useValue: mockRouter },
        { provide: ToastService, useValue: mockToastService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Perfil);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  describe('cargarDatos', () => {
    it('should redirect to login if no user ID is found', async () => {
      vi.useFakeTimers();
      mockJwtService.getIdUsuario.mockReturnValue(null);

      component.cargarDatos();

      expect(component.mensajeError).toBe('No se encontró sesión activa o el token expiró');
      expect(component.cargando).toBe(false);

      await vi.advanceTimersByTimeAsync(2000);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
      vi.useRealTimers();
    });

    it('should load user data successfully', () => {
      mockJwtService.getIdUsuario.mockReturnValue(1);

      component.cargarDatos();

      expect(component.cargando).toBe(false);
      expect(component.usuario.nombreUsuario).toBe('test');
      expect(component.usuario.correo).toBe('test@test.com');
    });

    it('should show error if user payload is null', () => {
      mockJwtService.getIdUsuario.mockReturnValue(1);
      mockUsuarioService.getById.mockReturnValue(of(null as any));

      component.cargarDatos();

      expect(component.mensajeError).toBe('No se encontró el usuario');
    });

    it('should handle 403 error on load', async () => {
      vi.useFakeTimers();
      mockJwtService.getIdUsuario.mockReturnValue(1);
      mockUsuarioService.getById.mockReturnValue(throwError(() => ({ status: 403 })));

      component.cargarDatos();

      expect(component.mensajeError).toBe('Sesión expirada. Por favor, inicie sesión nuevamente.');
      expect(mockJwtService.removerToken).toHaveBeenCalled();

      await vi.advanceTimersByTimeAsync(2000);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
      vi.useRealTimers();
    });

    it('should handle generic error on load', async () => {
      vi.useFakeTimers();
      mockJwtService.getIdUsuario.mockReturnValue(1);
      mockUsuarioService.getById.mockReturnValue(throwError(() => ({ status: 500 })));

      component.cargarDatos();

      expect(component.mensajeError).toBe('Error al cargar el perfil. Intente nuevamente.');

      await vi.advanceTimersByTimeAsync(2000);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
      vi.useRealTimers();
    });

  });

  describe('guardarCambios', () => {
    it('should not proceed if passwords do not match', () => {
      component.usuario.contrasenia = '12345678';
      component.confirmarContrasenia = '87654321';

      component.guardarCambios();

      expect(mockToastService.mostrar).toHaveBeenCalledWith('Las contraseñas no coinciden', false);
      expect(component.guardando).toBe(false);
    });

    it('should not proceed if password is less than 8 characters', () => {
      component.usuario.contrasenia = '123';
      component.confirmarContrasenia = '123';

      component.guardarCambios();

      expect(mockToastService.mostrar).toHaveBeenCalledWith(
        'La contraseña debe tener al menos 8 caracteres',
        false,
      );
      expect(component.guardando).toBe(false);
    });

    it('should save updates successfully and set new token', () => {
      component.usuario = { id: 1, nombreUsuario: 'new', correo: 'new@test.com' };
      mockUsuarioService.actualizar.mockReturnValue(of('newTokenString'));

      component.guardarCambios();

      expect(component.guardando).toBe(false);
      expect(mockJwtService.setToken).toHaveBeenCalledWith('newTokenString');
      expect(mockToastService.mostrar).toHaveBeenCalledWith(
        'Cambios guardados correctamente',
        true,
      );
      expect(component.usuario.contrasenia).toBe('');
      expect(component.confirmarContrasenia).toBe('');
    });

    it('should handle 403 error on update', async () => {
      component.usuario = { id: 1, nombreUsuario: 'new', correo: 'new@test.com' };
      mockUsuarioService.actualizar.mockReturnValue(throwError(() => ({ status: 403 })));

      vi.useFakeTimers();
      component.guardarCambios();

      await Promise.resolve();
      await Promise.resolve();
      await Promise.resolve();

      expect(mockToastService.mostrar).toHaveBeenCalledWith(
        'Sesión expirada. Inicie sesión nuevamente',
        false,
      );
      expect(localStorage.getItem('token')).toBeNull();

      await vi.advanceTimersByTimeAsync(2000);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
      vi.useRealTimers();
    });

    it('should handle generic error on update', () => {
      component.usuario = { id: 1, nombreUsuario: 'new', correo: 'new@test.com' };
      mockUsuarioService.actualizar.mockReturnValue(throwError(() => ({ status: 500 })));

      component.guardarCambios();

      expect(mockToastService.mostrar).toHaveBeenCalledWith('Error al guardar los cambios', false);
    });
  });

  describe('cancelar', () => {
    it('should navigate back to principal route', () => {
      component.cancelar();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/principal']);
    });
  });

});
