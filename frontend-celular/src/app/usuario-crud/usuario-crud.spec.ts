import { vi, describe, beforeEach, afterEach, it, expect } from 'vitest';
import { NgZone, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { UsuarioCrud } from './usuario-crud';
import { UsuarioService } from '../services/usuario-service';
import { JwtService } from '../services/jwt-service';
import { provideZoneChangeDetection } from '@angular/core';
import { of, throwError, Subject } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { Usuario } from '../models/usuario';
import { Rol } from '../enums/rol';

describe('UsuarioCrud', () => {
  let component: UsuarioCrud;
  let fixture: ComponentFixture<UsuarioCrud>;
  let mockUsuarioService: any;
  let mockJwtService: any;

  beforeEach(async () => {
    mockUsuarioService = {
      mostrar: vi.fn(),
      crear: vi.fn(),
      actualizar: vi.fn(),
      eliminar: vi.fn(),
    };

    mockJwtService = { getIdUsuario: vi.fn() };

    mockJwtService.getIdUsuario.mockReturnValue(1);
    mockUsuarioService.mostrar.mockReturnValue(of(new HttpResponse<Usuario[]>({ body: [] })));

    await TestBed.configureTestingModule({
      declarations: [UsuarioCrud],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        provideZoneChangeDetection(),
        NgZone,
        { provide: UsuarioService, useValue: mockUsuarioService },
        { provide: JwtService, useValue: mockJwtService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UsuarioCrud);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  describe('ngOnInit y Carga', () => {
    it('should load users and filter out current user', () => {
      const mockLista: Usuario[] = [
        { id: 1, nombreUsuario: 'Actual' },
        { id: 2, nombreUsuario: 'Otro' },
      ];
      mockUsuarioService.mostrar.mockReturnValue(of(new HttpResponse({ body: mockLista })));

      component.ngOnInit();

      expect(component.idUsuarioActual).toBe(1);
      expect(component.usuarios.length).toBe(1);
      expect(component.usuarios[0].id).toBe(2);
      expect(component.cargando).toBe(false);
    });

    it('should handle error on load users', () => {
      mockUsuarioService.mostrar.mockReturnValue(throwError(() => new Error('Error')));

      component.cargarUsuarios();

      expect(component.mensajeError).toBe('Error al cargar los usuarios. Intente nuevamente.');
      expect(component.usuarios.length).toBe(0);
      expect(component.cargando).toBe(false);
    });
  });

  describe('Filtros y Utilidades', () => {
    it('should return ID or index in buscarPorUsuario', () => {
      const user = { id: 99 } as Usuario;
      expect(component.buscarPorUsuario(3, user)).toBe(99);
      expect(component.buscarPorUsuario(3, { id: undefined } as Usuario)).toBe(3);
    });

    it('should filter users by text with debounce', () => {
      component.usuarios = [{ id: 2, nombreUsuario: 'Adrian', rol: Rol.ADMINISTRADOR }] as any;
      component.usuariosFiltradosCache = [...component.usuarios];

      component.filtroTexto = 'Juan';
      component.aplicarFiltro();

      expect(component.usuariosFiltradosCache.length).toBe(0);
    });

    it('should cache all users if filter text is empty', () => {
      component.usuarios = [{ id: 2 }] as any;
      component.filtroTexto = '   ';
      component.aplicarFiltro();
      expect(component.usuariosFiltradosCache.length).toBe(1);
    });
  });

  describe('Modales', () => {
    it('should open modal in create mode', () => {
      component.abrirCrear();
      expect(component.modoEdicion).toBe(false);
      expect(component.mostrarModal).toBe(true);
      expect(component.usuarioForm.id).toBeUndefined();
    });

    it('should open modal in edit mode with data', () => {
      const target = { id: 5, nombreUsuario: 'Edit', correo: 'e@e.com', rol: Rol.ADMINISTRADOR };
      component.abrirEditar(target);
      expect(component.modoEdicion).toBe(true);
      expect(component.usuarioForm.id).toBe(5);
      expect(component.usuarioForm.nombreUsuario).toBe('Edit');
      expect(component.mostrarModal).toBe(true);
    });

    it('should reset form on close modal', () => {
      component.cerrarModal();
      expect(component.mostrarModal).toBe(false);
      expect(component.usuarioForm.nombreUsuario).toBe('');
    });
  });

  describe('Guardar', () => {
    it('should prevent save if already saving', () => {
      component.guardando = true;
      component.guardar();
      expect(mockUsuarioService.crear).not.toHaveBeenCalled();
    });

    it('should validate empty username', () => {
      component.usuarioForm.nombreUsuario = '';
      component.guardar();
      expect(component.mensajeError).toBe('El nombre de usuario es requerido');
    });

    it('should validate matching passwords', () => {
      component.usuarioForm.nombreUsuario = 'user';
      component.usuarioForm.contrasenia = '123456';
      component.confirmarContrasenia = '654321';
      component.guardar();
      expect(component.mensajeError).toBe('Las contraseñas no coinciden');
    });

    it('should call crear service on success', async () => {
      component.modoEdicion = false;
      component.usuarioForm = {
        nombreUsuario: 'new',
        contrasenia: 'Abc@1234',
        correo: 'm@m.com',
        rol: Rol.ADMINISTRADOR
      };
      component.confirmarContrasenia = 'Abc@1234';
      component.confirmarCorreo = 'm@m.com';
      mockUsuarioService.crear.mockReturnValue(of('Creado OK'));

      vi.spyOn(component, 'cargarUsuarios').mockImplementation(() => {});

      component.guardar();
      tick();
      await Promise.resolve();
      await Promise.resolve();

      expect(mockUsuarioService.crear).toHaveBeenCalled();
      expect(component.mensajeExito).toBe('Creado OK');
    });

  });

  describe('Eliminar', () => {
    it('should execute delete successfully', async () => {
      component.usuarioAEliminar = { id: 15 } as Usuario;
      mockUsuarioService.eliminar.mockReturnValue(of('Eliminado OK'));

      vi.spyOn(component, 'cargarUsuarios').mockImplementation(() => {});

      component.eliminar();
      tick();
      await Promise.resolve();
      await Promise.resolve();

      expect(mockUsuarioService.eliminar).toHaveBeenCalledWith(15);
      expect(component.mensajeExito).toBe('Eliminado OK');
    });
  });
});
