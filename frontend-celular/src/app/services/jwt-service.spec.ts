import { TestBed } from '@angular/core/testing';
import { describe, beforeEach, it, expect } from 'vitest';
import { JwtService } from './jwt-service';
import { Rol } from '../enums/rol';

describe('JwtService', () => {
  let service: JwtService;
  const mockToken =
    'header.eyJpZCI6MTIzLCJyb2xlIjoiQURNSU5JU1RSQURPUiIsInN1YiI6ImFkcmlhbiJ9.signature';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [JwtService],
    });
    service = TestBed.inject(JwtService);
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getToken', () => {
    it('should return null if token does not exist', () => {
      expect(service.getToken()).toBeNull();
    });

    it('should return the token from localStorage if it exists', () => {
      localStorage.setItem('token', 'test-token');
      expect(service.getToken()).toBe('test-token');
    });
  });

  describe('setToken', () => {
    it('should save the trimmed token to localStorage', () => {
      service.setToken('  my-token  ');
      expect(localStorage.getItem('token')).toBe('my-token');
    });

    it('should not save anything if token is empty or whitespace', () => {
      service.setToken('   ');
      expect(localStorage.getItem('token')).toBeNull();
    });
  });

  describe('removerToken', () => {
    it('should remove the token from localStorage', () => {
      localStorage.setItem('token', 'to-be-removed');
      service.removerToken();
      expect(localStorage.getItem('token')).toBeNull();
    });
  });

  describe('decodificarToken', () => {
    it('should successfully decode a valid JWT token payload', () => {
      const payload = service.decodificarToken(mockToken);
      expect(payload).not.toBeNull();
      expect(payload?.id).toBe(123);
      expect(payload?.role).toBe(Rol.ADMINISTRADOR);
      expect(payload?.sub).toBe('adrian');
    });

    it('should return null if the token structure is invalid', () => {
      expect(service.decodificarToken('invalidtoken')).toBeNull();
    });

    it('should return null if decoding fails', () => {
      expect(service.decodificarToken('header.invalid-base64-!!!.signature')).toBeNull();
    });
  });

  describe('getIdUsuario', () => {
    it('should return null if no token is stored', () => {
      expect(service.getIdUsuario()).toBeNull();
    });

    it('should return user id from decoded token', () => {
      localStorage.setItem('token', mockToken);
      expect(service.getIdUsuario()).toBe(123);
    });
  });

  describe('getRolUsuario', () => {
    it('should return null if no token is stored', () => {
      expect(service.getRolUsuario()).toBeNull();
    });

    it('should return user role from decoded token', () => {
      localStorage.setItem('token', mockToken);
      expect(service.getRolUsuario()).toBe(Rol.ADMINISTRADOR);
    });
  });

  describe('getNombreUsuario', () => {
    it('should return null if no token is stored', () => {
      expect(service.getNombreUsuario()).toBeNull();
    });

    it('should return subject (username) from decoded token', () => {
      localStorage.setItem('token', mockToken);
      expect(service.getNombreUsuario()).toBe('adrian');
    });
  });
});
