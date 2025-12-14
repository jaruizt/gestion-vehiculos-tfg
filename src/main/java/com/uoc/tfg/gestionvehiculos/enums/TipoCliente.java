package com.uoc.tfg.gestionvehiculos.enums;

/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
public enum TipoCliente {
    PARTICULAR("Particular", "Cliente individual"),
    EMPRESA("Empresa", "Cliente corporativo"),
    AUTONOMO("Autónomo", "Profesional autónomo");

    private final String nombre;
    private final String descripcion;

    TipoCliente(String nombre, String descripcion) {
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