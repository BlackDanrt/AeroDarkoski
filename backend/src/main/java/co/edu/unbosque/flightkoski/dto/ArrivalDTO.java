package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar la información
 * del destino o aeropuerto de llegada de un vuelo.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class ArrivalDTO {

	/** Código IATA de tres letras correspondiente al aeropuerto de destino. */
	private String iataCode;

	/** Código ICAO de cuatro letras correspondiente al aeropuerto de destino. */
	private String icaoCode;

	/**
	 * Constructor por defecto de ArrivalDTO.
	 */
	public ArrivalDTO() {
		super();
	}

	/**
	 * Constructor parametrizado para definir la ubicación aeroportuaria de arribo.
	 * * @param iataCode Código IATA de destino
	 * @param icaoCode Código ICAO de destino
	 */
	public ArrivalDTO(String iataCode, String icaoCode) {
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
		return "ArrivalDTO [iataCode=" + iataCode + ", icaoCode=" + icaoCode + "]";
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
		ArrivalDTO other = (ArrivalDTO) obj;
		return Objects.equals(iataCode, other.iataCode) && Objects.equals(icaoCode, other.icaoCode);
	}

}
