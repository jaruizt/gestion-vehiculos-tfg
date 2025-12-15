package com.uoc.tfg.gestionvehiculos.dtos.proveedor;

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
public class ProveedorResponse {

    private Long id;
    private String cif;
    private String razonSocial;
    private String nombreComercial;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;
    private String telefono;
    private String email;
    private String personaContacto;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
}