package com.uoc.tfg.gestionvehiculos.dtos.contrato;

import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import jakarta.validation.constraints.*;
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
public class ContratoRentingRequest {

    @Size(max = 50, message = "El número de contrato no puede tener más de 50 caracteres")
    private String numeroContrato;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "La cuota mensual es obligatoria")
    @DecimalMin(value = "0.0", message = "La cuota mensual debe ser positiva")
    private BigDecimal cuotaMensual;

    @Min(value = 0, message = "Los kilómetros incluidos deben ser positivos")
    private Integer kilometrosIncluidos;

    @DecimalMin(value = "0.0", message = "El coste por km extra debe ser positivo")
    private BigDecimal costeKmExtra;

    @NotNull(message = "El estado es obligatorio")
    private EstadoContrato estado;

    private String observaciones;
}