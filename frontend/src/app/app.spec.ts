import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef, NO_ERRORS_SCHEMA } from '@angular/core';
import { Subject } from 'rxjs';
import { vi, describe, beforeEach, afterEach, it, expect } from 'vitest';
import { App } from './app';
import { ToastService } from './services/toast-service';
import { Toast } from 'bootstrap';

describe('App', () => {
  let component: App;
  let fixture: ComponentFixture<App>;
  let toastSubject: Subject<{ mensaje: string; exito: boolean }>;
  let mockToastService: any;
  let mockCdr: { detectChanges: any };

  beforeEach(async () => {
    toastSubject = new Subject<{ mensaje: string; exito: boolean }>();

    mockToastService = {
      toast$: toastSubject.asObservable()
    };

    mockCdr = {
      detectChanges: vi.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [App],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: ToastService, useValue: mockToastService }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(App);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    vi.restoreAllMocks();
    const existingToast = document.getElementById('miToast');
    if (existingToast && existingToast.parentNode) {
      existingToast.parentNode.removeChild(existingToast);
    }
  });

  describe('creación del componente', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should have default values', () => {
      expect(component.mensajeToast).toBe('');
      expect(component.toastExito).toBe(true);
    });

    it('should have the correct title signal', () => {
      expect(component['title']()).toBe('AeroDarkoski');
    });
  });

  describe('ngOnInit', () => {
    it('should subscribe to toastService.toast$', () => {
      const mostrarSpy = vi.spyOn(component as any, 'mostrarToast').mockImplementation(() => {});
      fixture.detectChanges();

      toastSubject.next({ mensaje: 'Hola', exito: true });

      expect(mostrarSpy).toHaveBeenCalledWith('Hola', true);
    });

    it('should call mostrarToast with correct values when toast$ emits', () => {
      const mostrarSpy = vi.spyOn(component as any, 'mostrarToast').mockImplementation(() => {});
      fixture.detectChanges();

      toastSubject.next({ mensaje: 'Error!', exito: false });

      expect(mostrarSpy).toHaveBeenCalledWith('Error!', false);
    });

    it('should handle multiple emissions from toast$', () => {
      const mostrarSpy = vi.spyOn(component as any, 'mostrarToast').mockImplementation(() => {});
      fixture.detectChanges();

      toastSubject.next({ mensaje: 'Primero', exito: true });
      toastSubject.next({ mensaje: 'Segundo', exito: false });

      expect(mostrarSpy).toHaveBeenCalledTimes(2);
    });
  });

  describe('mostrarToast', () => {
    it('should set mensajeToast and toastExito', () => {
      const toastEl = document.createElement('div');
      toastEl.id = 'miToast';
      document.body.appendChild(toastEl);
      vi.spyOn(Toast, 'getInstance').mockReturnValue(null);
      vi.spyOn(Toast.prototype, 'show').mockImplementation(() => {});

      fixture.detectChanges();
      toastSubject.next({ mensaje: 'Guardado', exito: true });

      expect(component.mensajeToast).toBe('Guardado');
      expect(component.toastExito).toBe(true);
    });

    it('should set toastExito to false when exito is false', () => {
      const toastEl = document.createElement('div');
      toastEl.id = 'miToast';
      document.body.appendChild(toastEl);
      vi.spyOn(Toast, 'getInstance').mockReturnValue(null);
      vi.spyOn(Toast.prototype, 'show').mockImplementation(() => {});

      fixture.detectChanges();
      toastSubject.next({ mensaje: 'Fallo', exito: false });

      expect(component.toastExito).toBe(false);
    });

    it('should call detectChanges', () => {
      const toastEl = document.createElement('div');
      toastEl.id = 'miToast';
      document.body.appendChild(toastEl);
      vi.spyOn(Toast, 'getInstance').mockReturnValue(null);
      vi.spyOn(Toast.prototype, 'show').mockImplementation(() => {});

      fixture.detectChanges();

      const cdrSpy = vi.spyOn((component as any).cdr, 'detectChanges');

      toastSubject.next({ mensaje: 'Test', exito: true });

      expect(cdrSpy).toHaveBeenCalled();
    });

    it('should return early if #miToast element does not exist in DOM', () => {
      fixture.detectChanges();

      vi.spyOn(document, 'getElementById').mockReturnValue(null);
      const toastSpy = vi.spyOn(Toast.prototype, 'show').mockImplementation(() => {});

      toastSubject.next({ mensaje: 'Sin DOM', exito: true });

      expect(toastSpy).not.toHaveBeenCalled();
    });

    it('should dispose previous Toast instance if one exists', () => {
      const mockDispose = vi.fn();
      const mockToastInstance = { dispose: mockDispose, show: vi.fn() } as unknown as Toast;

      vi.spyOn(Toast, 'getInstance').mockReturnValue(mockToastInstance);
      vi.spyOn(Toast.prototype, 'show').mockImplementation(() => {});

      const toastEl = document.createElement('div');
      toastEl.id = 'miToast';
      document.body.appendChild(toastEl);

      fixture.detectChanges();
      toastSubject.next({ mensaje: 'Con instancia previa', exito: true });

      expect(mockDispose).toHaveBeenCalled();
    });

    it('should create and show a new Toast when element exists', () => {
      vi.spyOn(Toast, 'getInstance').mockReturnValue(null);

      const mockShow = vi.fn();
      vi.spyOn(Toast.prototype, 'show').mockImplementation(mockShow);

      const toastEl = document.createElement('div');
      toastEl.id = 'miToast';
      document.body.appendChild(toastEl);

      fixture.detectChanges();
      toastSubject.next({ mensaje: 'Nuevo toast', exito: true });

      expect(component.mensajeToast).toBe('Nuevo toast');
      expect(component.toastExito).toBe(true);
      expect(mockShow).toHaveBeenCalled();
    });
  });
});
