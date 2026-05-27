import { ComponentFixture, TestBed, fakeAsync, tick, flush } from '@angular/core/testing';
import { vi, describe, beforeEach, afterEach, it, expect } from 'vitest';
import { Login } from './login';
import { AuthService } from '../services/auth-service';
import { Router } from '@angular/router';
import { ToastService } from '../services/toast-service';
import { of, throwError } from 'rxjs';
import 'zone.js';
import 'zone.js/testing';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let mockAuthService: any;
  let mockRouter: any;
  let mockToastService: any;

  beforeEach(async () => {
    mockAuthService = { logIn: vi.fn() };
    mockRouter = { navigate: vi.fn() };
    mockToastService = { mostrar: vi.fn() };

    await TestBed.configureTestingModule({
      declarations: [Login],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: ToastService, useValue: mockToastService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not call logIn if already loading', () => {
    component.cargando = true;
    component.login();
    expect(mockAuthService.logIn).not.toHaveBeenCalled();
  });

  it('should login successfully, set token, show toast and navigate after 2s', async () => {
    vi.useFakeTimers();
    const responseMock = { token: 'token123' };
    mockAuthService.logIn.mockReturnValue(of(responseMock));
    component.usuario = { nombreUsuario: 'user', correo: 'test@test.com', contrasenia: '123' };

    component.login();

    expect(component.cargando).toBe(false);
    expect(localStorage.getItem('token')).toBe('token123');
    expect(mockToastService.mostrar).toHaveBeenCalledWith('¡Bienvenido!', true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();

    await vi.advanceTimersByTimeAsync(2000);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/principal']);
    vi.useRealTimers();
  });

  it('should handle login error', () => {
    mockAuthService.logIn.mockReturnValue(throwError(() => new Error('Error')));

    component.login();

    expect(component.cargando).toBe(false);
    expect(mockToastService.mostrar).toHaveBeenCalledWith(
      'El nombre de usuario o la contraseña son incorrectos',
      false,
    );
    expect(localStorage.getItem('token')).toBeNull();
  });
});
