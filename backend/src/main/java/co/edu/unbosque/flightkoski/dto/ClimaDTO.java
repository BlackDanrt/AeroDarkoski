package co.edu.unbosque.flightkoski.dto;

import java.util.List;
import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar la información
 * climatológica y meteorológica detallada de una zona o aeropuerto en el sistema.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class ClimaDTO {

	/** Identificador ICAO de cuatro letras del aeropuerto al que pertenece el reporte climatológico. */
	private String icaoId;

	/** Temperatura actual registrada en grados Celsius. */
	private double temp;

	/** Punto de rocío (Dew point) registrado en grados Celsius. */
	private double dewp;

	/** Dirección del viento. */
	private String wdir;

	/** Velocidad del viento registrada. */
	private int wspd;

	/** Visibilidad horizontal registrada en la zona. */
	private String visib;

	/** Presión atmosférica ajustada al nivel del mar. */
	private double altim;

	/** Lista de capas de nubes o nubosidad detectadas en el espacio aéreo. */
	private List<NubeDTO> clouds;

	/** Hora y fecha textual de la emisión del reporte meteorológico. */
	private String reportTime;

	/**
	 * Constructor por defecto de ClimaDTO.
	 */
	public ClimaDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parámetros completo para inicializar todas las variables climáticas.
	 * * @param icaoId     Identificador ICAO del aeropuerto
	 * @param temp       Temperatura registrada
	 * @param dewp       Punto de rocío
	 * @param wdir       Dirección del viento
	 * @param wspd       Velocidad del viento
	 * @param visib      Visibilidad horizontal
	 * @param altim      Presión atmosférica
	 * @param clouds     Lista de nubosidades detectadas
	 * @param reportTime Hora de actualización del reporte
	 */
	public ClimaDTO(String icaoId, double temp, double dewp, String wdir, int wspd, String visib, double altim,
			List<NubeDTO> clouds, String reportTime) {
		super();
		this.icaoId = icaoId;
		this.temp = temp;
		this.dewp = dewp;
		this.wdir = wdir;
		this.wspd = wspd;
		this.visib = visib;
		this.altim = altim;
		this.clouds = clouds;
		this.reportTime = reportTime;
	}

	public String getIcaoId() {
		return icaoId;
	}

	public void setIcaoId(String icaoId) {
		this.icaoId = icaoId;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getDewp() {
		return dewp;
	}

	public void setDewp(double dewp) {
		this.dewp = dewp;
	}

	public String getWdir() {
		return wdir;
	}

	public void setWdir(String wdir) {
		this.wdir = wdir;
	}

	public int getWspd() {
		return wspd;
	}

	public void setWspd(int wspd) {
		this.wspd = wspd;
	}

	public String getVisib() {
		return visib;
	}

	public void setVisib(String visib) {
		this.visib = visib;
	}

	public double getAltim() {
		return altim;
	}

	public void setAltim(double altim) {
		this.altim = altim;
	}

	public List<NubeDTO> getClouds() {
		return clouds;
	}

	public void setClouds(List<NubeDTO> clouds) {
		this.clouds = clouds;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	@Override
	public String toString() {
		return "ClimaDTO [icaoId=" + icaoId + ", temp=" + temp + ", dewp=" + dewp + ", wdir=" + wdir + ", wspd=" + wspd
				+ ", visib=" + visib + ", altim=" + altim + ", clouds=" + clouds + ", reportTime=" + reportTime + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(altim, clouds, dewp, icaoId, reportTime, temp, visib, wdir, wspd);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClimaDTO other = (ClimaDTO) obj;
		return Double.doubleToLongBits(altim) == Double.doubleToLongBits(other.altim)
				&& Objects.equals(clouds, other.clouds)
				&& Double.doubleToLongBits(dewp) == Double.doubleToLongBits(other.dewp)
				&& Objects.equals(icaoId, other.icaoId) && Objects.equals(reportTime, other.reportTime)
				&& Double.doubleToLongBits(temp) == Double.doubleToLongBits(other.temp)
				&& Objects.equals(visib, other.visib) && Objects.equals(wdir, other.wdir) && wspd == other.wspd;
	}

}
