package co.edu.unbosque.flightkoski;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.HistorialDTO;
import co.edu.unbosque.flightkoski.entity.Historial;
import co.edu.unbosque.flightkoski.entity.Usuario;
import co.edu.unbosque.flightkoski.repository.HistorialRepository;
import co.edu.unbosque.flightkoski.repository.UsuarioRepository;
import co.edu.unbosque.flightkoski.service.AuditoriaService;
import co.edu.unbosque.flightkoski.service.HistorialService;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;
import co.edu.unbosque.flightkoski.util.enums.TipoBusqueda;

@ExtendWith(MockitoExtension.class)
class HistorialServiceTest {

    @Mock private HistorialRepository historialRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private AuditoriaService auditoriaService;

    @InjectMocks
    private HistorialService historialService;

    private Historial historialEntity;
    private HistorialDTO historialDTO;
    private Usuario usuarioEntity;

    @BeforeEach
    void setUp() {
        usuarioEntity = new Usuario();
        usuarioEntity.setId(1L);

        historialEntity = new Historial();
        historialEntity.setId(1L);

        historialDTO = new HistorialDTO();
        historialDTO.setIdUsuario(1L);
        historialDTO.setBusqueda("vuelo Bogotá");

        lenient().when(auditoriaService.preAccion(any(TipoAccion.class), eq(Servicio.HISTORIAL)))
                 .thenReturn(new AuditoriaDTO());
    }

    @Test
    void createUsuarioExisteDebeGuardarYRetornarCero() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));
        when(modelMapper.map(historialDTO, Historial.class)).thenReturn(historialEntity);

        int resultado = historialService.create(historialDTO);

        assertEquals(0, resultado);
        verify(historialRepository, times(1)).save(historialEntity);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void createUsuarioNoExisteDebeRetornarUnoSinGuardar() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        int resultado = historialService.create(historialDTO);

        assertEquals(1, resultado);
        verify(historialRepository, never()).save(any());
        verify(auditoriaService, never()).create(any());
    }

    @Test
    void getAllListaVaciaDebeRetornarListaVacia() {
        when(historialRepository.findAll()).thenReturn(new ArrayList<>());

        List<HistorialDTO> resultado = historialService.getAll();

        assertTrue(resultado.isEmpty());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void getAllConRegistrosDebeRetornarListaMapeada() {
        when(historialRepository.findAll()).thenReturn(List.of(historialEntity));
        when(modelMapper.map(historialEntity, HistorialDTO.class)).thenReturn(historialDTO);

        List<HistorialDTO> resultado = historialService.getAll();

        assertEquals(1, resultado.size());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void deleteByIdRegistroNoExisteDebeRetornarUno() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());

        int resultado = historialService.deleteById(99L);

        assertEquals(1, resultado);
        verify(historialRepository, never()).deleteById(anyLong());
        verify(auditoriaService, never()).create(any());
    }

    @Test
    void deleteByIdRegistroExisteDebeEliminarYRetornarCero() {
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historialEntity));

        int resultado = historialService.deleteById(1L);

        assertEquals(0, resultado);
        verify(historialRepository, times(1)).deleteById(1L);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void updateByIdRegistroNoExisteDebeRetornarUno() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());

        int resultado = historialService.updateById(99L, historialDTO);

        assertEquals(1, resultado);
        verify(historialRepository, never()).save(any());
        verify(auditoriaService, never()).create(any());
    }

    @Test
    void updateByIdRegistroExisteDebeActualizarCamposYRetornarCero() {
        historialDTO.setBusqueda("nuevo término");

        when(historialRepository.findById(1L)).thenReturn(Optional.of(historialEntity));

        int resultado = historialService.updateById(1L, historialDTO);

        assertEquals(0, resultado);
        assertEquals("nuevo término", historialEntity.getBusqueda());
        verify(historialRepository, times(1)).save(historialEntity);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void updateByIdRegistroExisteDebeActualizarTipoBusqueda() {
        historialDTO.setTipoBusqueda(TipoBusqueda.ICAO);

        when(historialRepository.findById(1L)).thenReturn(Optional.of(historialEntity));

        historialService.updateById(1L, historialDTO);

        assertEquals(TipoBusqueda.ICAO, historialEntity.getTipoBusqueda());
    }

    @Test
    void counDebeRetornarCantidadDeRegistros() {
        when(historialRepository.count()).thenReturn(3L);

        long resultado = historialService.count();

        assertEquals(3L, resultado);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void exisRegistroExisteDebeRetornarTrue() {
        when(historialRepository.existsById(1L)).thenReturn(true);

        assertTrue(historialService.exist(1L));
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void existRegistroNoExisteDebeRetornarFalse() {
        when(historialRepository.existsById(99L)).thenReturn(false);

        assertFalse(historialService.exist(99L));
    }

    @Test
    void findByIdUsuarioSinResultados_DebeRetornarListaVacia() {
        when(historialRepository.findByIdUsuario(1L)).thenReturn(new ArrayList<>());

        List<HistorialDTO> resultado = historialService.findByIdUsuario(1L);

        assertTrue(resultado.isEmpty());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByIdUsuarioConResultadoDebeRetornarListaMapeada() {
        when(historialRepository.findByIdUsuario(1L)).thenReturn(List.of(historialEntity));
        when(modelMapper.map(historialEntity, HistorialDTO.class)).thenReturn(historialDTO);

        List<HistorialDTO> resultado = historialService.findByIdUsuario(1L);

        assertEquals(1, resultado.size());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByIdUsuarioUsuarioConMultiplesHistorialesDebeRetornarTodos() {
        Historial h2 = new Historial();
        h2.setId(2L);
        HistorialDTO dto2 = new HistorialDTO();
        dto2.setIdUsuario(1L);

        when(historialRepository.findByIdUsuario(1L)).thenReturn(List.of(historialEntity, h2));
        when(modelMapper.map(historialEntity, HistorialDTO.class)).thenReturn(historialDTO);
        when(modelMapper.map(h2, HistorialDTO.class)).thenReturn(dto2);

        List<HistorialDTO> resultado = historialService.findByIdUsuario(1L);

        assertEquals(2, resultado.size());
    }
}