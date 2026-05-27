import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { describe, beforeEach, afterEach, it, expect } from 'vitest';
import { HistorialService } from './historial-service';
import { Historial } from '../models/historial';

describe('HistorialService', () => {
  let service: HistorialService;
  let httpMock: HttpTestingController;
  const baseUrl = 'https://gpcueb.org/aerodarkoski/historial';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HistorialService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(HistorialService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create history and return text response', () => {
    const mockHistorial: Historial = {
      idUsuario: 1,
      busqueda: 'SKBO',
      tipoBusqueda: 'IATA',
    } as any;
    const expectedResponse = 'Historial creado con éxito';

    service.crear(mockHistorial).subscribe((response) => {
      expect(response).toBe(expectedResponse);
    });

    const req = httpMock.expectOne(`${baseUrl}/crear`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockHistorial);
    expect(req.request.responseType).toBe('text');
    req.flush(expectedResponse);
  });

  it('should fetch history by user id and observe full response', () => {
    const mockHistoriales: Historial[] = [
      { idUsuario: 1, busqueda: 'SKBO', tipoBusqueda: 'IATA' } as any,
    ];

    service.mostrar(1).subscribe((response) => {
      expect(response.status).toBe(200);
      expect(response.body).toEqual(mockHistoriales);
    });

    const req = httpMock.expectOne(`${baseUrl}/findbyidusuario/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockHistoriales);
  });
});
