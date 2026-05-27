import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { describe, beforeEach, afterEach, it, expect } from 'vitest';
import { AuditoriaService } from './auditoria-service';
import { Auditoria } from '../models/auditoria';

describe('AuditoriaService', () => {
  let service: AuditoriaService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuditoriaService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(AuditoriaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send a GET request and observe the full response', () => {
    const mockAuditorias: Auditoria[] = [{ id: 1, idUsuario: 10, nombreUsuario: 'User1' } as any];

    service.mostrar().subscribe((response) => {
      expect(response.status).toBe(200);
      expect(response.body).toEqual(mockAuditorias);
    });

    const req = httpMock.expectOne('https://gpcueb.org/aerodarkoski/auditoria/mostrar');
    expect(req.request.method).toBe('GET');

    req.flush(mockAuditorias);
  });
});
