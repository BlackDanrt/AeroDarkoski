package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar la información
 * identificadora de una aerolínea comercial.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class AirlineDTO {

	/** Código IATA de dos caracteres que identifica a la aerolínea. */
	private String iataCode;

	/** Código ICAO de tres caracteres que identifica a la aerolínea. */
	private String icaoCode;

	/**
	 * Constructor por defecto de AirlineDTO.
	 */
	public AirlineDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parámetros para inicializar los códigos identificadores de la aerolínea.
	 * * @param iataCode Código IATA corporativo
	 * @param icaoCode Código ICAO corporativo
	 */
	public AirlineDTO(String iataCode, String icaoCode) {
		super();
		this.iataCode = iataCode;
		this.icaoCode = icaoCode;
	}

	public String getIataCode() {
		return iataCode;
	}

	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}

	public String getIcaoCode() {
		return icaoCode;
	}

	public void setIcaoCode(String icaoCode) {
		this.icaoCode = icaoCode;
	}

	@Override
	public String toString() {
		return "Airline [iataCode=" + iataCode + ", icaoCode=" + icaoCode + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(iataCode, icaoCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AirlineDTO other = (AirlineDTO) obj;
		return Objects.equals(iataCode, other.iataCode) && Objects.equals(icaoCode, other.icaoCode);
	}

}
