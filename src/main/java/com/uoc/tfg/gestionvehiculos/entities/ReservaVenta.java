package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import com.uoc.tfg.gestionvehiculos.enums.EstadoReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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
@Table(name = "reservas_venta",
        indexes = {
                @Index(name = "idx_cliente", columnList = "cliente_id"),
                @Index(name = "idx_vehiculo", columnList = "vehiculo_id"),
                @Index(name = "idx_estado", columnList = "estado"),
                @Index(name = "idx_fecha_reserva", columnList = "fecha_reserva")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReservaVenta extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El vehículo es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @NotNull(message = "La fecha de reserva es obligatoria")
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @NotNull(message = "El precio reserva es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser positivo")
    @Column(name = "precio_reserva", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioReserva;

    @DecimalMin(value = "0.0", message = "La señal debe ser positiva")
    @Column(precision = 10, scale = 2)
    private BigDecimal señal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Verifica si la reserva ha expirado
     */
    public boolean estaExpirada() {
        return estado == EstadoReserva.PENDIENTE &&
                fechaLimite != null &&
                fechaLimite.isBefore(LocalDate.now());
    }

    /**
     * Confirma la reserva
     */
    public void confirmar() {
        this.estado = EstadoReserva.CONFIRMADA;
    }

    /**
     * Cancela la reserva
     */
    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }

    /**
     * Marca la reserva como completada (venta realizada)
     */
    public void completar() {
        this.estado = EstadoReserva.COMPLETADA;
    }
}