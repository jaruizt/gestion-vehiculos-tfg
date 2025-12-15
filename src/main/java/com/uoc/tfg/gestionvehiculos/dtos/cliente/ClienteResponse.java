package com.uoc.tfg.gestionvehiculos.dtos.cliente;

import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
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
public class ClienteResponse {

    private Long id;
    private TipoCliente tipoCliente;
    private String documento;
    private String nombre;
    private String apellidos;
    private String razonSocial;
    private String nombreCompleto;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;
    private String telefono;
    private String email;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
}