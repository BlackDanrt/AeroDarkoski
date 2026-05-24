package co.edu.unbosque.flightkoski.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import co.edu.unbosque.flightkoski.util.enums.Rol;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar la información
 * detallada de los registros de auditoría y trazas de actividad dentro de la aplicación.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class AuditoriaDTO {

	/** Identificador único secuencial del registro de auditoría. */
	private long id;

	/** Código identificador del usuario que efectuó la acción. */
	private Long idUsuario;

	/** Nombre representativo o login del usuario involucrado. */
	private String nombreUsuario;

	/** Categoría o tipo de acción ejecutada. */
	private TipoAccion accion;

	/** Nivel de permisos o rol que poseía el usuario al realizar la acción. */
	private Rol rolUsuario;

	/** Identificación del módulo o servicio interno consumido durante el evento. */
	private Servicio servicioUsado;

	/** Fecha y hora exacta del sistema en la que quedó registrada la transacción. */
	private LocalDateTime fecha;

	/**
	 * Constructor por defecto de AuditoriaDTO.
	 */
	public AuditoriaDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor parametrizado completo para el mapeo y registro de transacciones de auditoría.
	 * * @param idUsuario     ID del operador
	 * @param nombreUsuario Credencial textual del operador
	 * @param accion        Tipo de evento gatillado
	 * @param rolUsuario    Rol activo en la sesión
	 * @param servicioUsado Capa de negocio o servicio invocado
	 * @param fecha         Estampa temporal del suceso
	 */
	public AuditoriaDTO(Long idUsuario, String nombreUsuario, TipoAccion accion, Rol rolUsuario, Servicio servicioUsado,
			LocalDateTime fecha) {
		super();
		this.idUsuario = idUsuario;
		this.nombreUsuario = nombreUsuario;
		this.accion = accion;
		this.rolUsuario = rolUsuario;
		this.servicioUsado = servicioUsado;
		this.fecha = fecha;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public TipoAccion getAccion() {
		return accion;
	}

	public void setAccion(TipoAccion accion) {
		this.accion = accion;
	}

	public Rol getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(Rol rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Servicio getServicioUsado() {
		return servicioUsado;
	}

	public void setServicioUsado(Servicio servicioUsado) {
		this.servicioUsado = servicioUsado;
	}

	@Override
	public String toString() {
		return "AuditoriaDTO [id=" + id + ", idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + ", accion="
				+ accion + ", rolUsuario=" + rolUsuario + ", servicioUsado=" + servicioUsado + ", fecha=" + fecha + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(accion, fecha, id, idUsuario, nombreUsuario, rolUsuario, servicioUsado);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditoriaDTO other = (AuditoriaDTO) obj;
		return accion == other.accion && Objects.equals(fecha, other.fecha) && id == other.id
				&& Objects.equals(idUsuario, other.idUsuario) && Objects.equals(nombreUsuario, other.nombreUsuario)
				&& rolUsuario == other.rolUsuario && servicioUsado == other.servicioUsado;
	}

}
