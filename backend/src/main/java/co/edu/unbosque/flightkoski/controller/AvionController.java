package co.edu.unbosque.flightkoski.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.flightkoski.dto.AvionDTO;
import co.edu.unbosque.flightkoski.service.AvionService;
import jakarta.validation.constraints.NotBlank;

/**
 * Controlador REST para gestionar la información de los aviones.
 * Permite realizar consultas generales y búsquedas especializadas por códigos IATA e ICAO.
 * @author Santiago Ortiz
 * @version 1.0
 */
@RestController
@Validated
@RequestMapping ("/avion")
@CrossOrigin(origins = { "https://aerodarkoski.netlify.app", "https://aerodarkoskicelular.netlify.app"})
public class AvionController {

	/** Servicio para la lógica de negocio de los aviones. */
    @Autowired
    private AvionService avionService;

    /**
     * Constructor por defecto del controlador.
     */
    public AvionController() {
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Obtiene la lista de todos los aviones registrados.
     * * @return Un {@link ResponseEntity} con la lista de {@link AvionDTO} y estado 200 OK,
     * o la lista con estado 204 NO CONTENT si no hay registros.
     */
    @GetMapping("/mostrar")
    public ResponseEntity<List<AvionDTO>> obtenerAviones() {
    	List<AvionDTO> aviones = avionService.obtenerTodos();
		if (aviones.isEmpty()) {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.OK);
		}
    }
    
    /**
     * Busca aviones asociados a un código de llegada IATA específico.
     * * @param iata Código IATA que se va a buscar (no puede estar vacío ni ser nulo).
     * @return Un {@link ResponseEntity} con los aviones encontrados y estado 200 OK,
     * o estado 204 NO CONTENT si no se hallaron coincidencias.
     */
    @GetMapping("/findbyiata/{iata}")
    public ResponseEntity<List<AvionDTO>> buscarPorIata(
    		@NotBlank(message = "El código iata no puede ser nulo o estar vacio") @PathVariable String iata
    		) {
    	List<AvionDTO> aviones = avionService.findByArrIata(iata);
		if (aviones.isEmpty()) {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.OK);
		}
    }
    
    /**
     * Busca aviones asociados a un código de llegada ICAO específico.
     * * @param icao Código ICAO que se va a buscar (no puede estar vacío ni ser nulo).
     * @return Un {@link ResponseEntity} con los aviones encontrados y estado 200 OK,
     * o estado 204 NO CONTENT si no se hallaron coincidencias.
     */
    @GetMapping("/findbyicao/{icao}")
    public ResponseEntity<List<AvionDTO>> buscarPorIcao(
    		@NotBlank(message = "El código icao no puede ser nulo o estar vacio") @PathVariable String icao
    		) {
    	List<AvionDTO> aviones = avionService.findByArrIcao(icao);
		if (aviones.isEmpty()) {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.OK);
		}
    }
    
    /**
     * Busca aviones filtrándolos por el código IATA de su vuelo actual.
     * * @param flightIata Código IATA del vuelo (no puede estar vacío ni ser nulo).
     * @return Un {@link ResponseEntity} con la lista de aviones y estado 200 OK,
     * o estado 204 NO CONTENT de estar vacía.
     */
    @GetMapping("/findbyflightiata/{flightIata}")
    public ResponseEntity<List<AvionDTO>> buscarPorFlightIata(
    		@NotBlank(message = "El código flight iata no puede ser nulo o estar vacio") @PathVariable String flightIata
    		) {
    	List<AvionDTO> aviones = avionService.findByFlightIata(flightIata);
		if (aviones.isEmpty()) {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.OK);
		}
    }
    
    /**
     * Busca aviones filtrándolos por el código ICAO de su vuelo actual.
     * * @param flightIcao Código ICAO del vuelo (no puede estar vacío ni ser nulo).
     * @return Un {@link ResponseEntity} con la lista de aviones y estado 200 OK,
     * o estado 204 NO CONTENT de estar vacía.
     */
    @GetMapping("/findbyflighticao/{flightIcao}")
    public ResponseEntity<List<AvionDTO>> buscarPorFlightIcao(
    		@NotBlank(message = "El código flight iaco no puede ser nulo o estar vacio") @PathVariable String flightIcao
    		) {
    	List<AvionDTO> aviones = avionService.findByFlightIcao(flightIcao);
		if (aviones.isEmpty()) {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<AvionDTO>>(aviones, HttpStatus.OK);
		}
    }
}