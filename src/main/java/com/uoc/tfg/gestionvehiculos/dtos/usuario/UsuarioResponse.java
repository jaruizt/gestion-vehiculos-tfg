package com.uoc.tfg.gestionvehiculos.dtos.usuario;

import com.uoc.tfg.gestionvehiculos.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellidos;
    private String telefono;
    private Rol rol;
    private Boolean cuentaBloqueada;
    private Boolean activo;
    private LocalDateTime fechaUltimoAcceso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}