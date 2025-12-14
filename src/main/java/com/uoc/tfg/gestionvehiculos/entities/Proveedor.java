package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "proveedores",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cif", columnNames = "cif")
        },
        indexes = {
                @Index(name = "idx_nombre_comercial", columnList = "nombre_comercial"),
                @Index(name = "idx_cif", columnList = "cif")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El CIF es obligatorio")
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String cif;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200)
    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @NotBlank(message = "El nombre comercial es obligatorio")
    @Size(max = 200)
    @Column(name = "nombre_comercial", nullable = false, length = 200)
    private String nombreComercial;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 300)
    @Column(nullable = false, length = 300)
    private String direccion;

    @Size(max = 100)
    @Column(length = 100)
    private String ciudad;

    @Size(max = 100)
    @Column(length = 100)
    private String provincia;

    @Size(max = 10)
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Size(max = 20)
    @Column(length = 20)
    private String telefono;

    @Email(message = "Email inválido")
    @Size(max = 100)
    @Column(length = 100)
    private String email;

    @Size(max = 100)
    @Column(name = "persona_contacto", length = 100)
    private String personaContacto;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}