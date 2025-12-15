package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.Usuario;
import com.uoc.tfg.gestionvehiculos.enums.Rol;
import com.uoc.tfg.gestionvehiculos.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<Usuario>> listarActivos() {
        log.info("Listando usuarios activos");
        List<Usuario> usuarios = usuarioService.listarActivos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo usuario", id);
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Usuario> obtenerPorUsername(@PathVariable String username) {
        log.info("Obteniendo usuario {}", username);
        Usuario usuario = usuarioService.obtenerPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable Rol rol) {
        log.info("Listando usuarios por rol", rol);
        List<Usuario> usuarios = usuarioService.obtenerPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/bloqueados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> obtenerBloqueados() {
        log.info("Listando usuarios bloqueados");
        List<Usuario> usuarios = usuarioService.obtenerBloqueados();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        log.info("Creando usuario: {}", usuario.getUsername());
        Usuario creado = usuarioService.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        log.info("Actualizando usuario", id);
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/cambiar-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO', 'USUARIO')")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {

        log.info("Cambiando contraseña de {}", id);

        String passwordActual = passwords.get("passwordActual");
        String passwordNueva = passwords.get("passwordNueva");

        usuarioService.cambiarPassword(id, passwordActual, passwordNueva);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña actualizada ");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desactivar(@PathVariable Long id) {
        log.info("Desactivando usuario {}", id);

        usuarioService.desactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario desactivado ");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> reactivar(@PathVariable Long id) {
        log.info("Reactivando usuario {}", id);

        usuarioService.reactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario reactivado ");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desbloquear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desbloquear(@PathVariable Long id) {
        log.info("Desbloqueando cuenta {}", id);

        usuarioService.desbloquear(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cuenta desbloqueada ");

        return ResponseEntity.ok(response);
    }
}