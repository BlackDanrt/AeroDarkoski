import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { vi, describe, beforeEach, afterEach, it, expect } from 'vitest';
import { ClimaService } from './clima-service';
import { Clima } from '../models/clima';

describe('ClimaService', () => {
  let service: ClimaService;
  let httpMock: HttpTestingController;
  const baseUrl = 'https://gpcueb.org/aerodarkoski/clima/mostrar';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClimaService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ClimaService);
    httpMock = TestBed.inject(HttpTestingController);
    vi.spyOn(console, 'error').mockImplementation(() => {});
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch weather reports successfully', () => {
    const mockClimas: Clima[] = [{ icaoId: 'SKBO', temp: 14 } as any];

    service.obtenerReportesClima().subscribe((res) => {
      expect(res).toEqual(mockClimas);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockClimas);
  });

  it('should return empty array if response is falsy', () => {
    service.obtenerReportesClima().subscribe((res) => {
      expect(res).toEqual([]);
    });

    const req = httpMock.expectOne(baseUrl);
    req.flush(null);
  });

  it('should handle network error using ErrorEvent', () => {
    const mockError = new ErrorEvent('NetworkError', { message: 'Connection failed' });

    service.obtenerReportesClima().subscribe({
      next: () => expect.fail('should have failed'),
      error: (err) => {
        expect(err.message).toContain('Error de red: Connection failed');
      },
    });

    const req = httpMock.expectOne(baseUrl);
    req.error(mockError);
  });

  it('should handle backend server error statuses', () => {
    service.obtenerReportesClima().subscribe({
      next: () => expect.fail('should have failed'),
      error: (err) => {
        expect(err.message).toContain('Error del servidor: 500');
      },
    });

    const req = httpMock.expectOne(baseUrl);
    req.flush('Internal Server Error', { status: 500, statusText: 'Server Error' });
  });
});
