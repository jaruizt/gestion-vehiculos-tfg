package com.uoc.tfg.gestionvehiculos.enums;

/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
public enum EstadoCuota {
    PENDIENTE("Pendiente", "Pendiente de pago"),
    PAGADA("Pagada", "Cuota pagada"),
    VENCIDA("Vencida", "Cuota vencida sin pagar"),
    CANCELADA("Cancelada", "Cuota cancelada");

    private final String nombre;
    private final String descripcion;

    EstadoCuota(String nombre, String descripcion) {
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