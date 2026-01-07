package com.uoc.tfg.gestionvehiculos.dtos.cuota;

import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
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
public class CuotaRentingResponse {

    private Long id;
    private Long contratoRentingId;
    private Long contratoId;
    private String contratoNumero;
    private Integer numeroCuota;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private BigDecimal importe;
    private String estadoNombre;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
}