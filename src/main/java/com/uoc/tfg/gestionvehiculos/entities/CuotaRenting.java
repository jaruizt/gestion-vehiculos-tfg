package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "cuotas_renting",
        indexes = {
                @Index(name = "idx_contrato", columnList = "contrato_id"),
                @Index(name = "idx_estado", columnList = "estado"),
                @Index(name = "idx_fecha_vencimiento", columnList = "fecha_vencimiento")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CuotaRenting extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El contrato es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private ContratoRenting contrato;

    @NotNull(message = "El número de cuota es obligatorio")
    @Min(value = 1, message = "El número de cuota debe ser al menos 1")
    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.0", message = "El importe debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCuota estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Marca la cuota como pagada
     */
    public void marcarComoPagada() {
        this.estado = EstadoCuota.PAGADA;
        this.fechaPago = LocalDate.now();
    }

    /**
     * Verifica si la cuota está vencida
     */
    public boolean estaVencida() {
        return estado == EstadoCuota.PENDIENTE &&
                fechaVencimiento.isBefore(LocalDate.now());
    }
}