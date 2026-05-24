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
import co.edu.unbosque.flightkoski.entity.Auditoria;
import co.edu.unbosque.flightkoski.repository.AuditoriaRepository;
import co.edu.unbosque.flightkoski.security.JwtAuthenticationFilter;
import co.edu.unbosque.flightkoski.security.JwtUtil;
import co.edu.unbosque.flightkoski.service.AuditoriaService;
import co.edu.unbosque.flightkoski.util.enums.Rol;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceTest {

    @Mock private AuditoriaRepository auditoriaRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private JwtUtil jwtUtil;
    @Mock private JwtAuthenticationFilter filtro;

    @InjectMocks
    private AuditoriaService auditoriaService;

    private Auditoria auditoriaEntity;
    private AuditoriaDTO auditoriaDTO;

    @BeforeEach
    void setUp() {
        auditoriaDTO  = new AuditoriaDTO();
        auditoriaEntity = new Auditoria();
    }


    @Test
    void preAccionSinTokenDebeRetornarDTONoNulo() {
        when(filtro.getTokenJWT()).thenReturn(null);

        AuditoriaDTO resultado = auditoriaService.preAccion(TipoAccion.READ, Servicio.USUARIO);
        assertNotNull(resultado);
    }

    @Test
    void preAccionConTokenVacioDebeRetornarDTONoNulo() {
        when(filtro.getTokenJWT()).thenReturn("");

        AuditoriaDTO resultado = auditoriaService.preAccion(TipoAccion.DELETE, Servicio.AVION);

        assertNotNull(resultado);
        assertNull(resultado.getIdUsuario());
        assertNull(resultado.getNombreUsuario());
    }

    @Test
    void preAccionConTokenDebeExtraerDatosDelJWT() {
        when(filtro.getTokenJWT()).thenReturn("tokenValido");
        when(jwtUtil.extractId("tokenValido")).thenReturn(1L);
        when(jwtUtil.extractUsername("tokenValido")).thenReturn("santiago");
        when(jwtUtil.extractRole("tokenValido")).thenReturn("USUARIO");

        AuditoriaDTO resultado = auditoriaService.preAccion(TipoAccion.CREATE, Servicio.HISTORIAL);

        assertNotNull(resultado);
        assertEquals(1L,         resultado.getIdUsuario());
        assertEquals("santiago", resultado.getNombreUsuario());
        verify(jwtUtil, times(1)).extractId("tokenValido");
        verify(jwtUtil, times(1)).extractUsername("tokenValido");
        verify(jwtUtil, times(1)).extractRole("tokenValido");
    }

    @Test
    void preAccionSinTokenNoDebeConsultarJWT() {
        when(filtro.getTokenJWT()).thenReturn(null);

        auditoriaService.preAccion(TipoAccion.READ, Servicio.USUARIO);
        verify(jwtUtil, never()).extractId(any());
        verify(jwtUtil, never()).extractUsername(any());
        verify(jwtUtil, never()).extractRole(any());
    }

    @Test
    void createDebeGuardarAuditoriaYRetornarCero() {
        when(modelMapper.map(auditoriaDTO, Auditoria.class)).thenReturn(auditoriaEntity);

        int resultado = auditoriaService.create(auditoriaDTO);

        assertEquals(0, resultado);
        verify(auditoriaRepository, times(1)).save(auditoriaEntity);
    }

    @Test
    void getAllListaVaciaDebeRetornarListaVacia() {
        when(auditoriaRepository.findAll()).thenReturn(new ArrayList<>());

        List<AuditoriaDTO> resultado = auditoriaService.getAll();

        assertTrue(resultado.isEmpty());
        verify(auditoriaRepository, times(1)).findAll();
    }

    @Test
    void getAllConRegistrosDebeRetornarListaMapeada() {
        when(auditoriaRepository.findAll()).thenReturn(List.of(auditoriaEntity));
        when(modelMapper.map(auditoriaEntity, AuditoriaDTO.class)).thenReturn(auditoriaDTO);

        List<AuditoriaDTO> resultado = auditoriaService.getAll();

        assertEquals(1, resultado.size());
        verify(modelMapper, times(1)).map(auditoriaEntity, AuditoriaDTO.class);
    }

    @Test
    void deleteByIdRegistroNoExisteDebeRetornarUno() {
        when(auditoriaRepository.findById(99L)).thenReturn(Optional.empty());

        int resultado = auditoriaService.deleteById(99L);

        assertEquals(1, resultado);
        verify(auditoriaRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteByIdRegistroExisteDebeEliminarYRetornarCero() {
        when(auditoriaRepository.findById(1L)).thenReturn(Optional.of(auditoriaEntity));

        int resultado = auditoriaService.deleteById(1L);

        assertEquals(0, resultado);
        verify(auditoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateByIdRegistroNoExisteDebeRetornarUno() {
        when(auditoriaRepository.findById(99L)).thenReturn(Optional.empty());

        int resultado = auditoriaService.updateById(99L, auditoriaDTO);

        assertEquals(1, resultado);
        verify(auditoriaRepository, never()).save(any());
    }

    @Test
    void updateByIdRegistroExisteDebeActualizarYRetornarCero() {
        when(auditoriaRepository.findById(1L)).thenReturn(Optional.of(auditoriaEntity));
        when(modelMapper.map(auditoriaDTO, Auditoria.class)).thenReturn(auditoriaEntity);

        int resultado = auditoriaService.updateById(1L, auditoriaDTO);

        assertEquals(0, resultado);
        verify(auditoriaRepository, times(1)).save(auditoriaEntity);
    }

    @Test
    void countDebeRetornarCantidadDeRegistros() {
        when(auditoriaRepository.count()).thenReturn(7L);

        long resultado = auditoriaService.count();

        assertEquals(7L, resultado);
        verify(auditoriaRepository, times(1)).count();
    }

    @Test
    void existRegistroExisteDebeRetornarTrue() {
        when(auditoriaRepository.existsById(1L)).thenReturn(true);

        assertTrue(auditoriaService.exist(1L));
    }

    @Test
    void existRegistroNoExisteDebeRetornarFalse() {
        when(auditoriaRepository.existsById(99L)).thenReturn(false);

        assertFalse(auditoriaService.exist(99L));
    }
}