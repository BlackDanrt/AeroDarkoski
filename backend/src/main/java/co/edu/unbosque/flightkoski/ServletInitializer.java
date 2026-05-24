package co.edu.unbosque.flightkoski;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Configuracion para el despliegue en servidores.
 * Permite que la aplicacion funcione dentro de un contenedor de servlets.
 * @author Santiago Ortiz
 * @version 1.0
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Configura la aplicacion especificando la clase principal.
	 * @param application Constructor de la aplicacion.
	 * @return La aplicacion configurada con la fuente principal.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FlightkoskiApplication.class);
	}

}
