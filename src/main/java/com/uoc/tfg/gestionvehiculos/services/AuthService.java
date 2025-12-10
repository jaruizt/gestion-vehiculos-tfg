package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.dtos.auth.LoginRequest;
import com.uoc.tfg.gestionvehiculos.dtos.auth.LoginResponse;
import com.uoc.tfg.gestionvehiculos.dtos.auth.RegistroRequest;
import com.uoc.tfg.gestionvehiculos.entities.Usuario;
import com.uoc.tfg.gestionvehiculos.enums.Rol;
import com.uoc.tfg.gestionvehiculos.repositories.UsuarioRepository;
import com.uoc.tfg.gestionvehiculos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Autentica un usuario y retorna un token JWT
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            usuario.registrarAcceso();
            usuarioRepository.save(usuario);

            String token = jwtUtil.generateToken(userDetails);

            return LoginResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .username(usuario.getUsername())
                    .email(usuario.getEmail())
                    .rol(usuario.getRol().name())
                    .nombreCompleto(usuario.getNombreCompleto())
                    .build();

        } catch (BadCredentialsException e) {
            // Registrar intento fallido
            usuarioRepository.findByUsername(request.getUsername())
                    .ifPresent(usuario -> {
                        usuario.registrarIntentoFallido();
                        usuarioRepository.save(usuario);
                    });

            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    /**
     * Registra un nuevo usuario
     */
    @Transactional
    public Usuario registrar(RegistroRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setApellidos(request.getApellidos());
        nuevoUsuario.setTelefono(request.getTelefono());
        nuevoUsuario.setRol(Rol.USUARIO); // Por defecto rol USUARIO
        nuevoUsuario.setFechaCambioPassword(LocalDateTime.now());

        return usuarioRepository.save(nuevoUsuario);
    }
}