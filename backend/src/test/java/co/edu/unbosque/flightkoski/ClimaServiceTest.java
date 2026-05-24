package co.edu.unbosque.flightkoski;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.ClimaDTO;
import co.edu.unbosque.flightkoski.service.AuditoriaService;
import co.edu.unbosque.flightkoski.service.ClimaService;
import co.edu.unbosque.flightkoski.service.ExternalHTPPRequestHandler;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

@ExtendWith(MockitoExtension.class)
class ClimaServiceTest {

    @Mock private ExternalHTPPRequestHandler handler;
    @Mock private AuditoriaService auditoriaService;

    @InjectMocks
    private ClimaService climaService;

    private ClimaDTO climaBogota;
    private ClimaDTO climaMedellin;

    @BeforeEach
    void setUp() {
        climaBogota   = new ClimaDTO();
        climaMedellin = new ClimaDTO();

        when(handler.getClima()).thenReturn(List.of(climaBogota, climaMedellin));
        climaService.actualizarClima();

        when(auditoriaService.preAccion(TipoAccion.READ, Servicio.CLIMA))
                .thenReturn(new AuditoriaDTO());
    }

    @Test
    void actualizarClimaResultadoValidoDebeActualizarCache() {
        when(handler.getClima()).thenReturn(List.of(climaBogota));
        climaService.actualizarClima();

        List<ClimaDTO> resultado = climaService.obtenerTodos();
        assertEquals(1, resultado.size());
    }

    @Test
    void actualizarClimaResultadoNuloNoDebeModificarCache() {
    	
        when(handler.getClima()).thenReturn(List.of(climaBogota, climaMedellin));
        climaService.actualizarClima();

        when(handler.getClima()).thenReturn(null);
        climaService.actualizarClima();

        List<ClimaDTO> resultado = climaService.obtenerTodos();
        assertEquals(2, resultado.size());
    }

    @Test
    void actualizarClimaResultadoVacioNoDebeModificarCache() {
        when(handler.getClima()).thenReturn(List.of(climaBogota));
        climaService.actualizarClima();

        when(handler.getClima()).thenReturn(new ArrayList<>());
        climaService.actualizarClima();

        List<ClimaDTO> resultado = climaService.obtenerTodos();
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerTodosDebeRetornarTodosLosReportes() {
        List<ClimaDTO> resultado = climaService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(auditoriaService, times(1)).preAccion(TipoAccion.READ, Servicio.CLIMA);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void obtenerTodosDebeRegistrarAuditoria() {
        climaService.obtenerTodos();

        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void obtenerTodosLlamadoMultiplesVecesDebeAuditarCadaVez() {
        climaService.obtenerTodos();
        climaService.obtenerTodos();
        climaService.obtenerTodos();

        verify(auditoriaService, times(3)).create(any(AuditoriaDTO.class));
    }
}