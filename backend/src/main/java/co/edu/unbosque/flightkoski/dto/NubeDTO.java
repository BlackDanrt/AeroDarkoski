package co.edu.unbosque.flightkoski.dto;

import java.util.Objects;

/**
 * Clase de Transferencia de Datos (DTO) encargada de representar las capas de
 * nubosidad, su cobertura y altitud informadas en los reportes de clima.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class NubeDTO {

	/** Tipo de cobertura o densidad de la nube registrada. */
	private String cover;

	/** Altura de la base de la capa de nubes medida en pies. */
	private int base;

	/**
	 * Constructor por defecto de NubeDTO.
	 */
	public NubeDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor parametrizado para instanciar las condiciones físicas de la nubosidad.
	 * * @param cover Estado o tipo de cobertura nubosa
	 * @param base  Altitud base calculada en pies
	 */
	public NubeDTO(String cover, int base) {
		super();
		this.cover = cover;
		this.base = base;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	@Override
	public String toString() {
		return "NubeDTO [cover=" + cover + ", base=" + base + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(base, cover);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NubeDTO other = (NubeDTO) obj;
		return base == other.base && Objects.equals(cover, other.cover);
	}

}