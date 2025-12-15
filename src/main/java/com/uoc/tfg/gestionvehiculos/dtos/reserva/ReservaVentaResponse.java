package com.uoc.tfg.gestionvehiculos.dtos.reserva;

import com.uoc.tfg.gestionvehiculos.enums.EstadoReserva;
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
public class ReservaVentaResponse {

    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private Long vehiculoId;
    private String vehiculoMatricula;
    private LocalDate fechaReserva;
    private LocalDate fechaLimite;
    private BigDecimal precioReserva;
    private BigDecimal señal;
    private EstadoReserva estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
}