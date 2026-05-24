package co.edu.unbosque.flightkoski.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.HistorialDTO;
import co.edu.unbosque.flightkoski.entity.Historial;
import co.edu.unbosque.flightkoski.entity.Usuario;
import co.edu.unbosque.flightkoski.repository.HistorialRepository;
import co.edu.unbosque.flightkoski.repository.UsuarioRepository;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

/**
 * Servicio encargado de gestionar el almacenamiento y recuperación de las consultas de búsqueda
 * realizadas por los usuarios registrados en el sistema, auditando de forma estricta sus operaciones.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Service
public class HistorialService implements CRUDOperation<HistorialDTO> {

	/** Repositorio de persistencia de datos asociado a las entidades del Historial. */
	@Autowired
	private HistorialRepository historialRepository;

	/** Repositorio de datos del Usuario utilizado para validar la integridad referencial. */
	@Autowired
	private UsuarioRepository usuarioRepository;

	/** Componente encargado del mapeo relacional de datos entre estructuras de datos DTO y Entidades. */
	@Autowired
	private ModelMapper modelMapper;

	/** Servicio transversal encargado de computar y registrar auditorías automáticas de las consultas realizadas. */
	@Autowired
	private AuditoriaService auditoriaService;
	
	/**
	 * Constructor por defecto del servicio.
	 */
	public HistorialService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Registra una consulta de búsqueda en el historial de un usuario, garantizando que el usuario exista en el sistema.
	 * * @param data Objeto contenedor de datos DTO con el cuerpo del historial a persistir.
	 * @return Código de respuesta: 0 si fue exitoso, 1 si el usuario asociado no se encuentra registrado.
	 */
	@Override
	public int create(HistorialDTO data) {
		Optional<Usuario> encontrado = usuarioRepository.findById(data.getIdUsuario());
		if (!encontrado.isPresent())
			return 1;
		Historial entity = modelMapper.map(data, Historial.class);
		historialRepository.save(entity);
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.CREATE, Servicio.HISTORIAL);
    	auditoriaService.create(auditoria);
		return 0;
	}

	/**
	 * Recupera la totalidad de los historiales guardados dentro del ecosistema administrativo.
	 * * @return Lista completa mapeada a contenedores {@link HistorialDTO}.
	 */
	@Override
	public List<HistorialDTO> getAll() {
		List<Historial> entityList = historialRepository.findAll();
		List<HistorialDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			HistorialDTO dto = modelMapper.map(entity, HistorialDTO.class);
			dtoList.add(dto);
		});
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.HISTORIAL);
    	auditoriaService.create(auditoria);
		return dtoList;
	}

	/**
	 * Remueve una entrada del historial de búsquedas del sistema a través de su ID único.
	 * * @param id Identificador numérico único de la traza de historial a eliminar.
	 * @return Código de respuesta: 0 si la eliminación fue correcta, 1 si el registro no existía.
	 */
	@Override
	public int deleteById(Long id) {
		Optional<Historial> encontrado = historialRepository.findById(id);
		if (!encontrado.isPresent())
			return 1;
		historialRepository.deleteById(id);
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.DELETE, Servicio.HISTORIAL);
    	auditoriaService.create(auditoria);
		return 0;
	}

	/**
	 * Modifica los datos descriptivos de una búsqueda guardada en el historial.
	 * * @param id      Identificador único del historial a cambiar.
	 * @param newData Valores actualizados provistos en un contenedor DTO.
	 * @return Código de respuesta: 0 si la operación fue exitosa, 1 si no existía el registro.
	 */
	@Override
	public int updateById(Long id, HistorialDTO newData) {
		Optional<Historial> encontrado = historialRepository.findById(id);
		if (!encontrado.isPresent())
			return 1;
		Historial entity = encontrado.get();
		entity.setBusqueda(newData.getBusqueda());
		entity.setTipoBusqueda(newData.getTipoBusqueda());
		historialRepository.save(entity);
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.UPDATE, Servicio.HISTORIAL);
    	auditoriaService.create(auditoria);
		return 0;
	}

	/**
	 * Retorna la cantidad agregada de registros de consultas salvados en la plataforma.
	 * * @return Total numérico en formato {@code long}.
	 */
	@Override
	public long count() {
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.HISTORIAL);
    	auditoriaService.create(auditoria);
		return historialRepository.count();
	}

	/**
	 * Valida de forma rápida la existencia de un registro por su identificador primario.
	 * * @param id Identificador único de consulta.
	 * @return true si el registro existe, false de lo contrario.
	 */
	@Override
	public boolean exist(Long id) {
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.HISTORIAL);
    	auditoriaService.create(auditoria);
		return historialRepository.existsById(id);
	}
	
	public List<HistorialDTO> findByIdUsuario(long idUsuario) {
		List<Historial> entityList = historialRepository.findByIdUsuario(idUsuario);
		List<HistorialDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			HistorialDTO dto = modelMapper.map(entity, HistorialDTO.class);
			dtoList.add(dto);
		});
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.HISTORIAL);
    	auditoriaService.create(auditoria);
		return dtoList;
	}

}
