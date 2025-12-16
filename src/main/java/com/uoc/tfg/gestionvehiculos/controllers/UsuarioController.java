package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.usuario.UsuarioMapper;
import com.uoc.tfg.gestionvehiculos.dtos.usuario.UsuarioRequest;
import com.uoc.tfg.gestionvehiculos.dtos.usuario.UsuarioResponse;
import com.uoc.tfg.gestionvehiculos.entities.Usuario;
import com.uoc.tfg.gestionvehiculos.enums.Rol;
import com.uoc.tfg.gestionvehiculos.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Usuarios", description = "Gestión de usuarios que operan en el sistema")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<UsuarioResponse>> listarActivos() {
        log.info("Listando usuarios activos");
        List<Usuario> usuarios = usuarioService.listarActivos();
        List<UsuarioResponse> response = UsuarioMapper.toListResponse(usuarios);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo usuario", id);
        Usuario usuario = usuarioService.obtenerPorId(id);
        UsuarioResponse response = UsuarioMapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<UsuarioResponse> obtenerPorUsername(@PathVariable String username) {
        log.info("Obteniendo usuario {}", username);
        Usuario usuario = usuarioService.obtenerPorUsername(username);
        UsuarioResponse response = UsuarioMapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> obtenerPorRol(@PathVariable Rol rol) {
        log.info("Listando usuarios por rol", rol);
        List<Usuario> usuarios = usuarioService.obtenerPorRol(rol);
        List<UsuarioResponse> response = UsuarioMapper.toListResponse(usuarios);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bloqueados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> obtenerBloqueados() {
        log.info("Listando usuarios bloqueados");
        List<Usuario> usuarios = usuarioService.obtenerBloqueados();
        List<UsuarioResponse> response = UsuarioMapper.toListResponse(usuarios);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        log.info("Creando usuario {}", request.getUsername());

        Usuario usuario = UsuarioMapper.toEntity(request);
        usuario.setPassword(request.getPassword());

        Usuario creado = usuarioService.crear(usuario);
        UsuarioResponse response = UsuarioMapper.toResponse(creado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        log.info("Actualizando usuario {}", id);

        Usuario usuario = usuarioService.obtenerPorId(id);
        UsuarioMapper.updateEntity(request, usuario);

        Usuario actualizado = usuarioService.actualizar(id, usuario);
        UsuarioResponse response = UsuarioMapper.toResponse(actualizado);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite a un usuario cambiar su contraseña proporcionando la actual y la nueva"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente"),
            @ApiResponse(responseCode = "422", description = "La contraseña actual es incorrecta")
    })
    @PatchMapping("/{id}/cambiar-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO', 'USUARIO')")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {

        log.info("Cambiando contraseña usuario {}", id);

        String passwordActual = passwords.get("passwordActual");
        String passwordNueva = passwords.get("passwordNueva");

        usuarioService.cambiarPassword(id, passwordActual, passwordNueva);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña actualizada exitosamente");

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

    @Operation(
            summary = "Desbloquear cuenta",
            description = "Desbloquea una cuenta de usuario que ha sido bloqueada por intentos fallidos"
    )
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