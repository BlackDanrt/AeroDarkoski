package co.edu.unbosque.flightkoski.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.flightkoski.dto.HistorialDTO;
import co.edu.unbosque.flightkoski.service.HistorialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * Controlador REST para el manejo de los historiales de búsqueda o navegación de los usuarios.
 * Permite recuperar registros específicos y añadir nuevas entradas al historial de la aplicación.
 * @author Santiago Ortiz
 * @version 1.0
 */
@RestController
@Validated
@RequestMapping ("/historial")
@CrossOrigin(origins = { "https://aerodarkoski.netlify.app", "https://aerodarkoskicelular.netlify.app"})
public class HistorialController {

	/** Servicio para la lógica del historial. */
	@Autowired
	private HistorialService historialService;
	
	/**
	 * Constructor por defecto del controlador.
	 */
	public HistorialController() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Recupera el historial correspondiente a un usuario a través de su identificador único.
	 * * @param idUsuario Identificador del usuario (debe ser un valor numérico positivo).
	 * @return Un {@link ResponseEntity} que contiene la lista de {@link HistorialDTO} con estado 200 OK,
	 * o estado 204 NO CONTENT si el usuario no posee registros históricos.
	 */
	@GetMapping("/findbyidusuario/{idUsuario}")
	public ResponseEntity<List<HistorialDTO>> findByIdUsuario(
			@Positive(message = "El id del usuario debe ser positivo") @PathVariable long idUsuario
			){
		List<HistorialDTO> listaHistorial = historialService.findByIdUsuario(idUsuario);
		if(!listaHistorial.isEmpty()) return new ResponseEntity<List<HistorialDTO>>(listaHistorial, HttpStatus.OK);
		else return new ResponseEntity<List<HistorialDTO>>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Registra una nueva entrada en el historial del sistema.
	 * * @param data Objeto {@link HistorialDTO} que contiene los datos validados del historial a crear.
	 * @return Un {@link ResponseEntity} con un mensaje de éxito y estado 200 OK si la operación fue exitosa,
	 * o un mensaje de error con estado 400 BAD REQUEST en caso contrario.
	 */
	@PostMapping("/crear")
	public ResponseEntity<String> crear(
			@Valid @RequestBody HistorialDTO data
			){
		int status = historialService.create(data);
		if(status == 0) return new ResponseEntity<String>("Historial creado con exito", HttpStatus.OK);
		else return new ResponseEntity<String>("Error al crear el historial", HttpStatus.BAD_REQUEST);
	}
}
