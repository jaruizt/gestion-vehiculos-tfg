package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import com.uoc.tfg.gestionvehiculos.enums.TipoCombustible;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehiculos", indexes = {
        @Index(name = "idx_matricula", columnList = "matricula"),
        @Index(name = "idx_situacion", columnList = "situacion_id"),
        @Index(name = "idx_marca_modelo", columnList = "marca, modelo")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String matricula;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(name = "anyo_fabricacion", nullable = false)
    private Integer anyoFabricacion;

    @Column(length = 30)
    private String color;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer kilometros;

    @Column(name = "numero_bastidor", unique = true, length = 17)
    private String numeroBastidor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_combustible", nullable = false, length = 20)
    private TipoCombustible tipoCombustible;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "situacion_id", nullable = false)
    private SituacionVehiculo situacion;

}
