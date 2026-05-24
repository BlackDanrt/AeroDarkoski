package co.edu.unbosque.flightkoski.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import com.google.gson.Gson;

import co.edu.unbosque.flightkoski.dto.AvionDTO;
import co.edu.unbosque.flightkoski.dto.ClimaDTO;

/**
 * Componente de infraestructura de bajo nivel encargado de estructurar y disparar peticiones síncronas HTTP
 * hacia APIs externas de aviación comercial y climatología mundial.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Service
public class ExternalHTPPRequestHandler {

	/** Credencial tokenizada inyectada del archivo de propiedades para el acceso a Aviation Edge. */
	@Value("${api.key}")
	private String apiKeyAvion;

	/** Listado estático estructurado con códigos IATA detallados de Sudamérica para consultar los aviones */
	private static final List<String> AEROPUERTOS_SA = List.of("BOG", "MDE", "CLO", "CTG", "BAQ", "ADZ", "GRU", "GIG",
			"BSB", "SSA", "FOR", "REC", "EZE", "AEP", "COR", "MDZ", "SCL", "PMC", "IQQ", "LIM", "CUZ", "AQP", "UIO",
			"GYE", "CCS", "MAR", "VVI", "LPB", "MVD", "ASU", "GEO", "PBM");

	/** Listado estático estructurado con códigos ICAO detallados de Sudamérica para consultas del METAR. */
	private static final List<String> AEROPUERTOS_SA_ICAO = List.of("SKBO", "SKRG", "SKCL", "SKCG", "SKBQ", "SKSP",
			"SBGR", "SBGL", "SBBR", "SBSA", "SBFZ", "SBRF", "SAEZ", "SABE", "SACO", "SAME", "SCEL", "SCTE", "SCFA",
			"SPJC", "SPZO", "SPQU", "SEQM", "SEGU", "SVMI", "SVMC", "SLVR", "SLLP", "SUMU", "SGAS", "SYCJ", "SMJP");

	/**
	 * Constructor por defecto del servicio.
	 */
	public ExternalHTPPRequestHandler() {
		// TODO Auto-generated constructor stub
	}

	/** Cliente HTTP estático e inmutable configurado para trabajar con el protocolo HTTP/2 y tiempos de espera de conexión. */
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	/**
	 * Ejecuta peticiones GET al API externo de vuelos y procesa el cuerpo de la respuesta en formato JSON 
	 * para mapearlo a colecciones del entorno Java.
	 * * @return Lista de objetos de tipo {@link AvionDTO} recolectados del API.
	 */
	public List<AvionDTO> getAviones() {

		List<AvionDTO> listaAviones = new ArrayList<>();

		for (String aeropuerto : AEROPUERTOS_SA) {
			HttpRequest solicitud = HttpRequest.newBuilder().GET()
					.uri(URI.create("https://aviation-edge.com/v2/public/flights?key="+apiKeyAvion+"&arrIata="+aeropuerto))
					.setHeader("User-Agent", "Java 11 HttpClient Bot").build();

			try {
				HttpResponse<String> respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());
				
				if (respuesta.statusCode() != 200 || 
				        respuesta.body() == null || 
				        respuesta.body().isBlank() ||
				        !respuesta.body().trim().startsWith("[")) { 
				        System.out.println("Sin datos para -> " + aeropuerto);
				        Thread.sleep(250);
				        continue;
				    }
				
				Gson gson = new Gson();
				AvionDTO[] resultado = gson.fromJson(respuesta.body(), AvionDTO[].class);

				if (resultado != null && resultado.length > 0) {
					listaAviones.addAll(List.of(resultado));
				}
				Thread.sleep(250);

				System.out.println("Status code -> " + respuesta.statusCode());
				System.out.println("aeropuerto llamado -> " + aeropuerto);

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		return listaAviones;
	}

	/**
	 * Ejecuta peticiones HTTP orientadas a la obtención de tramas meteorológicas METAR serializadas
	 * desde los servidores oficiales del Aviation Weather de EE. UU.
	 * * @return Lista con los objetos estructurados de tipo {@link ClimaDTO}.
	 */
	public List<ClimaDTO> getClima() {
		List<ClimaDTO> listaClima = new ArrayList<>();

		for (String aeropuerto : AEROPUERTOS_SA_ICAO) {
			HttpRequest solicitud = HttpRequest.newBuilder().GET()
					.uri(URI.create("https://aviationweather.gov/api/data/metar?ids=" + aeropuerto + "&format=json"))
					.setHeader("User-Agent", "Java 11 HttpClient Bot").build();

			try {
				HttpResponse<String> respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());

				System.out.println("Status code -> " + respuesta.statusCode());
				System.out.println("aeropuerto llamado -> " + aeropuerto);
				
				if (respuesta.statusCode() != 200 ||
					    respuesta.body() == null ||
					    respuesta.body().isBlank()) {

					    System.out.println(
					        "Sin datos para -> " +
					        aeropuerto
					    );

					    continue;
					}
				
				Gson gson = new Gson();
				ClimaDTO[] resultado = gson.fromJson(respuesta.body(), ClimaDTO[].class);

				if (resultado != null && resultado.length > 0) {
					listaClima.add(resultado[0]);
				}
				Thread.sleep(250);


			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		return listaClima;
	}
}
