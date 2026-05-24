package co.edu.unbosque.flightkoski.entity;

import java.util.Objects;

import co.edu.unbosque.flightkoski.util.enums.TipoBusqueda;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Entidad de persistencia mapeada a la tabla "historialavion", encargada de modelar
 * las consultas guardadas y el comportamiento de búsquedas de los usuarios en la aplicación.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Entity
@Table(name = "historialavion")
public class Historial {

	/** Identificador único secuencial del registro de historial. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/** Identificador del usuario propietario de la consulta realizada. */
	@Positive(message = "El id del usuario no puede ser negativo")
	private long idUsuario;

	/** Cadena de texto exacta que describe el criterio, código o palabra clave buscada. */
	@NotBlank(message = "La busqueda a realizar no puede estar vacia o ser nula")
	private String busqueda;

	/** Clasificación de la búsqueda. */
	@Enumerated(EnumType.STRING)
	@NotNull(message = "El tipo de busqueda no puede ser nulo")
	private TipoBusqueda tipoBusqueda;

	/**
	 * Constructor por defecto de Historial.
	 */
	public Historial() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor parametrizado para la instanciación de registros de búsquedas en persistencia.
	 * * @param idUsuario    ID del usuario que genera la consulta
	 * @param busqueda     Texto ingresado en la consulta
	 * @param tipoBusqueda Filtro operativo de la búsqueda
	 */
	public Historial(
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
		return "Historial [id=" + id + ", idUsuario=" + idUsuario + ", busqueda=" + busqueda + ", tipoBusqueda="
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
		Historial other = (Historial) obj;
		return Objects.equals(busqueda, other.busqueda) && id == other.id && idUsuario == other.idUsuario
				&& tipoBusqueda == other.tipoBusqueda;
	}

}
