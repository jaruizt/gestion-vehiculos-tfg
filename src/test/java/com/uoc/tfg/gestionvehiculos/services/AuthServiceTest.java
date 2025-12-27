package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.dtos.auth.LoginRequest;
import com.uoc.tfg.gestionvehiculos.dtos.auth.LoginResponse;
import com.uoc.tfg.gestionvehiculos.dtos.auth.RegistroRequest;
import com.uoc.tfg.gestionvehiculos.entities.Usuario;
import com.uoc.tfg.gestionvehiculos.enums.Rol;
import com.uoc.tfg.gestionvehiculos.repositories.UsuarioRepository;
import com.uoc.tfg.gestionvehiculos.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private LoginRequest loginRequest;
    private RegistroRequest registroRequest;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("$2a$10$encodedPassword");
        usuario.setEmail("test@example.com");
        usuario.setNombre("Test");
        usuario.setApellidos("User");
        usuario.setTelefono("666123456");
        usuario.setRol(Rol.USUARIO);
        usuario.setActivo(true);
        usuario.setFechaCambioPassword(LocalDateTime.now());

        // LoginRequest
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // RegistroRequest
        registroRequest = new RegistroRequest();
        registroRequest.setUsername("newuser");
        registroRequest.setPassword("password123");
        registroRequest.setEmail("newuser@example.com");
        registroRequest.setNombre("New");
        registroRequest.setApellidos("User");
        registroRequest.setTelefono("666999888");

        // UserDetails
        userDetails = User.builder()
                .username("testuser")
                .password("$2a$10$encodedPassword")
                .authorities(new ArrayList<>())
                .build();
    }

    @Test
    void login_ConCredencialesValidas_DeberiaRetornarToken() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mock.jwt.token");

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("USUARIO", response.getRol());
        assertEquals("Test User", response.getNombreCompleto());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(usuarioRepository, times(1)).findByUsername("testuser");
        verify(usuarioRepository, times(1)).save(usuario);
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }

    @Test
    void login_ConCredencialesInvalidas_DeberiaLanzarExcepcion() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, times(1)).findByUsername("testuser");
        verify(usuarioRepository, times(1)).save(usuario); // Registra intento fallido
    }

    @Test
    void login_UsuarioNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(usuarioRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void registrar_ConDatosValidos_DeberiaCrearUsuario() {
        // Arrange
        when(usuarioRepository.existsByUsername("newuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });

        // Act
        Usuario resultado = authService.registrar(registroRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("newuser", resultado.getUsername());
        assertEquals("$2a$10$encodedPassword", resultado.getPassword());
        assertEquals("newuser@example.com", resultado.getEmail());
        assertEquals("New", resultado.getNombre());
        assertEquals("User", resultado.getApellidos());
        assertEquals(Rol.USUARIO, resultado.getRol());

        verify(usuarioRepository, times(1)).existsByUsername("newuser");
        verify(usuarioRepository, times(1)).existsByEmail("newuser@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrar_UsernameYaExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsByUsername("newuser")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.registrar(registroRequest);
        });

        assertEquals("El username ya está en uso", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByUsername("newuser");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrar_EmailYaExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsByUsername("newuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("newuser@example.com")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.registrar(registroRequest);
        });

        assertEquals("El email ya está en uso", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByUsername("newuser");
        verify(usuarioRepository, times(1)).existsByEmail("newuser@example.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrar_DeberiaAsignarRolUsuarioPorDefecto() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = authService.registrar(registroRequest);

        // Assert
        assertEquals(Rol.USUARIO, resultado.getRol());
        assertNotNull(resultado.getFechaCambioPassword());
    }

    @Test
    void registrar_DeberiaEncriptarPassword() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = authService.registrar(registroRequest);

        // Assert
        assertEquals("$2a$10$hashedPassword", resultado.getPassword());
        assertNotEquals("password123", resultado.getPassword());
        verify(passwordEncoder, times(1)).encode("password123");
    }
}