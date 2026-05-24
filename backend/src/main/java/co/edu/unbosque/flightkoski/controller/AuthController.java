package co.edu.unbosque.flightkoski.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.flightkoski.dto.UsuarioDTO;
import co.edu.unbosque.flightkoski.entity.Usuario;
import co.edu.unbosque.flightkoski.security.JwtUtil;
import co.edu.unbosque.flightkoski.service.UsuarioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

/**
 * Controlador REST encargado de gestionar los procesos de autenticación 
 * y registro de usuarios en el sistema.
 * @author Santiago Ortiz
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
@Validated
@CrossOrigin(origins = { "https://aerodarkoski.netlify.app", "https://aerodarkoskicelular.netlify.app"})
public class AuthController {

	 /** Gestor de autenticación para validar credenciales de usuario. */
	  private final AuthenticationManager authenticationManager;

	  /** Utilidad para operaciones con tokens JWT. */
	  private final JwtUtil jwtUtil;
	  
	  /** Servicio para operaciones relacionadas con usuarios. */
	  private UsuarioService usuarioService;
	  	  
	  /**
	   * Constructor de la clase AuthController que inyecta las dependencias requeridas.
	   * * @param authenticationManager Gestor de autenticación de Spring Security
	   * @param jwtUtil Utilidad para el manejo de JSON Web Tokens
	   * @param usuarioSevice Servicio de lógica de negocio de usuarios
	   */
	  public AuthController(
		      AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService usuarioSevice) {
		    this.authenticationManager = authenticationManager;
		    this.jwtUtil = jwtUtil;
		    this.usuarioService = usuarioSevice;
		  }
	  
	  /**
	   * Autentica un usuario y genera un token JWT si las credenciales son válidas.
	   * * @param loginRequest DTO que contiene el nombre de usuario y contraseña
	   * @return ResponseEntity con el token de acceso y rol del usuario, o un estado UNAUTHORIZED en caso de fallo
	   */
	  @PostMapping("/login")
	  public ResponseEntity<?> login(
	      @Parameter(
	              description = "Credenciales de usuario para iniciar sesión",
	              required = true,
	              schema = @Schema(implementation = UsuarioDTO.class),
	              examples =
	                  @ExampleObject(
	                      value =
	                          """
	                    {
	                      "username": "admin",
	                      "password": "1234567890"
	                    }
	                """))
	          @RequestBody
	          UsuarioDTO loginRequest) {
	    try {
	      Authentication authentication =
	          authenticationManager.authenticate(
	              new UsernamePasswordAuthenticationToken(
	                  loginRequest.getNombreUsuario(), loginRequest.getContrasenia()));

	      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	      String jwt = jwtUtil.generateToken(userDetails);

	      String role = null;
	      if (userDetails instanceof Usuario) {
	        Usuario user = (Usuario) userDetails;
	        role = user.getRol().name();
	      }

	      return ResponseEntity.ok(new AuthResponse(jwt, role));
	    } catch (AuthenticationException e) {
	      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	          .body("Nombre de usuario o contraseña inválidos o usuario no encontrado");
	    }
	  }
	  
	  /**
	   * Registra un nuevo usuario validando que el nombre de usuario esté disponible.
	   * * @param registerRequest DTO con los datos del nuevo usuario
	   * @return ResponseEntity indicando el éxito de la operación o un mensaje de error con su respectivo código de estado
	   */
	  @PostMapping("/register")
	  public ResponseEntity<?> register(
	      @Parameter(
	              description = "Información del nuevo usuario",
	              required = true,
	              schema = @Schema(implementation = UsuarioDTO.class),
	              examples =
	                  @ExampleObject(
	                      value =
	                          """
	                    {
	                      "username": "nuevoUsuario",
	                      "password": "contraseña123"
	                    }
	                """))
	          @Valid @RequestBody
	          UsuarioDTO registerRequest) {
	    // Verificar si el nombre de usuario ya existe
	    if (usuarioService.existsByNombreUsuario(registerRequest.getNombreUsuario())) {
	      return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe");
	    }

	    // Crear nuevo usuario
	    int status = usuarioService.create(registerRequest);
	    if(status == 0) return new ResponseEntity<String>("Usuario creado con exito", HttpStatus.CREATED);
		else if(status == 1) return new ResponseEntity<String>("Error al crear el usuario, el nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<String>("Error al crear el usuario, el nombre de usuario solo puede contener caracteres alfanuméricos", HttpStatus.BAD_REQUEST);
	    }
	  
	  
	  /**
	   * Clase interna para representar la respuesta de autenticación. Contiene el token JWT y el rol
	   * del usuario autenticado.
	   * * @author Juan Martinez
	   * @version 1.0
	   */
	  private static class AuthResponse {
	    /** Token JWT generado para el usuario autenticado. */
	    private final String token;

	    /** Rol del usuario autenticado. */
	    private final String role;

	    /**
	     * Constructor con solo token.
	     *
	     * @param token Token JWT generado
	     */
	    public AuthResponse(String token) {
	      this.token = token;
	      // Extraer rol del token
	      this.role = null; // Se establecerá en el constructor con el parámetro de rol
	    }

	    /**
	     * Constructor con token y rol.
	     *
	     * @param token Token JWT generado
	     * @param role Rol del usuario
	     */
	    public AuthResponse(String token, String role) {
	      this.token = token;
	      this.role = role;
	    }

	    /**
	     * Obtiene el token JWT.
	     *
	     * @return Token JWT
	     */
	    public String getToken() {
	      return token;
	    }

	    /**
	     * Obtiene el rol del usuario.
	     *
	     * @return Rol del usuario
	     */
	    public String getRole() {
	      return role;
	    }
	  }
}
