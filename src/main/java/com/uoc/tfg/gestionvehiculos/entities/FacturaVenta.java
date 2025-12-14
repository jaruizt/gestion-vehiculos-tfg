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
 * @date 12/2025
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "facturas_venta",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_numero_factura_venta", columnNames = "numero_factura")
        },
        indexes = {
                @Index(name = "idx_cliente_venta", columnList = "cliente_id"),
                @Index(name = "idx_vehiculo_venta", columnList = "vehiculo_id"),
                @Index(name = "idx_reserva", columnList = "reserva_id"),
                @Index(name = "idx_fecha_factura_venta", columnList = "fecha_factura")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FacturaVenta extends AuditableEntity {

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

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El vehículo es obligatorio")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false, unique = true)
    private Vehiculo vehiculo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id")
    private ReservaVenta reserva;

    @NotNull(message = "El importe base es obligatorio")
    @DecimalMin(value = "0.0", message = "El importe base debe ser positivo")
    @Column(name = "importe_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeBase;

    @NotNull(message = "El IVA es obligatorio")
    @DecimalMin(value = "0.0", message = "El IVA debe ser positivo")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal iva;

    @DecimalMin(value = "0.0", message = "El descuento debe ser positivo")
    @Column(precision = 10, scale = 2)
    private BigDecimal descuento;

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
            BigDecimal base = importeBase;

            // Aplicar descuento si existe
            if (descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0) {
                base = base.subtract(descuento);
            }

            // Calcular IVA
            BigDecimal importeIva = base.multiply(iva).divide(new BigDecimal("100"));
            this.importeTotal = base.add(importeIva);
        }
    }

    /**
     * Calcula el beneficio de la venta
     * (Diferencia entre precio venta y precio compra)
     */
    public BigDecimal calcularBeneficio() {
        if (vehiculo != null && vehiculo.getFacturaCompra() != null) {
            BigDecimal precioCompra = vehiculo.getFacturaCompra().getImporteTotal();
            return importeTotal.subtract(precioCompra);
        }
        return BigDecimal.ZERO;
    }
}