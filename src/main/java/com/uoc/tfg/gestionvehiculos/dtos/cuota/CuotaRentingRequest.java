package com.uoc.tfg.gestionvehiculos.dtos.cuota;

import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
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
public class CuotaRentingRequest {

    @NotNull(message = "El contrato es obligatorio")
    private Long contratoId;

    @NotNull(message = "El número de cuota es obligatorio")
    @Min(value = 1, message = "El número de cuota debe ser al menos 1")
    private Integer numeroCuota;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;

    private LocalDate fechaPago;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.0", message = "El importe debe ser positivo")
    private BigDecimal importe;

    @NotNull(message = "El estado es obligatorio")
    private EstadoCuota estado;

    private String observaciones;
}