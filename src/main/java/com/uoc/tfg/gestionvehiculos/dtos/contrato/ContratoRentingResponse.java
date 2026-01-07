package com.uoc.tfg.gestionvehiculos.dtos.contrato;

import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class ContratoRentingResponse {

    private Long id;
    private String numeroContrato;
    private Long clienteId;
    private String clienteNombre;
    private Long vehiculoId;
    private String vehiculoMatricula;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer duracionMeses;
    private BigDecimal cuotaMensual;
    private Integer kilometrosIncluidos;
    private BigDecimal costeKmExtra;
    private EstadoContrato estado;
    private String estadoNombre;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
    private Integer diaCobroCuota;
}