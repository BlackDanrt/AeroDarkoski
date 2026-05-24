package co.edu.unbosque.flightkoski.configuration;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import co.edu.unbosque.flightkoski.entity.Usuario;
import co.edu.unbosque.flightkoski.repository.UsuarioRepository;
import co.edu.unbosque.flightkoski.util.AESUtil;
import co.edu.unbosque.flightkoski.util.enums.Rol;

/**
 * Clase de configuración para cargar datos iniciales en la base de datos. Crea usuarios
 * predeterminados (administrador y usuario normal) al iniciar la aplicación si estos no existen
 * previamente.
 * @author Juan Martinez
 * @version 1.0
 */
@Configuration
public class LoadDatabase {
  /** Logger para registrar mensajes durante la carga de datos. */
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  /** Contraseña del usuario administrador, inyectada desde las propiedades de configuración. */
  @Value("${contrasenia.admin}")
  private String contraseniaAdmin;

  /** Correo del usuario administrador, inyectado desde las propiedades de configuración. */
  @Value("${correo.admin}")
  private String correoAdmin;
  
  /** Nombre del usuario administrador, inyectado desde las propiedades de configuración. */
  @Value("${usuario.admin}")
  private String usuarioAdmin;
  

  /** Nombre del usuario normal, inyectado desde las propiedades de configuración. */
  @Value("${usuario.user}")
  private String usuarioUser;

  /** Correo del usuario normal, inyectado desde las propiedades de configuración. */
  @Value("${correo.user}")
  private String correoUser;
  
  /** Contraseña del usuario normal, inyectada desde las propiedades de configuración. */
  @Value("${contrasenia.user}")
  private String contraseniaUser;
  
  /** Iv inyectado desde el archivo de propiedades del sistema usado para encriptación y desencriptación de datos*/
  @Value("${iv.secreta}")
  private String iv;

  /** Llave inyectada desde el archivo de propiedades del sistema usada para encriptación y desencriptación de datos*/
  @Value("${key.secreta}")
  private String key;
 
  /**
   * Inicializa la base de datos con usuarios predeterminados. Crea un usuario administrador y un
   * usuario normal si no existen.
   *
   * @param userRepo Repositorio de usuarios para acceder a la base de datos
   * @param passwordEncoder Codificador de contraseñas para encriptar las contraseñas de los
   *     usuarios
   * @return Un CommandLineRunner que se ejecuta al iniciar la aplicación
   */
  @Bean
  CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {

    return args -> {
      Optional<Usuario> found = usuarioRepository.findByNombreUsuario("admin");
      
      if (found.isPresent()) {
        log.info("El administrador ya existe, omitiendo la creación del administrador...");
      } else {
        Usuario adminUser = new Usuario(usuarioAdmin, AESUtil.encrypt(key, iv, correoAdmin), passwordEncoder.encode(contraseniaAdmin), Rol.ADMINISTRADOR);
        usuarioRepository.save(adminUser);
        log.info("Precargando usuario administrador");
      }
      
      Optional<Usuario> found2 = usuarioRepository.findByNombreUsuario("normaluser");
      if (found2.isPresent()) {
        log.info("El usuario normal ya existe, omitiendo la creación del usuario normal...");
      } else {
        Usuario normalUser = new Usuario(usuarioUser, AESUtil.encrypt(key, iv, correoUser),  passwordEncoder.encode(contraseniaUser), Rol.USUARIO);
        usuarioRepository.save(normalUser);
        log.info("Precargando usuario normal");
      }
    };
  }
}
