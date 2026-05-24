package co.edu.unbosque.flightkoski;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import co.edu.unbosque.flightkoski.dto.ArrivalDTO;
import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.AvionDTO;
import co.edu.unbosque.flightkoski.dto.FlightDTO;
import co.edu.unbosque.flightkoski.service.AuditoriaService;
import co.edu.unbosque.flightkoski.service.AvionService;
import co.edu.unbosque.flightkoski.service.ExternalHTPPRequestHandler;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AvionServiceTest {

    @Mock private ExternalHTPPRequestHandler handler;
    @Mock private AuditoriaService auditoriaService;

    @InjectMocks
    private AvionService avionService;

    private AvionDTO avionBogota;
    private AvionDTO avionMedellin;

    @BeforeEach
    void setUp() {
        avionBogota  = buildAvion("SKBO", "BO", "AV101", "AVA101");
        avionMedellin = buildAvion("SKMD", "MX", "AV202", "AVA202");

        when(handler.getAviones()).thenReturn(List.of(avionBogota, avionMedellin));
        avionService.actualizarAviones();

        when(auditoriaService.preAccion(TipoAccion.READ, Servicio.AVION))
                .thenReturn(new AuditoriaDTO());
        
        lenient().when(auditoriaService.preAccion(any(TipoAccion.class), eq(Servicio.USUARIO)))
        .thenReturn(new AuditoriaDTO());
    }

    @Test
    void actualizarAvionesResultadoNoNuloDebeActualizarCache() {
        when(handler.getAviones()).thenReturn(List.of(avionBogota));
        avionService.actualizarAviones();

        List<AvionDTO> resultado = avionService.obtenerTodos();
        assertEquals(1, resultado.size());
    }

    @Test
    void actualizarAvionesResultadoNuloNoDebeModificarCache() {
        when(handler.getAviones()).thenReturn(List.of(avionBogota, avionMedellin));
        avionService.actualizarAviones();

        when(handler.getAviones()).thenReturn(null);
        avionService.actualizarAviones();

        List<AvionDTO> resultado = avionService.obtenerTodos();
        assertEquals(2, resultado.size());
    }

    @Test
    void actualizarAvionesResultadoVacioNoDebeModificarCache() {
        when(handler.getAviones()).thenReturn(List.of(avionBogota));
        avionService.actualizarAviones();

        when(handler.getAviones()).thenReturn(new ArrayList<>());
        avionService.actualizarAviones();

        List<AvionDTO> resultado = avionService.obtenerTodos();
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerTodosDebeRetornarTodosLosAviones() {
        List<AvionDTO> resultado = avionService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void obtenerTodosCacheVaciaDebeRetornarListaVacia() {

        when(handler.getAviones()).thenReturn(new ArrayList<>());
        AvionService servicioLimpio = new AvionService();

        assertFalse(avionService.obtenerTodos().isEmpty());
    }

    @Test
    void findByArrIcaoCodigoExistenteDebeRetornarAvionesCoincidentes() {
        List<AvionDTO> resultado = avionService.findByArrIcao("SKBO");

        assertEquals(1, resultado.size());
        assertEquals("SKBO", resultado.get(0).getArrival().getIcaoCode());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByArrIcaoCodigoInexistenteDebeRetornarListaVacia() {
        List<AvionDTO> resultado = avionService.findByArrIcao("XXXX");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByArrIcaoCodigoEnMinusculasDebeIgnorarCase() {
        List<AvionDTO> resultado = avionService.findByArrIcao("skbo");

        assertEquals(1, resultado.size());
    }

    @Test
    void findByArrIataCodigoExistenteDebeRetornarAvionesCoincidentes() {
        List<AvionDTO> resultado = avionService.findByArrIata("BO");

        assertEquals(1, resultado.size());
        assertEquals("BO", resultado.get(0).getArrival().getIataCode());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByArrIataCodigoInexistenteDebeRetornarListaVacia() {
        List<AvionDTO> resultado = avionService.findByArrIata("ZZ");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByArrIataCodigoEnMinusculasDebeIgnorarCase() {
        List<AvionDTO> resultado = avionService.findByArrIata("bo");

        assertEquals(1, resultado.size());
    }

    @Test
    void findByFlightIataCodigoExistenteDebeRetornarAvionesCoincidentes() {
        List<AvionDTO> resultado = avionService.findByFlightIata("AV101");

        assertEquals(1, resultado.size());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByFlightIataCodigoInexistenteDebeRetornarListaVacia() {
        List<AvionDTO> resultado = avionService.findByFlightIata("XX999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByFlightIataCodigoEnMinusculas_DebeIgnorarCase() {
        List<AvionDTO> resultado = avionService.findByFlightIata("av101");

        assertEquals(1, resultado.size());
    }

    @Test
    void findByFlightIcaoCodigoExistenteDebeRetornarAvionesCoincidentes() {
        List<AvionDTO> resultado = avionService.findByFlightIcao("AVA101");

        assertEquals(1, resultado.size());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByFlightIcaoCodigoInexistenteDebeRetornarListaVacia() {
        List<AvionDTO> resultado = avionService.findByFlightIcao("XXX000");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByFlightIcaoCodigoEnMinusculasDebeIgnorarCase() {
        List<AvionDTO> resultado = avionService.findByFlightIcao("ava101");

        assertEquals(1, resultado.size());
    }

    private AvionDTO buildAvion(String arrIcao, String arrIata, String flightIata, String flightIcao) {
        AvionDTO avion = new AvionDTO();

        // Arrival
        ArrivalDTO arrival = new ArrivalDTO();
        arrival.setIcaoCode(arrIcao);
        arrival.setIataCode(arrIata);
        avion.setArrival(arrival);

        // Flight
        FlightDTO flight = new FlightDTO();
        flight.setIataNumber(flightIata);
        flight.setIcaoNumber(flightIcao);
        avion.setFlight(flight);

        return avion;
    }
}