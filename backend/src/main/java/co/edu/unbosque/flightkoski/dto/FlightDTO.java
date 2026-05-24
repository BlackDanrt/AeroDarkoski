package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de almacenar los números e 
 * identificadores operativos comerciales de un vuelo.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class FlightDTO {

	/** Código alfanumérico IATA representativo del vuelo comercial. */
	private String iataNumber;

	/** Código alfanumérico ICAO representativo del vuelo comercial. */
	private String icaoNumber;

	/** Número base interno o secuencial de asignación de ruta de vuelo. */
	private String number;

	/**
	 * Constructor por defecto de FlightDTO.
	 */
	public FlightDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor parametrizado para establecer la identificación del vuelo comercial.
	 * * @param iataNumber Código comercial IATA
	 * @param icaoNumber Código comercial ICAO
	 * @param number     Número específico de identificación del vuelo
	 */
	public FlightDTO(String iataNumber, String icaoNumber, String number) {
		super();
		this.iataNumber = iataNumber;
		this.icaoNumber = icaoNumber;
		this.number = number;
	}

	public String getIataNumber() {
		return iataNumber;
	}

	public void setIataNumber(String iataNumber) {
		this.iataNumber = iataNumber;
	}

	public String getIcaoNumber() {
		return icaoNumber;
	}

	public void setIcaoNumber(String icaoNumber) {
		this.icaoNumber = icaoNumber;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "FlightDTO [iataNumber=" + iataNumber + ", icaoNumber=" + icaoNumber + ", number=" + number + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(iataNumber, icaoNumber, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightDTO other = (FlightDTO) obj;
		return Objects.equals(iataNumber, other.iataNumber) && Objects.equals(icaoNumber, other.icaoNumber)
				&& Objects.equals(number, other.number);
	}

}
