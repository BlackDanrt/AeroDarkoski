package co.edu.unbosque.flightkoski.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import co.edu.unbosque.flightkoski.util.enums.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad de base de datos vinculada a la tabla "usuariotaller". Implementa la
 * interfaz {@link UserDetails} de Spring Security para el manejo de sesiones
 * corporativas.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Entity
@Table(name = "usuarioavion")
public class Usuario implements UserDetails {

	/** Identificador único autoincremental del usuario en el sistema. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/** Nombre de cuenta único requerido para transacciones y accesos. */
	@Column(unique = true, nullable = false)
	private String nombreUsuario;

	/** Correo electronico del usuario */
	@Column(name = "correo", unique = true, nullable = false)
	@NotBlank(message = "El correo es obligatorio")
	private String correo;

	/** Clave secreta (encriptada habitualmente en el flujo de negocio). */
	@NotBlank(message = "La contraseña no puede ser nula o estar vacia")
	private String contrasenia;

	/**
	 * Jerarquía de permisos y roles del usuario mapeada como cadena de caracteres.
	 */
	@Enumerated(EnumType.STRING)
	Rol rol;

	/** Indica si la cuenta del usuario no ha expirado. */
	private boolean accountNonExpired;

	/** Indica si la cuenta del usuario no se encuentra en estado de bloqueo. */
	private boolean accountNonLocked;

	/**
	 * Indica si los tokens o credenciales de clave siguen vigentes en el tiempo.
	 */
	private boolean credentialsNonExpired;

	/**
	 * Estado lógico que define si el usuario se encuentra habilitado para operar.
	 */
	private boolean enabled;

	/**
	 * Constructor por defecto de Usuario. Configura el estado inicial por defecto
	 * con las propiedades de seguridad habilitadas y rol SIN_AUTENTICAR.
	 */
	public Usuario() {
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
		this.rol = Rol.SIN_AUTENTICAR;
	}

	/**
	 * Constructor básico para inicios de sesión o parametrizaciones simplificadas.
	 * 
	 * @param nombreUsuario Nombre único identificador
	 * @param correo        Correo electronico
	 * @param contrasenia   Texto de la contraseña
	 */
	public Usuario(String nombreUsuario,
			@Email(message = "El correo electrónico no es válido") @NotBlank(message = "El correo es obligatorio") String correo,
			@NotBlank(message = "La contraseña no puede ser nula o estar vacia") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String contrasenia) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.correo = correo;
		this.contrasenia = contrasenia;
	}

	/**
	 * Constructor parametrizado extendido para registrar usuarios con una jerarquía
	 * de acceso definida.
	 * 
	 * @param nombreUsuario Nombre de cuenta único
	 * @param correo        Correo electronico
	 * @param contrasenia   Clave secreta asignada
	 * @param rol           Enumeración de rol correspondiente
	 */
	public Usuario(String nombreUsuario,
			@Email(message = "El correo electrónico no es válido") @NotBlank(message = "El correo es obligatorio") String correo,
			@NotBlank(message = "La contraseña no puede ser nula o estar vacia") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String contrasenia,
			Rol rol) {
		this();
		this.nombreUsuario = nombreUsuario;
		this.correo = correo;
		this.contrasenia = contrasenia;
		this.rol = rol;
	}

	/**
	 * Obtiene las autoridades (roles) asignadas al usuario para la integración con
	 * Spring Security. Implementación mandatoria de {@link UserDetails}. * @return
	 * Colección de autoridades del usuario adaptadas bajo el formato "ROLE_"
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
	}

	@Override
	public String getPassword() {
		return contrasenia;
	}

	@Override
	public String getUsername() {
		return nombreUsuario;
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

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombreUsuario=" + nombreUsuario + ", correo=" + correo + ", contrasenia="
				+ contrasenia + ", rol=" + rol + ", accountNonExpired=" + accountNonExpired + ", accountNonLocked="
				+ accountNonLocked + ", credentialsNonExpired=" + credentialsNonExpired + ", enabled=" + enabled + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountNonExpired, accountNonLocked, contrasenia, correo, credentialsNonExpired, enabled,
				id, nombreUsuario, rol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return accountNonExpired == other.accountNonExpired && accountNonLocked == other.accountNonLocked
				&& Objects.equals(contrasenia, other.contrasenia) && Objects.equals(correo, other.correo)
				&& credentialsNonExpired == other.credentialsNonExpired && enabled == other.enabled && id == other.id
				&& Objects.equals(nombreUsuario, other.nombreUsuario) && rol == other.rol;
	}

}