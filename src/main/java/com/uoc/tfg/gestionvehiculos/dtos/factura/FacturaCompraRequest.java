package com.uoc.tfg.gestionvehiculos.dtos.factura;

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
public class FacturaCompraRequest {

    @NotBlank(message = "El número de factura es obligatorio")
    @Size(max = 50, message = "El número de factura no puede tener más de 50 caracteres")
    private String numeroFactura;

    @NotNull(message = "La fecha de factura es obligatoria")
    private LocalDate fechaFactura;

    @NotNull(message = "El proveedor es obligatorio")
    private Long proveedorId;

    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El importe base es obligatorio")
    @DecimalMin(value = "0.0", message = "El importe base debe ser positivo")
    private BigDecimal importeBase;

    @NotNull(message = "El IVA es obligatorio")
    @DecimalMin(value = "0.0", message = "El IVA debe ser positivo")
    @DecimalMax(value = "100.0", message = "El IVA no puede ser mayor a 100")
    private BigDecimal iva;

    private String observaciones;
}