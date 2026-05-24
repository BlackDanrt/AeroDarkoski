package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de agrupar los indicadores cinemáticos
 * y de velocidad (tanto horizontal como vertical) de un avión rastreado.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class SpeedDTO {

	/** Magnitud de la velocidad de avance horizontal de la aeronave. */
	private Double horizontal;

	/** Indicador o factor que evalúa si el avión se encuentra actualmente en tierra. */
	private Double isGround;

	/** Magnitud de la velocidad vertical de ascenso o descenso (Vertical Speed). */
	private Double vspeed;

	/**
	 * Constructor por defecto de SpeedDTO.
	 */
	public SpeedDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor parametrizado con los vectores físicos cinemáticos del vuelo.
	 * * @param horizontal Velocidad horizontal de desplazamiento
	 * @param isGround   Mapeo de estado en pista / tierra
	 * @param vspeed     Velocidad vertical de ascenso o descenso
	 */
	public SpeedDTO(Double horizontal, Double isGround, Double vspeed) {
		super();
		this.horizontal = horizontal;
		this.isGround = isGround;
		this.vspeed = vspeed;
	}

	public Double getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(Double horizontal) {
		this.horizontal = horizontal;
	}

	public Double getIsGround() {
		return isGround;
	}

	public void setIsGround(Double isGround) {
		this.isGround = isGround;
	}

	public Double getVspeed() {
		return vspeed;
	}

	public void setVspeed(Double vspeed) {
		this.vspeed = vspeed;
	}

	@Override
	public String toString() {
		return "SpeedDTO [horizontal=" + horizontal + ", isGround=" + isGround + ", vspeed=" + vspeed + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(horizontal, isGround, vspeed);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpeedDTO other = (SpeedDTO) obj;
		return Objects.equals(horizontal, other.horizontal) && Objects.equals(isGround, other.isGround)
				&& Objects.equals(vspeed, other.vspeed);
	}

}
