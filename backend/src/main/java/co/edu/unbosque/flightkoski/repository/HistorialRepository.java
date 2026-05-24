package co.edu.unbosque.flightkoski.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.flightkoski.entity.Historial;

/**
 * Interfaz de Repositorio encargada de gestionar las consultas físicas sobre los registros 
 * de la entidad {@link Historial} en la base de datos.
 * @author Santiago Ortiz
 * @version 1.0
 */
public interface HistorialRepository extends JpaRepository<Historial, Long>{

	/**
	 * Recupera la lista completa de búsquedas realizadas históricamente por un usuario específico.
	 * * @param idUsuario Identificador único del usuario consultado
	 * @return Lista de objetos {@link Historial} asociados al identificador dado
	 */
	List<Historial> findByIdUsuario(long idUsuario);
	
}
