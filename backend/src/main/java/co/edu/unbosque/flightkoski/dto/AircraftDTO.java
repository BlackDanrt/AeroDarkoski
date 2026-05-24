package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar la información
 * técnica e identificadores de una aeronave en el sistema.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class AircraftDTO {

	/** Código IATA único asignado al tipo o modelo de aeronave. */
	private String iataCode;

	/** Dirección transponder de 24 bits (ICAO24) asignada de forma única a la aeronave. */
	private String icao24;

	/** Código ICAO que identifica el modelo o tipo de aeronave. */
	private String icaoCode;

	/** Número de matrícula o registro oficial de la aeronave (Registration Number). */
	private String regNumber;

	/**
	 * Constructor por defecto de AircraftDTO.
	 */
	public AircraftDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parámetros completo para inicializar todos los datos de la aeronave.
	 * * @param iataCode  Código IATA de la aeronave
	 * @param icao24    Dirección transponder ICAO24
	 * @param icaoCode  Código ICAO de la aeronave
	 * @param regNumber Número de matrícula de registro
	 */
	public AircraftDTO(String iataCode, String icao24, String icaoCode, String regNumber) {
		super();
		this.iataCode = iataCode;
		this.icao24 = icao24;
		this.icaoCode = icaoCode;
		this.regNumber = regNumber;
	}

	public String getIataCode() {
		return iataCode;
	}

	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}

	public String getIcao24() {
		return icao24;
	}

	public void setIcao24(String icao24) {
		this.icao24 = icao24;
	}

	public String getIcaoCode() {
		return icaoCode;
	}

	public void setIcaoCode(String icaoCode) {
		this.icaoCode = icaoCode;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	@Override
	public String toString() {
		return "AircraftDTO [iataCode=" + iataCode + ", icao24=" + icao24 + ", icaoCode=" + icaoCode + ", regNumber="
				+ regNumber + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(iataCode, icao24, icaoCode, regNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AircraftDTO other = (AircraftDTO) obj;
		return Objects.equals(iataCode, other.iataCode) && Objects.equals(icao24, other.icao24)
				&& Objects.equals(icaoCode, other.icaoCode) && Objects.equals(regNumber, other.regNumber);
	}

}
