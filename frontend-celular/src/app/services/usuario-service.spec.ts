import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { describe, beforeEach, afterEach, it, expect } from 'vitest';
import { UsuarioService } from './usuario-service';
import { Usuario } from '../models/usuario';

describe('UsuarioService', () => {
  let service: UsuarioService;
  let httpMock: HttpTestingController;
  const baseUrl = 'https://gpcueb.org/aerodarkoski/usuario';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UsuarioService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(UsuarioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create a user and return text response', () => {
    const mockUsuario: Usuario = { nombreUsuario: 'adrian', contrasenia: '12345678' };
    const expectedResponse = 'Usuario creado con éxito';

    service.crear(mockUsuario).subscribe((response) => {
      expect(response).toBe(expectedResponse);
    });

    const req = httpMock.expectOne(`${baseUrl}/crear`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockUsuario);
    expect(req.request.responseType).toBe('text');
    req.flush(expectedResponse);
  });

  it('should update a user and return text response', () => {
    const mockUsuario: Usuario = { nombreUsuario: 'adrian-edit', correo: 'test@test.com' };
    const expectedResponse = 'Usuario actualizado con éxito';

    service.actualizar(mockUsuario, 1).subscribe((response) => {
      expect(response).toBe(expectedResponse);
    });

    const req = httpMock.expectOne(`${baseUrl}/actualizar/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockUsuario);
    expect(req.request.responseType).toBe('json');
    req.flush(expectedResponse);
  });

  it('should delete a user and return text response', () => {
    const expectedResponse = 'Usuario eliminado con éxito';

    service.eliminar(1).subscribe((response) => {
      expect(response).toBe(expectedResponse);
    });

    const req = httpMock.expectOne(`${baseUrl}/eliminar/1`);
    expect(req.request.method).toBe('DELETE');
    expect(req.request.responseType).toBe('text');
    req.flush(expectedResponse);
  });

  it('should fetch all users observing full response', () => {
    const mockUsuarios: Usuario[] = [
      { id: 1, nombreUsuario: 'user1' },
      { id: 2, nombreUsuario: 'user2' },
    ];

    service.mostrar().subscribe((response) => {
      expect(response.status).toBe(200);
      expect(response.body).toEqual(mockUsuarios);
    });

    const req = httpMock.expectOne(`${baseUrl}/mostrar`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUsuarios);
  });

  it('should fetch a single user by ID and map to body directly', () => {
    const mockUsuario: Usuario = { id: 1, nombreUsuario: 'adrian' };

    service.getById(1).subscribe((response) => {
      expect(response).toEqual(mockUsuario);
    });

    const req = httpMock.expectOne(`${baseUrl}/getbyid/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUsuario);
  });

  it('should count users observing full response', () => {
    const expectedPayload = { cantidad: 42 };

    service.contar().subscribe((response) => {
      expect(response.status).toBe(200);
      expect(response.body).toEqual(expectedPayload);
    });

    const req = httpMock.expectOne(`${baseUrl}/contar`);
    expect(req.request.method).toBe('GET');
    req.flush(expectedPayload);
  });
});
