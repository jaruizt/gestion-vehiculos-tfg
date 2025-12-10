package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "situaciones_vehiculo")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SituacionVehiculo extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String descripcion;

    @Column(name = "orden_visualizacion")
    private Integer orden;
}
