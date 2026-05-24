package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar la información
 * del origen o aeropuerto de salida de un vuelo.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class DepartureDTO {

	/** Código IATA de tres letras correspondiente al aeropuerto de origen. */
	private String iataCode;

	/** Código ICAO de cuatro letras correspondiente al aeropuerto de origen. */
	private String icaoCode;

	/**
	 * Constructor por defecto de DepartureDTO.
	 */
	public DepartureDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor parametrizado para definir la ubicación aeroportuaria de salida.
	 * * @param iataCode Código IATA de origen
	 * @param icaoCode Código ICAO de origen
	 */
	public DepartureDTO(String iataCode, String icaoCode) {
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
		return "DepartureDTO [iataCode=" + iataCode + ", icaoCode=" + icaoCode + "]";
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
		DepartureDTO other = (DepartureDTO) obj;
		return Objects.equals(iataCode, other.iataCode) && Objects.equals(icaoCode, other.icaoCode);
	}

}
