package com.uoc.tfg.gestionvehiculos.dtos.vehiculo;

import com.uoc.tfg.gestionvehiculos.enums.TipoCombustible;
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
public class VehiculoResponse {

    private Long id;
    private String matricula;
    private String marca;
    private String modelo;
    private Integer anyoFabricacion;
    private String color;
    private Integer kilometros;
    private String numeroBastidor;
    private TipoCombustible tipoCombustible;
    private String situacionNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
}