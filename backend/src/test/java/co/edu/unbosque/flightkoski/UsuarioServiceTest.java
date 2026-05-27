package co.edu.unbosque.flightkoski;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.unbosque.flightkoski.dto.AuditoriaDTO;
import co.edu.unbosque.flightkoski.dto.UsuarioDTO;
import co.edu.unbosque.flightkoski.entity.Usuario;
import co.edu.unbosque.flightkoski.repository.UsuarioRepository;
import co.edu.unbosque.flightkoski.service.AuditoriaService;
import co.edu.unbosque.flightkoski.service.CorreoService;
import co.edu.unbosque.flightkoski.service.UsuarioService;
import co.edu.unbosque.flightkoski.util.AESUtil;
import co.edu.unbosque.flightkoski.util.enums.Rol;
import co.edu.unbosque.flightkoski.util.enums.Servicio;
import co.edu.unbosque.flightkoski.util.enums.TipoAccion;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioServiceTest {

    private static final String TEST_KEY = "1234567890123456";
    private static final String TEST_IV  = "1234567890123456";

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private CorreoService correoService;
    @Mock private AuditoriaService auditoriaService;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(usuarioService, "iv",  TEST_IV);
        ReflectionTestUtils.setField(usuarioService, "key", TEST_KEY);

        lenient().when(auditoriaService.preAccion(any(TipoAccion.class), eq(Servicio.USUARIO)))
                 .thenReturn(new AuditoriaDTO());
    }

    @Test
    void createRegistroExitosoDebeRetornarCero() {
        UsuarioDTO dto = buildDTO("Santiago", "claveSegurA*", "santiago@test.com", Rol.USUARIO);
        Usuario entity = new Usuario();

        when(usuarioRepository.existsByNombreUsuario("Santiago")).thenReturn(false);
        when(modelMapper.map(dto, Usuario.class)).thenReturn(entity);
        when(passwordEncoder.encode("claveSegurA*")).thenReturn("hash123");

        int resultado = usuarioService.create(dto);

        assertEquals(0, resultado);
        verify(usuarioRepository, times(1)).save(entity);

        verify(auditoriaService, times(2)).create(any(AuditoriaDTO.class));
    }

    @Test
    void creaUsuarioYaExisteDebeRetornarUno() {
        UsuarioDTO dto = buildDTO("Juan", null, null, null);

        when(usuarioRepository.existsByNombreUsuario("Juan")).thenReturn(true);

        int resultado = usuarioService.create(dto);

        assertEquals(1, resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void createNombreNoAlfanumericoDebeRetornarDos() {
        UsuarioDTO dto = buildDTO("Jesus_123!", null, null, null);

        when(usuarioRepository.existsByNombreUsuario("Jesus_123!")).thenReturn(false);

        int resultado = usuarioService.create(dto);

        assertEquals(2, resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void createRolNullEnDTODebeAsignarRolUsuarioPorDefecto() {
        UsuarioDTO dto = buildDTO("Santiago", "clavE*", "s@test.com", null); 
        Usuario entity = new Usuario();

        when(usuarioRepository.existsByNombreUsuario("Santiago")).thenReturn(false);
        when(modelMapper.map(dto, Usuario.class)).thenReturn(entity);
        when(passwordEncoder.encode("clavE*")).thenReturn("hash");

        usuarioService.create(dto);

  
        assertEquals(Rol.USUARIO, entity.getRol());
    }

    @Test
    void createRolNoNullEnDTODebeRespestarElRolRecibido() {
        UsuarioDTO dto = buildDTO("AdminUser", "clavE*", "admin@test.com", Rol.ADMINISTRADOR);
        Usuario entity = new Usuario();

        when(usuarioRepository.existsByNombreUsuario("AdminUser")).thenReturn(false);
        when(modelMapper.map(dto, Usuario.class)).thenReturn(entity);
        when(passwordEncoder.encode("clavE*")).thenReturn("hash");

        usuarioService.create(dto);

        assertEquals(Rol.ADMINISTRADOR, entity.getRol());
    }

    @Test
    void createDebeEnviarCorreoConEmailOriginalSinEncriptar() {
        String correoOriginal = "santiago@test.com";
        UsuarioDTO dto = buildDTO("Santiago", "clavE*", correoOriginal, Rol.USUARIO);
        Usuario entity = new Usuario();

        when(usuarioRepository.existsByNombreUsuario("Santiago")).thenReturn(false);
        when(modelMapper.map(dto, Usuario.class)).thenReturn(entity);
        when(passwordEncoder.encode("clavE*")).thenReturn("hash");

        usuarioService.create(dto);

        verify(correoService, times(1)).enviarCorreoRegistro(eq(correoOriginal), eq("Santiago"));
    }

    @Test
    void createDebeGuardarCorreoEncriptadoEnLaEntidad() {
        String correoOriginal = "santiago@test.com";
        UsuarioDTO dto = buildDTO("Santiago", "clavE*", correoOriginal, Rol.USUARIO);
        Usuario entity = new Usuario();

        when(usuarioRepository.existsByNombreUsuario("Santiago")).thenReturn(false);
        when(modelMapper.map(dto, Usuario.class)).thenReturn(entity);
        when(passwordEncoder.encode("clavE*")).thenReturn("hash");

        usuarioService.create(dto);

        String correoEsperado = AESUtil.encrypt(TEST_KEY, TEST_IV, correoOriginal);
        assertEquals(correoEsperado, entity.getCorreo());
    }

    @Test
    void getAllListaVaciaDebeRetornarListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

        List<UsuarioDTO> resultado = usuarioService.getAll();

        assertTrue(resultado.isEmpty());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void getAll_ConUsuariosDebeRetornarListaMapeada() {
        Usuario user = buildEntity(1L, "santiago", "santiago@test.com", Rol.USUARIO);
        when(usuarioRepository.findAll()).thenReturn(List.of(user));

        List<UsuarioDTO> resultado = usuarioService.getAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("santiago", resultado.get(0).getNombreUsuario());
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void getAllContraseniaDebeSerNullEnElDTO() {
        Usuario user = buildEntity(1L, "santiago", "santiago@test.com", Rol.USUARIO);
        when(usuarioRepository.findAll()).thenReturn(List.of(user));

        List<UsuarioDTO> resultado = usuarioService.getAll();

        assertNull(resultado.get(0).getContrasenia(),
                "La contraseña nunca debe exponerse en el DTO de salida");
    }

    @Test
    void getAllCorreoDebeEstarDesencriptadoEnElDTO() {
        String correoOriginal = "santiago@test.com";
        Usuario user = buildEntity(1L, "santiago", correoOriginal, Rol.USUARIO);
        when(usuarioRepository.findAll()).thenReturn(List.of(user));

        List<UsuarioDTO> resultado = usuarioService.getAll();

        assertEquals(correoOriginal, resultado.get(0).getCorreo());
    }

    @Test
    void getAllDebeMapearIdYRolCorrectamente() {
        Usuario user = buildEntity(5L, "juan", "juan@test.com", Rol.ADMINISTRADOR);
        when(usuarioRepository.findAll()).thenReturn(List.of(user));

        List<UsuarioDTO> resultado = usuarioService.getAll();

        assertEquals(5L, resultado.get(0).getId());
        assertEquals(Rol.ADMINISTRADOR, resultado.get(0).getRol());
    }

    @Test
    void deleteByIdUsuarioExisteDebeEliminarYRetornarCero() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        int resultado = usuarioService.deleteById(1L);

        assertEquals(0, resultado);
        verify(usuarioRepository, times(1)).deleteById(1L);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void deleteByIdUsuarioNoExisteDebeRetornarUnoSinEliminar() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        int resultado = usuarioService.deleteById(99L);

        assertEquals(1, resultado);
        verify(usuarioRepository, never()).deleteById(anyLong());
        verify(auditoriaService, never()).create(any(AuditoriaDTO.class));
    }

    @Test
    void updateByIdActualizacionExitosaDebeRetornarCero() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("santiagoViejo");

        UsuarioDTO newData = buildDTO("santiagoNuevo", "nuevaClave*", null, Rol.ADMINISTRADOR);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByNombreUsuario("santiagoNuevo")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("nuevaClave*")).thenReturn("hashNuevo");

        int resultado = usuarioService.updateById(1L, newData);

        assertEquals(0, resultado);
        assertEquals("santiagoNuevo", existente.getNombreUsuario());
        assertEquals("hashNuevo", existente.getContrasenia());
        assertEquals(Rol.ADMINISTRADOR, existente.getRol());
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void updateByIdUsuarioNoExisteDebeRetornarUno() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        int resultado = usuarioService.updateById(99L, buildDTO("cualquiera", null, null, null));

        assertEquals(1, resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void updateByIdNombreTomadoPorOtroUsuarioDebeRetornarDos() {
        Usuario existente = new Usuario();
        existente.setId(1L);

        Usuario otro = new Usuario();
        otro.setId(2L);
        otro.setNombreUsuario("juan");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByNombreUsuario("juan")).thenReturn(Optional.of(otro));

        int resultado = usuarioService.updateById(1L, buildDTO("juan", null, null, null));

        assertEquals(2, resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void updateByIdNombreMismoUsuarioDebePermitirActualizacion() {
    	
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("santiago");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));

        when(usuarioRepository.findByNombreUsuario("santiago")).thenReturn(Optional.of(existente));

        int resultado = usuarioService.updateById(1L, buildDTO("santiago", null, null, null));

        assertEquals(0, resultado);
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void updateByIdNombreNoAlfanumericoDebeRetornarTres() {
        Usuario existente = new Usuario();
        existente.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByNombreUsuario("santiago-123")).thenReturn(Optional.empty());

        int resultado = usuarioService.updateById(1L, buildDTO("santiago-123", null, null, null));

        assertEquals(3, resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void updateByIdContraseniaNullNoDebeEncriptarNiCambiarContrasenia() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("santiago");
        existente.setContrasenia("hashOriginal");

        UsuarioDTO newData = buildDTO("santiagoNuevo", null, null, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByNombreUsuario("santiagoNuevo")).thenReturn(Optional.empty());

        usuarioService.updateById(1L, newData);

        verify(passwordEncoder, never()).encode(any());
        assertEquals("hashOriginal", existente.getContrasenia(),
                "La contraseña no debe modificarse cuando el DTO trae null");
    }

    @Test
    void updateByIdRolNullEnDTONoDebeModificarRolExistente() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("santiago");
        existente.setRol(Rol.ADMINISTRADOR);

        UsuarioDTO newData = buildDTO("santiagoNuevo", null, null, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByNombreUsuario("santiagoNuevo")).thenReturn(Optional.empty());

        usuarioService.updateById(1L, newData);

        assertEquals(Rol.ADMINISTRADOR, existente.getRol(),
                "El rol no debe cambiar cuando el DTO trae null");
    }

    @Test
    void countDebeRetornarCantidadDeUsuarios() {
        when(usuarioRepository.count()).thenReturn(5L);

        long cantidad = usuarioService.count();

        assertEquals(5L, cantidad);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void existUsuarioExisteDebeRetornarTrue() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        assertTrue(usuarioService.exist(1L));
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void existUsuarioNoExisteDebeRetornarFalse() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertFalse(usuarioService.exist(99L));
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void existsByNombreUsuarioNombreExistenteDebeRetornarTrue() {
        when(usuarioRepository.existsByNombreUsuario("santiago")).thenReturn(true);

        assertTrue(usuarioService.existsByNombreUsuario("santiago"));
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void existsByNombreUsuarioNombreInexistenteDebeRetornarFalse() {
        when(usuarioRepository.existsByNombreUsuario("fantasma")).thenReturn(false);

        assertFalse(usuarioService.existsByNombreUsuario("fantasma"));
    }

    @Test
    void existsByNombreUsuarioDebeRegistrarAuditoria() {
        when(usuarioRepository.existsByNombreUsuario(anyString())).thenReturn(false);

        usuarioService.existsByNombreUsuario("cualquiera");

        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByIdUsuarioExisteDebeRetornarDTONoNulo() {
        Usuario entidad = buildEntity(1L, "santiago", "prueba@test.com", Rol.USUARIO);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(entidad));

        UsuarioDTO resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        verify(auditoriaService, times(1)).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByIdUsuarioNoExisteDebeRetornarNull() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        UsuarioDTO resultado = usuarioService.findById(99L);

        assertNull(resultado);

        verify(auditoriaService, never()).create(any(AuditoriaDTO.class));
    }

    @Test
    void findByIdDebeMapearIdYNombreCorrectamente() {
        Usuario entidad = buildEntity(7L, "juan", "juan@test.com", Rol.ADMINISTRADOR);
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(entidad));

        UsuarioDTO resultado = usuarioService.findById(7L);

        assertEquals(7L, resultado.getId());
        assertEquals("juan", resultado.getNombreUsuario());
    }

    @Test
    void findByIdContraseniaDebeSerNullEnElDTO() {
        Usuario entidad = buildEntity(1L, "santiago", "prueba@test.com", Rol.USUARIO);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(entidad));

        UsuarioDTO resultado = usuarioService.findById(1L);

        assertNull(resultado.getContrasenia(),
                "La contraseña nunca debe exponerse en el DTO de salida");
    }

    @Test
    void findByIdCorreoDebeEstarDesencriptadoEnElDTO() {
        String correoOriginal = "prueba@test.com";
        Usuario entidad = buildEntity(1L, "santiago", correoOriginal, Rol.USUARIO);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(entidad));

        UsuarioDTO resultado = usuarioService.findById(1L);

        assertEquals(correoOriginal, resultado.getCorreo());
    }


    private UsuarioDTO buildDTO(String nombreUsuario, String contrasenia, String correo, Rol rol) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombreUsuario(nombreUsuario);
        dto.setContrasenia(contrasenia);
        dto.setCorreo(correo);
        dto.setRol(rol);
        return dto;
    }


    private Usuario buildEntity(Long id, String nombreUsuario, String correoOriginal, Rol rol) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombreUsuario(nombreUsuario);
        u.setCorreo(AESUtil.encrypt(TEST_KEY, TEST_IV, correoOriginal));
        u.setRol(rol);
        return u;
    }
}