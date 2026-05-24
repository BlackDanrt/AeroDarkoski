package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

import co.edu.unbosque.flightkoski.util.enums.TipoBusqueda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar las búsquedas
 * individuales y consultas almacenadas en el historial de navegación de los usuarios.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class HistorialDTO {

	/** Identificador único secuencial del registro del historial. */
	private long id;

	/** Identificador único del usuario dueño de la consulta. */
	@Positive(message = "El id del usuario no puede ser negativo")
	private long idUsuario;

	/** Cadena de texto correspondiente al criterio o palabra clave buscada. */
	@NotBlank(message = "La busqueda a realizar no puede estar vacia o ser nula")
	private String busqueda;

	/** Categoría o filtro de búsqueda utilizado (por aerolínea, vuelo, clima, etc.). */
	@NotNull(message = "El tipo de busqueda no puede ser nulo")
	private TipoBusqueda tipoBusqueda;

	/**
	 * Constructor por defecto de HistorialDTO.
	 */
	public HistorialDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parámetros para la inicialización directa de nuevas consultas a registrar.
	 * * @param idUsuario    ID del usuario que realiza la acción
	 * @param busqueda     Texto o código consultado
	 * @param tipoBusqueda Enum que clasifica el tipo de criterio de búsqueda
	 */
	public HistorialDTO(
			@Positive(message = "El id del usuario no puede ser negativo") long idUsuario,
			@NotBlank(message = "La busqueda a realizar no puede estar vacia o ser nula") String busqueda,
			@NotNull(message = "El tipo de busqueda no puede ser nulo") TipoBusqueda tipoBusqueda) {
		super();
		this.idUsuario = idUsuario;
		this.busqueda = busqueda;
		this.tipoBusqueda = tipoBusqueda;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}

	public TipoBusqueda getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(TipoBusqueda tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	@Override
	public String toString() {
		return "HistorialDTO [id=" + id + ", idUsuario=" + idUsuario + ", busqueda=" + busqueda + ", tipoBusqueda="
				+ tipoBusqueda + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(busqueda, id, idUsuario, tipoBusqueda);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistorialDTO other = (HistorialDTO) obj;
		return Objects.equals(busqueda, other.busqueda) && id == other.id && idUsuario == other.idUsuario
				&& tipoBusqueda == other.tipoBusqueda;
	}

}
