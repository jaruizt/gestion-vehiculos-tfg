package com.uoc.tfg.gestionvehiculos.dtos.cuota;

import com.uoc.tfg.gestionvehiculos.entities.CuotaRenting;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class CuotaRentingMapper {

    private CuotaRentingMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CuotaRentingResponse toResponse(CuotaRenting cuota) {
        if (cuota == null) {
            return null;
        }

        return CuotaRentingResponse.builder()
                .id(cuota.getId())
                .contratoId(cuota.getContrato() != null ? cuota.getContrato().getId() : null)
                .contratoNumero(cuota.getContrato() != null ? cuota.getContrato().getNumeroContrato() : null)
                .numeroCuota(cuota.getNumeroCuota())
                .fechaVencimiento(cuota.getFechaVencimiento())
                .fechaPago(cuota.getFechaPago())
                .importe(cuota.getImporte())
                .estado(cuota.getEstado())
                .observaciones(cuota.getObservaciones())
                .fechaCreacion(cuota.getFechaCreacion())
                .fechaActualizacion(cuota.getFechaActualizacion())
                .activo(cuota.getActivo())
                .build();
    }

    public static CuotaRenting toEntity(CuotaRentingRequest request) {
        if (request == null) {
            return null;
        }

        CuotaRenting cuota = new CuotaRenting();
        updateEntityFromRequest(request, cuota);
        return cuota;
    }

    public static void updateEntity(CuotaRentingRequest request, CuotaRenting cuota) {
        if (request == null || cuota == null) {
            return;
        }

        updateEntityFromRequest(request, cuota);
    }

    public static List<CuotaRentingResponse> toListResponse(List<CuotaRenting> cuotas) {
        if (cuotas == null) {
            return List.of();
        }

        return cuotas.stream()
                .map(CuotaRentingMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(CuotaRentingRequest request, CuotaRenting cuota) {
        cuota.setNumeroCuota(request.getNumeroCuota());
        cuota.setFechaVencimiento(request.getFechaVencimiento());
        cuota.setFechaPago(request.getFechaPago());
        cuota.setImporte(request.getImporte());
        cuota.setEstado(request.getEstado());
        cuota.setObservaciones(request.getObservaciones());
    }
}