import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { describe, beforeEach, afterEach, it, expect } from 'vitest';
import { AvionService } from './avion-service';
import { Avion } from '../models/avion';

describe('AvionService', () => {
  let service: AvionService;
  let httpMock: HttpTestingController;
  const baseUrl = 'https://gpcueb.org/aerodarkoski/avion';
  const mockAviones: Avion[] = [{} as Avion];

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AvionService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(AvionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all planes via obtenerTodos', () => {
    service.obtenerTodos().subscribe((res) => {
      expect(res).toEqual(mockAviones);
    });

    const req = httpMock.expectOne(`${baseUrl}/mostrar`);
    expect(req.request.method).toBe('GET');
    req.flush(mockAviones);
  });

  it('should fetch by arrival IATA via buscarPorArrIata', () => {
    service.buscarPorArrIata('BOG').subscribe((res) => {
      expect(res).toEqual(mockAviones);
    });

    const req = httpMock.expectOne(`${baseUrl}/findbyiata/BOG`);
    expect(req.request.method).toBe('GET');
    req.flush(mockAviones);
  });

  it('should fetch by arrival ICAO via buscarPorArrIcao', () => {
    service.buscarPorArrIcao('SKBO').subscribe((res) => {
      expect(res).toEqual(mockAviones);
    });

    const req = httpMock.expectOne(`${baseUrl}/findbyicao/SKBO`);
    expect(req.request.method).toBe('GET');
    req.flush(mockAviones);
  });

  it('should fetch by flight IATA via buscarPorFlightIata', () => {
    service.buscarPorFlightIata('AV26').subscribe((res) => {
      expect(res).toEqual(mockAviones);
    });

    const req = httpMock.expectOne(`${baseUrl}/findbyflightiata/AV26`);
    expect(req.request.method).toBe('GET');
    req.flush(mockAviones);
  });

  it('should fetch by flight ICAO via buscarPorFlightIcao', () => {
    service.buscarPorFlightIcao('AVA26').subscribe((res) => {
      expect(res).toEqual(mockAviones);
    });

    const req = httpMock.expectOne(`${baseUrl}/findbyflighticao/AVA26`);
    expect(req.request.method).toBe('GET');
    req.flush(mockAviones);
  });
});
