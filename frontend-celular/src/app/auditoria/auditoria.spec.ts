import 'zone.js';
import 'zone.js/testing';

import { ComponentFixture, TestBed, fakeAsync, tick, flush } from '@angular/core/testing';
import { vi, describe, beforeEach, it, expect } from 'vitest';
import { AuditoriaComponent } from './auditoria';
import { AuditoriaService } from '../services/auditoria-service';
import { HistorialService } from '../services/historial-service';
import { of, throwError } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { Auditoria } from '../models/auditoria';

describe('AuditoriaComponent', () => {
  let component: AuditoriaComponent;
  let fixture: ComponentFixture<AuditoriaComponent>;
  let mockAuditoriaService: any;

  beforeEach(async () => {

    mockAuditoriaService = {
      mostrar: vi.fn()
    };


    mockAuditoriaService.mostrar.mockReturnValue(of(new HttpResponse<Auditoria[]>({ body: [] })));

    await TestBed.configureTestingModule({
      declarations: [AuditoriaComponent],
      providers: [
        { provide: AuditoriaService, useValue: mockAuditoriaService },
        { provide: HistorialService, useValue: {} },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AuditoriaComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should load auditorias and reverse them', () => {
    const data: Auditoria[] = [{ id: 1 } as Auditoria, { id: 2 } as Auditoria];
    mockAuditoriaService.mostrar.mockReturnValue(of(new HttpResponse({ body: data })));

    fixture.detectChanges();

    expect(component.auditorias[0].id).toBe(2);
    expect(component.auditorias[1].id).toBe(1);
  });

  it('should handle error on load', () => {
    mockAuditoriaService.mostrar.mockReturnValue(throwError(() => new Error('Error')));

    fixture.detectChanges();

    expect(component.mensajeError).toBe('Error al cargar las auditorías. Intente nuevamente.');
    expect(component.auditorias.length).toBe(0);
  });

  it('should filter auditorias with debounce', () => {
    component.auditorias = [
      { id: 1, nombreUsuario: 'Juan' },
      { id: 2, nombreUsuario: 'Pedro' },
    ] as any;
    component.auditoriasFiltradasCache = [...component.auditorias];

    component.filtroTexto = 'Pedro';
    component.aplicarFiltro();

    expect(component.auditoriasFiltradasCache.length).toBe(1);
    expect(component.auditoriasFiltradasCache[0].nombreUsuario).toBe('Pedro');
  });
});
