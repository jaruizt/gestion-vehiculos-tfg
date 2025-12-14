package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "contratos_renting",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_numero_contrato", columnNames = "numero_contrato")
        },
        indexes = {
                @Index(name = "idx_cliente", columnList = "cliente_id"),
                @Index(name = "idx_vehiculo", columnList = "vehiculo_id"),
                @Index(name = "idx_estado", columnList = "estado"),
                @Index(name = "idx_fecha_inicio", columnList = "fecha_inicio")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContratoRenting extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de contrato es obligatorio")
    @Size(max = 50)
    @Column(name = "numero_contrato", nullable = false, unique = true, length = 50)
    private String numeroContrato;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El vehículo es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull(message = "La duración en meses es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 mes")
    @Column(name = "duracion_meses", nullable = false)
    private Integer duracionMeses;

    @NotNull(message = "La cuota mensual es obligatoria")
    @DecimalMin(value = "0.0", message = "La cuota mensual debe ser positiva")
    @Column(name = "cuota_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaMensual;

    @Min(value = 0, message = "Los kilómetros incluidos deben ser positivos")
    @Column(name = "kilometros_incluidos")
    private Integer kilometrosIncluidos;

    @DecimalMin(value = "0.0", message = "El coste por kilómetro extra debe ser positivo")
    @Column(name = "coste_km_extra", precision = 5, scale = 3)
    private BigDecimal costeKmExtra;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoContrato estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CuotaRenting> cuotas = new ArrayList<>();

    /**
     * Calcula la duración en meses entre fechas
     */
    public void calcularDuracionMeses() {
        if (fechaInicio != null && fechaFin != null) {
            this.duracionMeses = (int) java.time.temporal.ChronoUnit.MONTHS.between(
                    fechaInicio,
                    fechaFin
            );
        }
    }
}