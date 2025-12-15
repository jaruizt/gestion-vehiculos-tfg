package com.uoc.tfg.gestionvehiculos.dtos.reserva;

import com.uoc.tfg.gestionvehiculos.entities.ReservaVenta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class ReservaVentaMapper {

    private ReservaVentaMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ReservaVentaResponse toResponse(ReservaVenta reserva) {
        if (reserva == null) {
            return null;
        }

        return ReservaVentaResponse.builder()
                .id(reserva.getId())
                .clienteId(reserva.getCliente() != null ? reserva.getCliente().getId() : null)
                .clienteNombre(reserva.getCliente() != null ? reserva.getCliente().getNombreCompleto() : null)
                .vehiculoId(reserva.getVehiculo() != null ? reserva.getVehiculo().getId() : null)
                .vehiculoMatricula(reserva.getVehiculo() != null ? reserva.getVehiculo().getMatricula() : null)
                .fechaReserva(reserva.getFechaReserva())
                .fechaLimite(reserva.getFechaLimite())
                .precioReserva(reserva.getPrecioReserva())
                .señal(reserva.getSeñal())
                .estado(reserva.getEstado())
                .observaciones(reserva.getObservaciones())
                .fechaCreacion(reserva.getFechaCreacion())
                .fechaActualizacion(reserva.getFechaActualizacion())
                .activo(reserva.getActivo())
                .build();
    }

    public static ReservaVenta toEntity(ReservaVentaRequest request) {
        if (request == null) {
            return null;
        }

        ReservaVenta reserva = new ReservaVenta();
        updateEntityFromRequest(request, reserva);
        return reserva;
    }

    public static void updateEntity(ReservaVentaRequest request, ReservaVenta reserva) {
        if (request == null || reserva == null) {
            return;
        }

        updateEntityFromRequest(request, reserva);
    }

    public static List<ReservaVentaResponse> toListResponse(List<ReservaVenta> reservas) {
        if (reservas == null) {
            return List.of();
        }

        return reservas.stream()
                .map(ReservaVentaMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(ReservaVentaRequest request, ReservaVenta reserva) {
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setFechaLimite(request.getFechaLimite());
        reserva.setPrecioReserva(request.getPrecioReserva());
        reserva.setSeñal(request.getSeñal());
        reserva.setEstado(request.getEstado());
        reserva.setObservaciones(request.getObservaciones());
    }
}