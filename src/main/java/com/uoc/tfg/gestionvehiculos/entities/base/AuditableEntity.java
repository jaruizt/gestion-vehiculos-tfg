package com.uoc.tfg.gestionvehiculos.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditableEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column
    private LocalDateTime fechaActualizacion;

    @CreatedBy
    @Column(updatable = false, length = 100)
    private String usuarioCreacion;

    @LastModifiedBy
    @Column(length = 100)
    private String usuarioModificacion;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;
}