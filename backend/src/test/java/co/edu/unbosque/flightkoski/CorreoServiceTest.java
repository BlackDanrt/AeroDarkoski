package co.edu.unbosque.flightkoski;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.service.AuditoriaService;
import co.edu.unbosque.flightkoski.service.CorreoService;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class CorreoServiceTest {

    @Mock private JavaMailSender mailSender;
    @Mock private AuditoriaService auditoriaService;

    @InjectMocks
    private CorreoService correoService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        when(auditoriaService.preAccion(TipoAccion.SEND, Servicio.CORREO))
                .thenReturn(new AuditoriaDTO());
    }

    @Test
    void enviarCorreoRegistroEnvioExitosoDebeRetornarCero() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        int resultado = correoService.enviarCorreoRegistro("usuario@test.com", "Santiago");

        assertEquals(0, resultado);
        verify(mailSender, times(1)).send(mimeMessage);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void enviarCorreoRegistroErrorAlEnviarDebeRetornarMenosUno() {
     
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Error de conexión SMTP"));

        int resultado = correoService.enviarCorreoRegistro("usuario@test.com", "Santiago");

        assertEquals(-1, resultado);
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void enviarCorreoRegistroDebeRegistrarAuditoriaAntesDeSendmail() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        correoService.enviarCorreoRegistro("usuario@test.com", "Santiago");

        verify(auditoriaService, times(1)).preAccion(TipoAccion.SEND, Servicio.CORREO);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void enviarCorreoRegistroConNombreDistintoDebeEnviarSinErrores() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        int resultado = correoService.enviarCorreoRegistro("otro@mail.com", "Juan");

        assertEquals(0, resultado);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void enviarCorreoRegistroAuditoriaRegistradaAunConFalloDeEnvio() {

        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Fallo SMTP"));

        correoService.enviarCorreoRegistro("fail@test.com", "ErrorUser");

        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }
}