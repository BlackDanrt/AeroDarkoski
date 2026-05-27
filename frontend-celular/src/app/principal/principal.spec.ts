import { ComponentFixture, TestBed, fakeAsync, tick, flush } from '@angular/core/testing';
import { vi, describe, beforeEach, it, expect } from 'vitest';
import { Principal } from './principal';
import { Router } from '@angular/router';
import 'zone.js';
import 'zone.js/testing';

describe('Principal', () => {
  let component: Principal;
  let fixture: ComponentFixture<Principal>;
  let mockRouter: any;

  beforeEach(async () => {
    mockRouter = {
      navigate: vi.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [Principal],
      providers: [{ provide: Router, useValue: mockRouter }],
    }).compileComponents();

    fixture = TestBed.createComponent(Principal);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate after 2 seconds with correctly parsed integer parameter', async () => {
    vi.useFakeTimers();

    component.buscar('SKBO', '1');

    expect(mockRouter.navigate).not.toHaveBeenCalled();

    await vi.advanceTimersByTimeAsync(2000);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/busqueda/SKBO/1']);
    vi.useRealTimers();
  });
});
