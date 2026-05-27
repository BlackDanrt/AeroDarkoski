import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi, describe, beforeEach, it, expect } from 'vitest';
import { Nav } from './nav';
import { JwtService } from '../services/jwt-service';
import { Router } from '@angular/router';
import { Rol } from '../enums/rol';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('Nav', () => {
  let component: Nav;
  let fixture: ComponentFixture<Nav>;
  let mockJwtService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockJwtService = {
      getToken: vi.fn(),
      removerToken: vi.fn(),
      getRolUsuario: vi.fn(),
      getIdUsuario: vi.fn(),
    };
    mockRouter = {
      navigate: vi.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [Nav],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: JwtService, useValue: mockJwtService },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Nav);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('isAutenticado', () => {
    it('should return true if token is not null', () => {
      mockJwtService.getToken.mockReturnValue('mock-token');
      expect(component.isAutenticado()).toBe(true);
    });

    it('should return false if token is null', () => {
      mockJwtService.getToken.mockReturnValue(null);
      expect(component.isAutenticado()).toBe(false);
    });
  });

  describe('cerrarSesion', () => {
    it('should call removerToken and navigate to login', () => {
      component.cerrarSesion();
      expect(mockJwtService.removerToken).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['login']);
    });
  });

  describe('isAdministrador', () => {
    it('should return true if user role is ADMINISTRADOR', () => {
      mockJwtService.getRolUsuario.mockReturnValue(Rol.ADMINISTRADOR);
      expect(component.isAdministrador()).toBe(true);
    });

    it('should return false if user role is not ADMINISTRADOR', () => {
      mockJwtService.getRolUsuario.mockReturnValue(Rol.USUARIO);
      expect(component.isAdministrador()).toBe(false);
    });
  });

  describe('navegarAHistorial', () => {
    it('should navigate to the correct history URL using the user ID', () => {
      mockJwtService.getIdUsuario.mockReturnValue(123);

      component.navegarAHistorial();

      expect(mockJwtService.getIdUsuario).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/historial/123']);
    });
  });
});
