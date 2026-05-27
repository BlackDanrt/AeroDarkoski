import { ComponentFixture, TestBed, fakeAsync, tick, flush } from '@angular/core/testing';
import { vi, describe, beforeEach, it, expect } from 'vitest';
import { Registro } from './registro';
import { AuthService } from '../services/auth-service';
import { Router } from '@angular/router';
import { ToastService } from '../services/toast-service';
import { of, throwError } from 'rxjs';
import 'zone.js';
import 'zone.js/testing';

describe('Registro', () => {
  let component: Registro;
  let fixture: ComponentFixture<Registro>;
  let mockAuthService: any;
  let mockRouter: any;
  let mockToastService: any;

  beforeEach(async () => {
    mockAuthService = {
      registrar: vi.fn(),
    };
    mockRouter = {
      navigate: vi.fn(),
    };
    mockToastService = {
      mostrar: vi.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [Registro],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: ToastService, useValue: mockToastService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Registro);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not proceed if already loading', () => {
    component.cargando = true;
    component.registrarse();
    expect(mockAuthService.registrar).not.toHaveBeenCalled();
  });

  it('should fail validation if passwords do not match', () => {
    component.usuario = { nombreUsuario: 'test', correo: 'a@a.com', contrasenia: '12345678' };
    component.confirmarContrasenia = '87654321';
    component.confirmarCorreo = 'a@a.com';

    component.registrarse();

    expect(component.cargando).toBe(false);
    expect(mockToastService.mostrar).toHaveBeenCalledWith('Las contraseñas no coinciden', false);
  });

  it('should fail validation if emails do not match', () => {
    component.usuario = { nombreUsuario: 'test', correo: 'a@a.com', contrasenia: '12345678' };
    component.confirmarContrasenia = '12345678';
    component.confirmarCorreo = 'b@b.com';

    component.registrarse();

    expect(component.cargando).toBe(false);
    expect(mockToastService.mostrar).toHaveBeenCalledWith(
      'Las direcciones de corrreo no coinciden',
      false,
    );
  });

  it('should fail validation if password is too short', () => {
    component.usuario = { nombreUsuario: 'test', correo: 'a@a.com', contrasenia: '123' };
    component.confirmarContrasenia = '123';
    component.confirmarCorreo = 'a@a.com';

    component.registrarse();

    expect(component.cargando).toBe(false);
    expect(mockToastService.mostrar).toHaveBeenCalledWith(
      'La contraseña debe tener al menos 8 caracteres',
      false,
    );
  });

  it('should register successfully and navigate after 2s', async () => {
    vi.useFakeTimers();
    mockAuthService.registrar.mockReturnValue(of('Usuario registrado'));
    component.usuario = { nombreUsuario: 'test', correo: 'a@a.com', contrasenia: '12345678' };
    component.confirmarContrasenia = '12345678';
    component.confirmarCorreo = 'a@a.com';

    component.registrarse();

    expect(component.cargando).toBe(false);
    expect(mockToastService.mostrar).toHaveBeenCalledWith('Usuario registrado', true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();

    await vi.advanceTimersByTimeAsync(2000);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    vi.useRealTimers();
  });

  it('should handle server fallback error if err.error is ProgressEvent or not a string', () => {
    mockAuthService.registrar.mockReturnValue(
      throwError(() => ({ error: new ProgressEvent('error') })),
    );
    component.usuario = { nombreUsuario: 'test', correo: 'a@a.com', contrasenia: '12345678' };
    component.confirmarContrasenia = '12345678';
    component.confirmarCorreo = 'a@a.com';

    component.registrarse();

    expect(component.cargando).toBe(false);
    expect(mockToastService.mostrar).toHaveBeenCalledWith('Error interno del servidor', false);
  });

  it('should handle specific text message error from backend', () => {
    mockAuthService.registrar.mockReturnValue(
      throwError(() => ({ error: 'El usuario ya existe' })),
    );
    component.usuario = { nombreUsuario: 'test', correo: 'a@a.com', contrasenia: '12345678' };
    component.confirmarContrasenia = '12345678';
    component.confirmarCorreo = 'a@a.com';

    component.registrarse();

    expect(component.cargando).toBe(false);
    expect(mockToastService.mostrar).toHaveBeenCalledWith('El usuario ya existe', false);
  });
});
