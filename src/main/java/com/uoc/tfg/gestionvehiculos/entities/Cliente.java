package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "clientes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_documento", columnNames = "documento")
        },
        indexes = {
                @Index(name = "idx_nombre", columnList = "nombre"),
                @Index(name = "idx_documento", columnList = "documento"),
                @Index(name = "idx_tipo_cliente", columnList = "tipo_cliente")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false, length = 20)
    private TipoCliente tipoCliente;

    @NotBlank(message = "El documento (DNI/CIF) es obligatorio")
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String nombre;

    @Size(max = 200)
    @Column(length = 200)
    private String apellidos;

    @Size(max = 200)
    @Column(name = "razon_social", length = 200)
    private String razonSocial;

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

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Retorna el nombre completo del cliente
     * Para empresas retorna razonSocial
     * Para particulares retorna nombre + apellidos
     */
    public String getNombreCompleto() {
        if (tipoCliente == TipoCliente.EMPRESA) {
            return razonSocial != null ? razonSocial : nombre;
        }
        return apellidos != null ? nombre + " " + apellidos : nombre;
    }
}