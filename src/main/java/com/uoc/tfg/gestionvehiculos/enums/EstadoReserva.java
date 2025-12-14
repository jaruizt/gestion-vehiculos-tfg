package com.uoc.tfg.gestionvehiculos.enums;


/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
public enum EstadoReserva {
    PENDIENTE("Pendiente", "Reserva pendiente de confirmación"),
    CONFIRMADA("Confirmada", "Reserva confirmada"),
    CANCELADA("Cancelada", "Reserva cancelada"),
    COMPLETADA("Completada", "Venta completada");

    private final String nombre;
    private final String descripcion;

    EstadoReserva(String nombre, String descripcion) {
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