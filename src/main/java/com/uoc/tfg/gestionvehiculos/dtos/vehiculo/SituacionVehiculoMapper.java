package com.uoc.tfg.gestionvehiculos.dtos.vehiculo;

import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class SituacionVehiculoMapper {

    private SituacionVehiculoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SituacionVehiculoResponse toResponse(SituacionVehiculo situacion) {
        if (situacion == null) {
            return null;
        }

        return SituacionVehiculoResponse.builder()
                .id(situacion.getId())
                .nombre(situacion.getNombre())
                .descripcion(situacion.getDescripcion())
                .orden(situacion.getOrden())
                .fechaCreacion(situacion.getFechaCreacion())
                .fechaActualizacion(situacion.getFechaActualizacion())
                .activo(situacion.getActivo())
                .build();
    }

    public static SituacionVehiculo toEntity(SituacionVehiculoRequest request) {
        if (request == null) {
            return null;
        }

        SituacionVehiculo situacion = new SituacionVehiculo();
        updateEntityFromRequest(request, situacion);
        return situacion;
    }

    public static void updateEntity(SituacionVehiculoRequest request, SituacionVehiculo situacion) {
        if (request == null || situacion == null) {
            return;
        }

        updateEntityFromRequest(request, situacion);
    }

    public static List<SituacionVehiculoResponse> toListResponse(List<SituacionVehiculo> situaciones) {
        if (situaciones == null) {
            return List.of();
        }

        return situaciones.stream()
                .map(SituacionVehiculoMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(SituacionVehiculoRequest request, SituacionVehiculo situacion) {
        situacion.setNombre(request.getNombre());
        situacion.setDescripcion(request.getDescripcion());
        situacion.setOrden(request.getOrden());
    }
}