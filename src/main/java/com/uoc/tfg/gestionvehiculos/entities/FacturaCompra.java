package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "facturas_compra",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_numero_factura", columnNames = "numero_factura")
        },
        indexes = {
                @Index(name = "idx_proveedor", columnList = "proveedor_id"),
                @Index(name = "idx_vehiculo", columnList = "vehiculo_id"),
                @Index(name = "idx_fecha_factura", columnList = "fecha_factura")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FacturaCompra extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de factura es obligatorio")
    @Size(max = 50)
    @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
    private String numeroFactura;

    @NotNull(message = "La fecha de factura es obligatoria")
    @Column(name = "fecha_factura", nullable = false)
    private LocalDate fechaFactura;

    @NotNull(message = "El proveedor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @NotNull(message = "El vehículo es obligatorio")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false, unique = true)
    private Vehiculo vehiculo;

    @NotNull(message = "El importe base es obligatorio")
    @DecimalMin(value = "0.0", message = "El importe base debe ser positivo")
    @Column(name = "importe_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeBase;

    @NotNull(message = "El IVA es obligatorio")
    @DecimalMin(value = "0.0", message = "El IVA debe ser positivo")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal iva;

    @NotNull(message = "El importe total es obligatorio")
    @DecimalMin(value = "0.0", message = "El importe total debe ser positivo")
    @Column(name = "importe_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeTotal;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Calcula el importe total automáticamente
     */
    public void calcularImporteTotal() {
        if (importeBase != null && iva != null) {
            BigDecimal importeIva = importeBase.multiply(iva).divide(new BigDecimal("100"));
            this.importeTotal = importeBase.add(importeIva);
        }
    }
}