import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { routerGuard } from './router-guard';
import { AuthService } from '../services/auth-service';
import { ToastService } from '../services/toast-service';
import { Rol } from '../enums/rol';

describe('routerGuard', () => {
  let authService: { getUsuarioToken: ReturnType<typeof vi.fn> };
  let router: { navigate: ReturnType<typeof vi.fn> };
  let toastService: { mostrar: ReturnType<typeof vi.fn> };
  let route: ActivatedRouteSnapshot;

  beforeEach(() => {
    authService = { getUsuarioToken: vi.fn() };
    router = { navigate: vi.fn() };
    toastService = { mostrar: vi.fn() };
    route = new ActivatedRouteSnapshot();
    (route as any).data = {};

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
        { provide: ToastService, useValue: toastService },
      ],
    });
  });

  it('debe redirigir a login si no hay token', () => {
    authService.getUsuarioToken.mockReturnValue(null);
    const resultado = TestBed.runInInjectionContext(() =>
      routerGuard(route, {} as RouterStateSnapshot)
    );
    expect(resultado).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(toastService.mostrar).toHaveBeenCalled();
  });

  it('debe permitir acceso si hay token y no hay roles requeridos', () => {
    authService.getUsuarioToken.mockReturnValue({ id: 1, sub: 'test@test.com', role: Rol.USUARIO });
    (route as any).data = { roles: [] };
    const resultado = TestBed.runInInjectionContext(() =>
      routerGuard(route, {} as RouterStateSnapshot)
    );
    expect(resultado).toBe(true);
  });

  it('debe permitir acceso si el rol del usuario está en los roles requeridos', () => {
    authService.getUsuarioToken.mockReturnValue({ id: 1, sub: 'admin@test.com', role: Rol.ADMINISTRADOR });
    (route as any).data = { roles: [Rol.ADMINISTRADOR] };
    const resultado = TestBed.runInInjectionContext(() =>
      routerGuard(route, {} as RouterStateSnapshot)
    );
    expect(resultado).toBe(true);
  });

  it('debe denegar acceso y redirigir si el rol no coincide', () => {
    authService.getUsuarioToken.mockReturnValue({ id: 1, sub: 'test@test.com', role: Rol.USUARIO });
    (route as any).data = { roles: [Rol.ADMINISTRADOR] };
    const resultado = TestBed.runInInjectionContext(() =>
      routerGuard(route, {} as RouterStateSnapshot)
    );
    expect(resultado).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(toastService.mostrar).toHaveBeenCalled();
  });

  it('debe permitir acceso si no se definen roles en la ruta', () => {
    authService.getUsuarioToken.mockReturnValue({ id: 1, sub: 'test@test.com', role: Rol.USUARIO });
    (route as any).data = {};
    const resultado = TestBed.runInInjectionContext(() =>
      routerGuard(route, {} as RouterStateSnapshot)
    );
    expect(resultado).toBe(true);
  });
});
