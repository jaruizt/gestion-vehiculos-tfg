package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Usuario;
import com.uoc.tfg.gestionvehiculos.enums.Rol;
import com.uoc.tfg.gestionvehiculos.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios activos
     */
    public List<Usuario> listarActivos() {
        log.debug("Listando todos los usuarios activos");
        return usuarioRepository.findByActivoTrue();
    }

    /**
     * Obtiene un usuario por ID
     */
    public Usuario obtenerPorId(Long id) {
        log.debug("Buscando usuario con id: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    /**
     * Obtiene un usuario por username
     */
    public Usuario obtenerPorUsername(String username) {
        log.debug("Buscando usuario con username: {}", username);
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }

    /**
     * Crea un nuevo usuario
     */
    @Transactional
    public Usuario crear(Usuario usuario) {
        log.info("Creando nuevo usuario: {}", usuario.getUsername());

        // Validar que el username no exista
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya existe: " + usuario.getUsername());
        }

        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está en uso: " + usuario.getEmail());
        }

        // Hashear password
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaCambioPassword(LocalDateTime.now());

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con id: {}", guardado.getId());

        return guardado;
    }

    /**
     * Actualiza un usuario existente
     */
    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        log.info("Actualizando usuario con id: {}", id);

        Usuario usuarioExistente = obtenerPorId(id);

        // Validar cambio de username
        if (!usuarioExistente.getUsername().equals(usuarioActualizado.getUsername())) {
            if (usuarioRepository.existsByUsername(usuarioActualizado.getUsername())) {
                throw new RuntimeException("El username ya existe: " + usuarioActualizado.getUsername());
            }
        }

        // Validar cambio de email
        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
                throw new RuntimeException("El email ya está en uso: " + usuarioActualizado.getEmail());
            }
        }

        // Actualizar campos
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellidos(usuarioActualizado.getApellidos());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setRol(usuarioActualizado.getRol());

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        log.info("Usuario actualizado exitosamente");

        return actualizado;
    }

    /**
     * Cambia la contraseña de un usuario
     */
    @Transactional
    public void cambiarPassword(Long id, String passwordActual, String passwordNueva) {
        log.info("Cambiando contraseña para usuario id: {}", id);

        Usuario usuario = obtenerPorId(id);

        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuario.setFechaCambioPassword(LocalDateTime.now());

        usuarioRepository.save(usuario);
        log.info("Contraseña cambiada exitosamente");
    }

    /**
     * Desactiva un usuario (soft delete)
     */
    @Transactional
    public void desactivar(Long id) {
        log.info("Desactivando usuario con id: {}", id);

        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);

        usuarioRepository.save(usuario);
        log.info("Usuario desactivado exitosamente");
    }

    /**
     * Reactiva un usuario
     */
    @Transactional
    public void reactivar(Long id) {
        log.info("Reactivando usuario con id: {}", id);

        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(true);
        usuario.setCuentaBloqueada(false);
        usuario.setIntentosFallidos(0);

        usuarioRepository.save(usuario);
        log.info("Usuario reactivado exitosamente");
    }

    /**
     * Desbloquea una cuenta bloqueada
     */
    @Transactional
    public void desbloquear(Long id) {
        log.info("Desbloqueando cuenta de usuario id: {}", id);

        Usuario usuario = obtenerPorId(id);
        usuario.setCuentaBloqueada(false);
        usuario.setIntentosFallidos(0);

        usuarioRepository.save(usuario);
        log.info("Cuenta desbloqueada exitosamente");
    }

    /**
     * Obtiene usuarios por rol
     */
    public List<Usuario> obtenerPorRol(Rol rol) {
        log.debug("Buscando usuarios con rol: {}", rol);
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Obtiene usuarios con cuentas bloqueadas
     */
    public List<Usuario> obtenerBloqueados() {
        log.debug("Listando usuarios con cuentas bloqueadas");
        return usuarioRepository.findByCuentaBloqueadaTrue();
    }
}