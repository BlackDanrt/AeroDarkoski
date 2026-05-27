package co.edu.unbosque.flightkoski.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.flightkoski.dto.UsuarioDTO;
import co.edu.unbosque.flightkoski.entity.Usuario;
import co.edu.unbosque.flightkoski.security.JwtUtil;
import co.edu.unbosque.flightkoski.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador REST destinado a la gestión administrativa de los usuarios del sistema,
 * permitiendo el control completo sobre sus perfiles y roles.
 * @author Santiago Ortiz
 * @version 1.0
 */
@RestController
@Validated
@RequestMapping ("/usuario")
@CrossOrigin(origins = { "https://aerodarkoski.netlify.app", "https://aerodarkoskicelular.netlify.app"})
public class UsuarioController {

	/** Servicio con la lógica de negocio para los perfiles de usuario. */
	@Autowired
	private UsuarioService usuarioService;
	
	/** Utilidad para la administración de JSON Web Tokens (JWT). */
	@Autowired
	private JwtUtil jwtUtil;

	/** Servicio nativo de Spring Security para cargar datos de usuario. */
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * Crea de manera directa un nuevo usuario administrando las validaciones correspondientes de formato.
	 * * @param usuario DTO con la información base del nuevo usuario
	 * @return ResponseEntity indicando éxito o especificando el error de validación/duplicidad
	 */
	@PostMapping("/crear")
	public ResponseEntity<String> crear(
			@Valid @RequestBody UsuarioDTO usuario
			) {
		int status = usuarioService.create(usuario);
		if(status == 0) return new ResponseEntity<String>("Usuario creado con exito", HttpStatus.CREATED);
		else if(status == 1) return new ResponseEntity<String>("Error al crear el usuario, el nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
		else if(status == 2) return new ResponseEntity<String>("Error al crear el usuario, el nombre de usuario solo puede contener caracteres alfanuméricos", HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<String>("Error al crear el usuario, la contraseña debe contener al menos una Mayúscula, una minúscula y un carácter especial", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Expone la lista global de usuarios registrados en el sistema.
	 * * @return ResponseEntity con los DTOs de los usuarios o un estado NO_CONTENT si se encuentra vacío
	 */
	@GetMapping("/mostrar")
	public ResponseEntity<List<UsuarioDTO>> mostrar() {
		List<UsuarioDTO> usuarios = usuarioService.getAll();
		if(!usuarios.isEmpty()) return new ResponseEntity<List<UsuarioDTO>>(usuarios, HttpStatus.OK);
		else return new ResponseEntity<List<UsuarioDTO>>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Actualiza los datos de un usuario y regenera de forma dinámica su token JWT con la información nueva.
	 * * @param id Identificador único del usuario (debe ser positivo)
	 * @param usuario DTO cargado con los datos a actualizar
	 * @return ResponseEntity con el mapa del nuevo token y rol del usuario, o el error correspondiente
	 */
	@PutMapping("/actualizar/{id}")
	public ResponseEntity<?> actualizar(
			@Positive(message = "El id del usuario debe ser positivo") @PathVariable long id,
			@Valid @RequestBody UsuarioDTO usuario
			) {
		int status = usuarioService.updateById(id, usuario);
		if(status == 0) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getNombreUsuario());
	        String jwt = jwtUtil.generateToken(userDetails);

	        String role = null;
	        if (userDetails instanceof Usuario user) {
	            role = user.getRol().name();
	        }

	        return ResponseEntity.ok(Map.of("token", jwt, "role", role));
		}
		else if(status == 1) return new ResponseEntity<String>("Error al actualizar el usuario, no existe el usuario", HttpStatus.NOT_FOUND);
		else if(status == 2) return new ResponseEntity<String>("Error al actualizar el usuario, el nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
		else if(status == 3) return new ResponseEntity<String>("Error al actualizar el usuario, el nombre de usuario solo puede contener caracteres alfanuméricos", HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<String>("Error al actualizar el usuario, la contraseña debe contener al menos una Mayúscula, una minúscula y un carácter especial", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Da de baja o elimina un usuario del sistema basándose en su ID.
	 * * @param id Identificador único del usuario a eliminar (debe ser positivo)
	 * @return ResponseEntity confirmando el estado final de la operación
	 */
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<String> eliminar(
			@Positive(message = "El id del usuario debe ser positivo") @PathVariable long id
			) {
		int status = usuarioService.deleteById(id);
		if(status == 0) return new ResponseEntity<String>("Usuario eliminado con exito", HttpStatus.OK);
		else return new ResponseEntity<String>("Error al eliminar el usuario, el usuario no existe", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Obtiene el total de usuarios registrados en la base de datos de la aplicación.
	 * * @return ResponseEntity con la cantidad exacta de usuarios
	 */
	@GetMapping("/contar")
	public ResponseEntity<Long> contar() {
		Long cantidad = usuarioService.count();
		if (cantidad != 0) return new ResponseEntity<Long>(cantidad, HttpStatus.OK);
		else return new ResponseEntity<Long>(cantidad, HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Localiza un usuario mediante su ID.
	 * * @param id Identificador único del usuario (debe ser positivo)
	 * @return ResponseEntity con el DTO del usuario hallado o un estado NOT_FOUND con un DTO vacío
	 */
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<UsuarioDTO> getById(
			@Positive(message = "El id del usuario debe ser positivo") @PathVariable long id
			) {
		UsuarioDTO encontrado = usuarioService.findById(id);
		if(encontrado != null) return new ResponseEntity<UsuarioDTO>(encontrado, HttpStatus.OK);
		else return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(), HttpStatus.NOT_FOUND);
	}
	
}
