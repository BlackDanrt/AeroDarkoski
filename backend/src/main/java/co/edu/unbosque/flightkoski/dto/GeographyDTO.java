package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de modelar el posicionamiento
 * geográfico, espacial y rumbo de navegación de una aeronave en tiempo real.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class GeographyDTO {

	/** Altitud actual calculada del avión respecto al nivel del mar. */
	private Double altitude;

	/** Dirección o rumbo de orientación de navegación medido en grados. */
	private Double direction;

	/** Coordenada de latitud geográfica donde se encuentra el avión. */
	private Double latitude;

	/** Coordenada de longitud geográfica donde se encuentra el avión. */
	private Double longitude;

	/**
	 * Constructor por defecto de GeographyDTO.
	 */
	public GeographyDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parámetros completo para inicializar la geolocalización y rumbo del avión.
	 * * @param altitude  Altitud de vuelo
	 * @param direction Rumbo u orientación en grados
	 * @param latitude  Coordenada decimal de latitud
	 * @param longitude Coordenada decimal de longitud
	 */
	public GeographyDTO(Double altitude, Double direction, Double latitude, Double longitude) {
		super();
		this.altitude = altitude;
		this.direction = direction;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public Double getDirection() {
		return direction;
	}

	public void setDirection(Double direction) {
		this.direction = direction;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "GeographyDTO [altitude=" + altitude + ", direction=" + direction + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(altitude, direction, latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeographyDTO other = (GeographyDTO) obj;
		return Objects.equals(altitude, other.altitude) && Objects.equals(direction, other.direction)
				&& Objects.equals(latitude, other.latitude) && Objects.equals(longitude, other.longitude);
	}

}