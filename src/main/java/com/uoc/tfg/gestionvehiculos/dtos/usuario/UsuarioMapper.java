package com.uoc.tfg.gestionvehiculos.dtos.usuario;

import com.uoc.tfg.gestionvehiculos.entities.Usuario;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class UsuarioMapper {

    private UsuarioMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellidos(usuario.getApellidos())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .cuentaBloqueada(usuario.getCuentaBloqueada())
                .activo(usuario.getActivo())
                .fechaUltimoAcceso(usuario.getFechaUltimoAcceso())
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }

    public static Usuario toEntity(UsuarioRequest request) {
        if (request == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        updateEntityFromRequest(request, usuario);
        return usuario;
    }

    public static void updateEntity(UsuarioRequest request, Usuario usuario) {
        if (request == null || usuario == null) {
            return;
        }

        updateEntityFromRequest(request, usuario);
    }

    public static List<UsuarioResponse> toListResponse(List<Usuario> usuarios) {
        if (usuarios == null) {
            return List.of();
        }

        return usuarios.stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(UsuarioRequest request, Usuario usuario) {
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setNombre(request.getNombre());
        usuario.setApellidos(request.getApellidos());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(request.getRol());
    }
}