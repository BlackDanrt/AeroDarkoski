package co.edu.unbosque.flightkoski;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal que inicia la aplicacion Spring Boot.
 * Configura los componentes base y arranca el servidor.
 * @author Santiago Ortiz
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling
public class FlightkoskiApplication {

	/**
	   * Método principal que inicia la aplicación Spring Boot.
	   *
	   * @param args Argumentos de línea de comandos pasados a la aplicación
	   */
	public static void main(String[] args) {
		SpringApplication.run(FlightkoskiApplication.class, args);
	}
	
	/**
	   * Crea y configura un bean ModelMapper para el mapeo de objetos. Este bean se utiliza en toda la
	   * aplicación para mapear entre DTOs y objetos de entidad.
	   *
	   * @return Instancia configurada de ModelMapper
	   */
	  @Bean
	  public ModelMapper getModelMapper() {
	    return new ModelMapper();
	  }

}
