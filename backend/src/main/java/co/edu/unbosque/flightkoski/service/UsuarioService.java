package co.edu.unbosque.flightkoski.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.UsuarioDTO;
import co.edu.unbosque.flightkoski.entity.Usuario;
import co.edu.unbosque.flightkoski.repository.UsuarioRepository;
import co.edu.unbosque.flightkoski.util.AESUtil;
import co.edu.unbosque.flightkoski.util.enums.Rol;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

/**
 * Servicio CRUD encargado de gestionar la lógica de negocio
 * y persistencia para el control de cuentas de usuario en el sistema.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Service
public class UsuarioService implements CRUDOperation<UsuarioDTO> {
	
	/** Repositorio de acceso a datos para las operaciones CRUD sobre la entidad Usuario. */
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/** Componente encargado del mapeo relacional entre estructuras de datos DTO y Entidades. */
	@Autowired
	private ModelMapper modelMapper;
	
	/** Codificador para encriptar contraseñas de usuarios. */
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	/** Servicio de envio de correos */
	@Autowired
	private CorreoService correoService;
	
	@Autowired
	private AuditoriaService auditoriaService;
	
	/** Iv inyectado desde el archivo de propiedades del sistema usado para encriptación y desencriptación de datos*/
	@Value("${iv.secreta}")
	private String iv;

	/** Llave inyectada desde el archivo de propiedades del sistema usada para encriptación y desencriptación de datos*/
	@Value("${key.secreta}")
	private String key;
	    
	/**
	 * Constructor por defecto del servicio.
	 */
	public UsuarioService() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Registra un nuevo usuario aplicando validaciones complejas de duplicación, 
	 * estructura alfanumérica en el alias y aplicando hashing sobre su clave secreta.
	 * * @param data Objeto con las credenciales del nuevo usuario a crear.
	 * @return Código de respuesta: 
	 * <ul>
	 * <li>0: Creado con éxito.</li>
	 * <li>1: Error, el nombre de usuario ya está registrado en el sistema.</li>
	 * <li>2: Error, el apodo o nombre contiene caracteres no permitidos (debe ser alfanumérico estricto).</li>
	 * </ul>
	 */
	@Override
	public int create(UsuarioDTO data) {
		if (existsByNombreUsuario(data.getNombreUsuario())) return 1;
	    if (!data.getNombreUsuario().matches("^[a-zA-Z0-9]+$")) return 2;

	    Usuario entity = modelMapper.map(data, Usuario.class);
	    entity.setContrasenia(passwordEncoder.encode(data.getContrasenia()));
	    entity.setAccountNonExpired(true);
	    entity.setAccountNonLocked(true);
	    entity.setCredentialsNonExpired(true);
	    entity.setEnabled(true);
	    entity.setRol(Rol.USUARIO);
	    entity.setCorreo(AESUtil.encrypt(key, iv, data.getCorreo()));
	    if (data.getRol() != null) entity.setRol(data.getRol());
	    correoService.enviarCorreoRegistro(data.getCorreo(), data.getNombreUsuario());
	    usuarioRepository.save(entity);
	    AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.CREATE, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
	    return 0;
	}

	/**
	 * Recupera todos los usuarios del sistema mapeados a DTOs para la gestión administrativa.
	 * * @return Lista completa de objetos de tipo {@link UsuarioDTO}.
	 */
	@Override
	public List<UsuarioDTO> getAll() {
		List<Usuario> entityList = usuarioRepository.findAll();
		List<UsuarioDTO> dtoList = new ArrayList<>();	
		entityList.forEach(
			        (entity) -> {
			         UsuarioDTO dto = new UsuarioDTO();
			         dto.setId(entity.getId());
			         dto.setNombreUsuario(entity.getNombreUsuario());
			         dto.setCorreo(AESUtil.decrypt(key, iv, entity.getCorreo()));
			         dto.setContrasenia(null);
			         dto.setRol(entity.getRol());
			         dtoList.add(dto);
			        });
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
		return dtoList;
	}

	/**
	 * Remueve la cuenta de un usuario de la persistencia a través de su ID único.
	 * * @param id Identificador numérico único del usuario.
	 * @return Código de respuesta: 0 si la eliminación fue correcta, 1 si el usuario no existía.
	 */
	@Override
	public int deleteById(Long id) {
		if (!usuarioRepository.existsById(id)) return 1;
		usuarioRepository.deleteById(id);
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.DELETE, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
		return 0;
	}

	/**
	 * Actualiza los datos de un usuario existente, garantizando el cumplimiento de unicidad en 
	 * el nombre de usuario modificado, validaciones alfanuméricas y re-encriptación de contraseña si aplica.
	 * * @param id      Identificador único del usuario a modificar.
	 * @param newData Nuevos valores provistos dentro de un contenedor DTO.
	 * @return Código de respuesta:
	 * <ul>
	 * <li>0: Actualización completada de forma correcta.</li>
	 * <li>1: El usuario a modificar no existe en el sistema.</li>
	 * <li>2: El nuevo nombre de usuario ya está tomado por otra cuenta activa.</li>
	 * <li>3: El nombre de usuario provisto infringe el formato alfanumérico estricto.</li>
	 * </ul>
	 */
	@Override
	public int updateById(Long id, UsuarioDTO newData) {
		Optional<Usuario> encontrado = usuarioRepository.findById(id);
		Optional<Usuario> encontradoNuevo = usuarioRepository.findByNombreUsuario(newData.getNombreUsuario());
		
		if (!encontrado.isPresent()) return 1; 
	    if (encontradoNuevo.isPresent() && (encontradoNuevo.get().getId() != encontrado.get().getId())) return 2;
	    if (!newData.getNombreUsuario().matches("^[a-zA-Z0-9]+$")) return 3;
	    Usuario temp = encontrado.get();
	    temp.setNombreUsuario(newData.getNombreUsuario());
	    if(!(newData.getContrasenia() == null)) {
	    	temp.setContrasenia(passwordEncoder.encode(newData.getContrasenia()));
	    }
	    if (newData.getRol() != null) {
	        temp.setRol(newData.getRol());
	    }
	    usuarioRepository.save(temp);
	    AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.UPDATE, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
	    return 0; 
	}

	/**
	 * Retorna la cantidad agregada de usuarios creados en la plataforma.
	 * * @return Total numérico en formato {@code long}.
	 */
	@Override
	public long count() {
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
		return usuarioRepository.count();
	}

	/**
	 * Valida de forma rápida la existencia de un perfil de usuario por su identificador primario.
	 * * @param id Identificador único de consulta.
	 * @return true si la cuenta existe, false de lo contrario.
	 */
	@Override
	public boolean exist(Long id) {
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
		return usuarioRepository.existsById(id);
	}
	
	/**
	 * Verifica si un nombre de usuario o alias ya se encuentra ocupado en los almacenes del sistema.
	 * * @param nombreUsuario Cadena de caracteres del alias a auditar.
	 * @return true si el nombre ya está registrado, false en caso de estar libre.
	 */
	public boolean existsByNombreUsuario(String nombreUsuario) {
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
		return usuarioRepository.existsByNombreUsuario(nombreUsuario);
	}

	/**
	 * Obtiene el perfil estructurado de un usuario por su ID de persistencia.
	 * * @param id Identificador numérico a buscar.
	 * @return Objeto {@link UsuarioDTO} si se encuentra, o {@code null} en caso contrario.
	 */
	public UsuarioDTO findById(long id) {
		Optional<Usuario> encontrado = usuarioRepository.findById(id);
		if(!encontrado.isPresent()) return null;
		UsuarioDTO dto = new UsuarioDTO();
        dto.setId(encontrado.get().getId());
        dto.setNombreUsuario(encontrado.get().getNombreUsuario());
        dto.setCorreo(AESUtil.decrypt(key, iv, encontrado.get().getCorreo()));
        dto.setContrasenia(null);
        dto.setRol(encontrado.get().getRol());
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.READ, Servicio.USUARIO);
    	auditoriaService.create(auditoria);
		return dto;
	}
}
