import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth-service';
import { Rol } from '../enums/rol';
import { Usuario } from '../models/usuario';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('debe crearse correctamente', () => {
    expect(service).toBeTruthy();
  });

  it('registrar debe hacer POST a /auth/register', () => {
    const usuario: Usuario = { nombreUsuario: 'test', correo: 'test@test.com', contrasenia: '12345678' };
    service.registrar(usuario).subscribe();
    const req = httpMock.expectOne('https://gpcueb.org/aerodarkoski/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(usuario);
    req.flush('Usuario registrado');
  });

  it('logIn debe hacer POST a /auth/login y retornar token', () => {
    const usuario: Usuario = { nombreUsuario: 'test', contrasenia: '12345678' };
    const mockRespuesta = { token: 'mock.token.aqui' };
    service.logIn(usuario).subscribe(res => {
      expect(res.token).toBe('mock.token.aqui');
    });
    const req = httpMock.expectOne('https://gpcueb.org/aerodarkoski/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockRespuesta);
  });

  it('getUsuarioToken debe retornar null si no hay token en localStorage', () => {
    const resultado = service.getUsuarioToken();
    expect(resultado).toBeNull();
  });

  it('getIdUsuario debe retornar null si no hay token', () => {
    const resultado = service.getIdUsuario();
    expect(resultado).toBeNull();
  });

  it('getRolUsuario debe retornar null si no hay token', () => {
    const resultado = service.getRolUsuario();
    expect(resultado).toBeNull();
  });

  it('getUsuarioToken debe decodificar el token del localStorage', () => {
    const tokenMock = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwic3ViIjoidGVzdEB0ZXN0LmNvbSIsInJvbGUiOiJVU1VBUklPIn0.signature';
    localStorage.setItem('token', tokenMock);
    const resultado = service.getUsuarioToken();
    expect(resultado).not.toBeNull();
    expect(resultado?.id).toBe(1);
    expect(resultado?.sub).toBe('test@test.com');
    expect(resultado?.role).toBe(Rol.USUARIO);
  });

  it('getIdUsuario debe retornar el id del token', () => {
    const tokenMock = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwic3ViIjoidGVzdEB0ZXN0LmNvbSIsInJvbGUiOiJVU1VBUklPIn0.signature';
    localStorage.setItem('token', tokenMock);
    expect(service.getIdUsuario()).toBe(1);
  });

  it('getRolUsuario debe retornar el rol del token', () => {
    const tokenMock = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwic3ViIjoidGVzdEB0ZXN0LmNvbSIsInJvbGUiOiJVU1VBUklPIn0.signature';
    localStorage.setItem('token', tokenMock);
    expect(service.getRolUsuario()).toBe(Rol.USUARIO);
  });
});
