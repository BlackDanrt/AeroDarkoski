package co.edu.unbosque.flightkoski.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.ClimaDTO;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

/**
 * Servicio encargado de gestionar y actualizar los reportes climáticos de los diferentes aeropuertos,
 * sincronizándose periódicamente con APIs externas de aviación.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Service
public class ClimaService {
	
	/** Manejador encargado del consumo de endpoints HTTP externos de meteorología. */
	@Autowired
	private ExternalHTPPRequestHandler handler;
	
	/** Servicio de auditoría utilizado para rastrear las lecturas de información climática. */
	@Autowired
	private AuditoriaService auditoriaService;
	
	/** Caché interna que almacena los últimos reportes climáticos procesados. */
	private List<ClimaDTO> listaClima = new ArrayList<>();
	
	/**
	 * Constructor por defecto del servicio.
	 */
	public ClimaService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Tarea programada (cada 30 minutos) que descarga y actualiza los reportes METAR 
	 * desde el servidor meteorológico externo.
	 */
	@Scheduled(fixedRate = 1800000)
	public void actualizarClima() {
		List<ClimaDTO> resultado = handler.getClima();
		if (resultado != null && !resultado.isEmpty()) {
			listaClima = resultado;
		}
	}
	
	/**
	 * Retorna la lista global de reportes climatológicos disponibles, auditando la operación.
	 * * @return Lista de objetos {@link ClimaDTO} con la información del clima actual.
	 */
	public List<ClimaDTO> obtenerTodos(){
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.CLIMA);
    	auditoriaService.create(auditoria);
		return listaClima;
	}
}
