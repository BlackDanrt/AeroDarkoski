package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

import co.edu.unbosque.flightkoski.util.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar la información
 * de credenciales y datos de perfil de un usuario del sistema. 
 * @author Santiago Ortiz
 * @version 1.0
 */
public class UsuarioDTO {

	/** Identificador único asignado al usuario. */
	private long id;

	/** Nombre único de usuario requerido para el acceso al sistema. */
	@NotBlank(message = "El nombre de usuario es obligatorio")
	private String nombreUsuario;

	/** Correo electronico del usuario */
	@Email(message = "El correo electrónico no es válido")
	@NotBlank(message = "El correo es obligatorio")
	private String correo;

	/** Contraseña del usuario codificada o en texto plano según el proceso. */
	private String contrasenia;

	/** Rol del usuario en el sistema. */
	private Rol rol;

	/**
	 * Constructor por defecto de UsuarioDTO.
	 */
	public UsuarioDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parámetros completo para inicializar credenciales y rol.
	 * 
	 * @param nombreUsuario Cuenta de usuario
	 * @param correo        Corre electronico
	 * @param contrasenia   Clave secreta
	 * @param rol           Privilegios asociados
	 */
	public UsuarioDTO(String nombreUsuario, String correo, String contrasenia, Rol rol) {
		this.nombreUsuario = nombreUsuario;
		this.correo = correo;
		this.contrasenia = contrasenia;
		this.rol = rol;
	}

	/**
	 * Constructor simplificado para inicios de sesión o registros básicos.
	 * 
	 * @param nombreUsuario Cuenta de usuario
	 * @param correo        Correo electronico
	 * @param contrasenia   Clave secreta
	 */
	public UsuarioDTO(String nombreUsuario, String correo, String contrasenia) {
		this.nombreUsuario = nombreUsuario;
		this.correo = correo;
		this.contrasenia = contrasenia;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", nombreUsuario=" + nombreUsuario + ", correo=" + correo + ", contrasenia="
				+ contrasenia + ", rol=" + rol + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, correo, id, nombreUsuario, rol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia) && Objects.equals(correo, other.correo) && id == other.id
				&& Objects.equals(nombreUsuario, other.nombreUsuario) && rol == other.rol;
	}

}