package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase compuesta de Transferencia de Datos (DTO) que unifica el estado global, 
 * telemetría, rutas y metadatos de un vuelo rastreado en tiempo real.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class AvionDTO {

	/** Objeto DTO que anida las especificaciones físicas de la aeronave. */
	private AircraftDTO aircraft;

	/** Objeto DTO que especifica la empresa transportadora dueña del vuelo. */
	private AirlineDTO airline;

	/** Estructura DTO con el aeropuerto planificado de destino. */
	private ArrivalDTO arrival;

	/** Estructura DTO con el aeropuerto planificado de procedencia. */
	private DepartureDTO departure;

	/** Identificadores de vuelo comercial, incluyendo números operativos. */
	private FlightDTO flight;

	/** Información de posición geográfica activa. */
	private GeographyDTO geography;

	/** Telemetría asociada a la velocidad horizontal y vertical de avance. */
	private SpeedDTO speed;

	/** Estado situacional del avión. */
	private String status;

	/**
	 * Constructor por defecto de AvionDTO.
	 */
	public AvionDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor compuesto estructurado con todos los sub-objetos de rastreo y telemetría aérea.
	 * * @param aircraft  Componente estructural técnico
	 * @param airline   Componente operativo de aerolínea
	 * @param arrival   Metadatos de llegada
	 * @param departure Metadatos de salida
	 * @param flight    Información identificadora del viaje
	 * @param geography Posicionamiento dinámico espacial
	 * @param speed     Registros cinemáticos y de velocidad
	 * @param status    Etiqueta situacional de estado
	 */
	public AvionDTO(AircraftDTO aircraft, AirlineDTO airline, ArrivalDTO arrival, DepartureDTO departure,
			FlightDTO flight, GeographyDTO geography, SpeedDTO speed, String status) {
		super();
		this.aircraft = aircraft;
		this.airline = airline;
		this.arrival = arrival;
		this.departure = departure;
		this.flight = flight;
		this.geography = geography;
		this.speed = speed;
		this.status = status;
	}

	public AircraftDTO getAircraft() {
		return aircraft;
	}

	public void setAircraft(AircraftDTO aircraft) {
		this.aircraft = aircraft;
	}

	public AirlineDTO getAirline() {
		return airline;
	}

	public void setAirline(AirlineDTO airline) {
		this.airline = airline;
	}

	public ArrivalDTO getArrival() {
		return arrival;
	}

	public void setArrival(ArrivalDTO arrival) {
		this.arrival = arrival;
	}

	public DepartureDTO getDeparture() {
		return departure;
	}

	public void setDeparture(DepartureDTO departure) {
		this.departure = departure;
	}

	public FlightDTO getFlight() {
		return flight;
	}

	public void setFlight(FlightDTO flight) {
		this.flight = flight;
	}

	public GeographyDTO getGeography() {
		return geography;
	}

	public void setGeography(GeographyDTO geography) {
		this.geography = geography;
	}

	public SpeedDTO getSpeed() {
		return speed;
	}

	public void setSpeed(SpeedDTO speed) {
		this.speed = speed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AvionDTO [aircraft=" + aircraft + ", airline=" + airline + ", arrival=" + arrival + ", departure="
				+ departure + ", flight=" + flight + ", geography=" + geography + ", speed=" + speed + ", status="
				+ status + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(aircraft, airline, arrival, departure, flight, geography, speed, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvionDTO other = (AvionDTO) obj;
		return Objects.equals(aircraft, other.aircraft) && Objects.equals(airline, other.airline)
				&& Objects.equals(arrival, other.arrival) && Objects.equals(departure, other.departure)
				&& Objects.equals(flight, other.flight) && Objects.equals(geography, other.geography)
				&& Objects.equals(speed, other.speed) && Objects.equals(status, other.status);
	}

}
