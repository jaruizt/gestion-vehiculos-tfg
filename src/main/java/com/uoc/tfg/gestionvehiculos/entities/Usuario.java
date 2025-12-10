package com.uoc.tfg.gestionvehiculos.entities;

import com.uoc.tfg.gestionvehiculos.entities.base.AuditableEntity;
import com.uoc.tfg.gestionvehiculos.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_activo", columnList = "activo")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends AuditableEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false, length = 255)
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String apellidos;

    @Size(max = 20)
    @Column(length = 20)
    private String telefono;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(name = "cuenta_bloqueada", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean cuentaBloqueada = false;

    @Column(name = "cuenta_expirada", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean cuentaExpirada = false;

    @Column(name = "credenciales_expiradas", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean credencialesExpiradas = false;

    @Column(name = "intentos_fallidos", columnDefinition = "INT DEFAULT 0")
    private Integer intentosFallidos = 0;

    @Column(name = "fecha_ultimo_acceso")
    private LocalDateTime fechaUltimoAcceso;

    @Column(name = "fecha_cambio_password")
    private LocalDateTime fechaCambioPassword;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !cuentaExpirada;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !cuentaBloqueada;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credencialesExpiradas;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(super.getActivo());
    }

    // ========== MÉTODOS HELPER ==========

    /**
     * Retorna el nombre completo del usuario
     */
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    /**
     * Registra un intento de login fallido
     */
    public void registrarIntentoFallido() {
        this.intentosFallidos++;
        if (this.intentosFallidos >= 5) {
            this.cuentaBloqueada = true;
        }
    }

    /**
     * Resetea los intentos fallidos (después de login exitoso)
     */
    public void resetearIntentosFallidos() {
        this.intentosFallidos = 0;
    }

    /**
     * Registra último acceso exitoso
     */
    public void registrarAcceso() {
        this.fechaUltimoAcceso = LocalDateTime.now();
        resetearIntentosFallidos();
    }
}
