package co.edu.unbosque.flightkoski.service;

import java.util.List;

/**
* Interfaz genérica que define el contrato fundamental para las operaciones
* de persistencia y lógica de negocio orientadas a un CRUD (Create, Read, Update, Delete).
* @param <T> Tipo de Objeto de Transferencia de Datos (DTO) que manejará el servicio.
* @author Santiago Ortiz
* @version 1.0
*/
public interface CRUDOperation<T>  {
	
	/**
	 * Permite crear un nuevo registro.
	 * 
	 * @param data información a registrar.
	 * @return 0 si se creó correctamente, 1 si ocurrió algún error.
	 */
	public int create(T data);

	/**
	 * Permite obtener todos los registros.
	 * 
	 * @return lista de elementos existentes.
	 */
	public List<T> getAll();

	/**
	 * Permite eliminar un registro usando su ID.
	 * 
	 * @param id identificador del registro.
	 * @return 0 si se eliminó correctamente, 1 si no se encontró.
	 */
	public int deleteById(Long id);

	/**
	 * Permite actualizar un registro existente.
	 * 
	 * @param id      identificador del registro.
	 * @param newData nueva información.
	 * @return 0 si se actualizó correctamente, 1 si no se encontró.
	 */
	public int updateById(Long id, T newData);

	/**
	 * Permite conocer la cantidad total de registros.
	 * 
	 * @return número total de elementos.
	 */
	public long count();

	/**
	 * Permite verificar si existe un registro por su ID.
	 * 
	 * @param id identificador del registro.
	 * @return true si existe, false en caso contrario.
	 */
	public boolean exist(Long id);
}