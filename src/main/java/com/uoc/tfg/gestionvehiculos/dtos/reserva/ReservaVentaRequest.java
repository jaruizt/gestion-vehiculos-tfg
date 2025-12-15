package com.uoc.tfg.gestionvehiculos.dtos.reserva;

import com.uoc.tfg.gestionvehiculos.enums.EstadoReserva;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaVentaRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "La fecha de reserva es obligatoria")
    private LocalDate fechaReserva;

    private LocalDate fechaLimite;

    @NotNull(message = "El precio de reserva es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser positivo")
    private BigDecimal precioReserva;

    @DecimalMin(value = "0.0", message = "La señal debe ser positiva")
    private BigDecimal señal;

    @NotNull(message = "El estado es obligatorio")
    private EstadoReserva estado;

    private String observaciones;
}