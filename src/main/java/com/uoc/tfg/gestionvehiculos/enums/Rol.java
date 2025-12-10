package com.uoc.tfg.gestionvehiculos.enums;

public enum Rol {
    ADMIN("Administrador", "Acceso total al sistema"),
    GERENTE("Gerente", "Gestión de vehículos, contratos e informes"),
    COMERCIAL("Comercial", "Gestión de clientes, contratos y ventas"),
    OPERARIO("Operario", "Consulta de vehículos y contratos"),
    USUARIO("Usuario", "Acceso básico de solo lectura");

    private final String nombre;
    private final String descripcion;

    Rol(String nombre, String descripcion) {
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
