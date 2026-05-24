package co.edu.unbosque.flightkoski.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.flightkoski.dto.ClimaDTO;
import co.edu.unbosque.flightkoski.service.ClimaService;

/**
 * Controlador REST encargado de gestionar y exponer la información climatológica pertinente.
 * @author Santiago Ortiz
 * @version 1.0
 */
@RestController
@RequestMapping ("/clima")
@CrossOrigin(origins = { "https://aerodarkoski.netlify.app", "https://aerodarkoskicelular.netlify.app"})
public class ClimaController {

	/** Servicio para la lógica del clima. */
	@Autowired
	private ClimaService climaSerrvice;
	
	/**
	 * Constructor por defecto del controlador.
	 */
	public ClimaController() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Obtiene los registros climatológicos del sistema.
	 * * @return Un {@link ResponseEntity} con la lista de {@link ClimaDTO} y estado 202 ACCEPTED si contiene datos,
	 * o un estado 204 NO CONTENT si la consulta no retorna elementos.
	 */
	 @GetMapping("/mostrar")
	    public ResponseEntity<List<ClimaDTO>> obtenerClima() {
	    	List<ClimaDTO> aviones = climaSerrvice.obtenerTodos();
			if (aviones.isEmpty()) {
				return new ResponseEntity<List<ClimaDTO>>(aviones, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<ClimaDTO>>(aviones, HttpStatus.ACCEPTED);
			}
	    }
}
