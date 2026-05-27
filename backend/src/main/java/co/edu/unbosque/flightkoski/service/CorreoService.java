package co.edu.unbosque.flightkoski.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio encargado de la composición y envío automatizado de notificaciones por correo electrónico,
 * soportando plantillas en formato HTML estructurado.
 * @author Santiago Ortiz
 * @version 1.0
 */
@Service
public class CorreoService {

	/** Proveedor e interfaz de Spring encargada del envío de correos electrónicos. */
	@Autowired
	private JavaMailSender mailSender;
	
	/** Servicio transversal para el registro y rastreo de auditorías de mensajería saliente. */
	@Autowired
	private AuditoriaService auditoriaService;
	
	/** Logger para registrar mensajes durante el envio de correos. */
	  private static final Logger log = LoggerFactory.getLogger(CorreoService.class);
	  
	/**
	 * Constructor por defecto del servicio.
	 */
	public CorreoService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Envía un correo electrónico estructurado en HTML dando la bienvenida
	 * a un usuario recién registrado en la plataforma.
	 * * @param destinatario  Dirección de correo electrónico a la cual se enviará el mensaje.
	 * @param nombreUsuario Alias o nombre de la cuenta registrada para personalización.
	 * @return Código de respuesta: 0 si el envío fue exitoso, -1 si ocurrió una anomalía durante el proceso.
	 */
	public int enviarCorreoRegistro(String destinatario, String nombreUsuario) {
		AuditoriaDTO auditoria = auditoriaService.preAccion(TipoAccion.SEND, Servicio.CORREO);
    	auditoriaService.create(auditoria);
	    try {
	        MimeMessage mensaje = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

	        helper.setFrom("aerodarkoski@gmail.com");
	        helper.setTo(destinatario);
	        helper.setSubject("¡Registro exitoso! - AeroDarkoski");

	        String html = """
	            <html>
	            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
	                <div style="max-width: 600px; margin: auto; background: white; border-radius: 8px; padding: 30px;">
	                    <h2 style="color: #2c3e50;">AeroDarkoski ✈</h2>
	                    <hr style="border: 1px solid #eee;">
	                    <p style="color: #555; font-size: 15px;">¡Bienvenido, <strong>%s</strong>!</p>
	                    <p style="color: #555; font-size: 15px;">Tu registro ha sido exitoso. Ya puedes iniciar sesión y comenzar a explorar nuestro tracker.</p>
	                    <div style="text-align: center; margin: 30px 0;">
	                        <span style="background-color: #2c3e50; color: #fff; font-size: 20px;
	                            font-weight: bold; padding: 15px 30px;
	                            border-radius: 8px;">
	                            ¡Registro exitoso!
	                        </span>
	                    </div>
	                    <hr style="border: 1px solid #eee;">
	                    <small style="color: #aaa;">Este es un correo automático, no responder.</small>
	                </div>
	            </body>
	            </html>
	            """.formatted(nombreUsuario);

	        helper.setText(html, true);
	        mailSender.send(mensaje);
	        log.info("Correo de registro enviado a: " + destinatario);
	        return 0;

	    } catch (Exception e) {
	        log.warn("Error al enviar correo: " + e.getMessage());
	        return -1;
	    }
	}
}
