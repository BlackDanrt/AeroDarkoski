import { TestBed } from '@angular/core/testing';
import { describe, beforeEach, it, expect } from 'vitest';
import { ToastService } from './toast-service';

describe('ToastService', () => {
  let service: ToastService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ToastService],
    });
    service = TestBed.inject(ToastService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should emit toast message and success status when mostrar is called', async () => {
    const expectedPayload = { mensaje: 'Operación exitosa', exito: true };

    const promise = new Promise((resolve) => {
      service.toast$.subscribe((payload) => {
        resolve(payload);
      });
    });

    service.mostrar('Operación exitosa', true);

    const payload = await promise;
    expect(payload).toEqual(expectedPayload);
  });
});
