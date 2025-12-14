package com.uoc.tfg.gestionvehiculos.enums;

/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
public enum EstadoContrato {
    ACTIVO("Activo", "Contrato en vigor"),
    FINALIZADO("Finalizado", "Contrato finalizado"),
    CANCELADO("Cancelado", "Contrato cancelado antes de tiempo"),
    PENDIENTE("Pendiente", "Pendiente de firma");

    private final String nombre;
    private final String descripcion;

    EstadoContrato(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}