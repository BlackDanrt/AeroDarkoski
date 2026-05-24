package co.edu.unbosque.flightkoski.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.AvionDTO;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;
import jakarta.annotation.PostConstruct;

/**
 * Servicio encargado de la gestión, consulta y filtrado de aeronaves en tiempo real,
 * integrando peticiones HTTP externas y aplicando la traza de auditoría correspondiente.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Service
public class AvionService {

	/** Manejador de peticiones HTTP externas encargado de consumir las APIs aeronáuticas. */
	@Autowired
    private ExternalHTPPRequestHandler handler;
	
	/** Servicio transversal para el registro de auditorías en cada operación de lectura. */
	@Autowired
	private AuditoriaService auditoriaService;
	
	/** Almacenamiento en caché local de la lista de aviones recuperados. */
    private List<AvionDTO> aviones = new ArrayList<>();

    /**
     * Constructor por defecto del servicio.
     */
    public AvionService() {
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Actualiza el listado en caché de los aviones consultando el proveedor externo de datos.
     */
    @Scheduled(fixedRate = 900000) 
    public void actualizarAviones() {
        List<AvionDTO> resultado = handler.getAviones();
        if (resultado != null && !resultado.isEmpty()) {
            aviones = resultado;
        }
    }
    
    /**
     * Recupera todos los aviones almacenados actualmente en el sistema, registrando la acción en auditoría.
     * * @return Lista completa de objetos {@link AvionDTO}.
     */
    public List<AvionDTO> obtenerTodos() {
    	AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.AVION);
    	auditoriaService.create(auditoria);
        return aviones;
    }

    /**
     * Filtra los aviones cuyo código ICAO de llegada coincida con el proporcionado.
     * * @param icao Cadena de caracteres con el código ICAO del aeropuerto de destino.
     * @return Lista filtrada de objetos {@link AvionDTO}.
     */
    public List<AvionDTO> findByArrIcao(String icao) {
    	AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.AVION);
    	auditoriaService.create(auditoria);
        return aviones.stream()
            .filter(avion -> avion.getArrival() != null 
                && avion.getArrival().getIcaoCode().equalsIgnoreCase(icao))
            .toList();
    }

    /**
     * Filtra los aviones cuyo código IATA de llegada coincida con el proporcionado.
     * * @param iata Cadena de caracteres con el código IATA del aeropuerto de destino.
     * @return Lista filtrada de objetos {@link AvionDTO}.
     */
    public List<AvionDTO> findByArrIata(String iata) {
    	AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.AVION);
    	auditoriaService.create(auditoria);
        return aviones.stream()
            .filter(avion -> avion.getArrival() != null 
                && avion.getArrival().getIataCode().equalsIgnoreCase(iata))
            .toList();
    }

    /**
     * Filtra los aviones basándose en el número identificador IATA del vuelo.
     * * @param flightIata Cadena identificadora IATA del vuelo.
     * @return Lista filtrada de objetos {@link AvionDTO}.
     */
    public List<AvionDTO> findByFlightIata(String flightIata) {
    	AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.AVION);
    	auditoriaService.create(auditoria);
        return aviones.stream()
            .filter(avion -> avion.getFlight() != null 
                && avion.getFlight().getIataNumber().equalsIgnoreCase(flightIata))
            .toList();
    }

    /**
     * Filtra los aviones basándose en el número identificador ICAO del vuelo.
     * * @param flightIcao Cadena identificadora ICAO del vuelo.
     * @return Lista filtrada de objetos {@link AvionDTO}.
     */
    public List<AvionDTO> findByFlightIcao(String flightIcao) {
    	AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.AVION);
    	auditoriaService.create(auditoria);
        return aviones.stream()
            .filter(avion -> avion.getFlight() != null 
                && avion.getFlight().getIcaoNumber().equalsIgnoreCase(flightIcao))
            .toList();
    }

}