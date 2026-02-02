package com.uoc.tfg.gestionvehiculos.dtos.factura;

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
public class FacturaVentaResponse {

    private Long id;
    private String numeroFactura;
    private LocalDate fechaFactura;
    private Long clienteId;
    private String clienteNombre;
    private Long vehiculoId;
    private String vehiculoMatricula;
    private String vehiculoMarcaModelo;
    private Long reservaId;
    private BigDecimal importeBase;
    private BigDecimal iva;
    private BigDecimal descuento;
    private BigDecimal importeTotal;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
}