package com.uoc.tfg.gestionvehiculos.dtos.vehiculo;

import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class VehiculoMapper {

    private VehiculoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static VehiculoResponse toResponse(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }

        return VehiculoResponse.builder()
                .id(vehiculo.getId())
                .matricula(vehiculo.getMatricula())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anyoFabricacion(vehiculo.getAnyoFabricacion())
                .color(vehiculo.getColor())
                .kilometros(vehiculo.getKilometros())
                .numeroBastidor(vehiculo.getNumeroBastidor())
                .tipoCombustible(vehiculo.getTipoCombustible())
                .situacionNombre(vehiculo.getSituacion() != null ? vehiculo.getSituacion().getNombre() : null)
                .fechaCreacion(vehiculo.getFechaCreacion())
                .fechaActualizacion(vehiculo.getFechaActualizacion())
                .activo(vehiculo.getActivo())
                .build();
    }

    public static Vehiculo toEntity(VehiculoRequest request) {
        if (request == null) {
            return null;
        }

        Vehiculo vehiculo = new Vehiculo();
        updateEntityFromRequest(request, vehiculo);
        return vehiculo;
    }

    public static void updateEntity(VehiculoRequest request, Vehiculo vehiculo) {
        if (request == null || vehiculo == null) {
            return;
        }

        updateEntityFromRequest(request, vehiculo);
    }

    private static void updateEntityFromRequest(VehiculoRequest request, Vehiculo vehiculo) {
        vehiculo.setMatricula(request.getMatricula());
        vehiculo.setMarca(request.getMarca());
        vehiculo.setModelo(request.getModelo());
        vehiculo.setAnyoFabricacion(request.getAnyoFabricacion());
        vehiculo.setColor(request.getColor());
        vehiculo.setKilometros(request.getKilometros());
        vehiculo.setNumeroBastidor(request.getNumeroBastidor());
        vehiculo.setTipoCombustible(request.getTipoCombustible());
    }
}