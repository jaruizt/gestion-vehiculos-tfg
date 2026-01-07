package com.uoc.tfg.gestionvehiculos.dtos.contrato;

import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class ContratoRentingMapper {

    private ContratoRentingMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ContratoRentingResponse toResponse(ContratoRenting contrato) {
        if (contrato == null) {
            return null;
        }

        return ContratoRentingResponse.builder()
                .id(contrato.getId())
                .numeroContrato(contrato.getNumeroContrato())
                .clienteId(contrato.getCliente() != null ? contrato.getCliente().getId() : null)
                .clienteNombre(contrato.getCliente() != null ? contrato.getCliente().getNombreCompleto() : null)
                .vehiculoId(contrato.getVehiculo() != null ? contrato.getVehiculo().getId() : null)
                .vehiculoMatricula(contrato.getVehiculo() != null ? contrato.getVehiculo().getMatricula() : null)
                .fechaInicio(contrato.getFechaInicio())
                .fechaFin(contrato.getFechaFin())
                .duracionMeses(contrato.getDuracionMeses())
                .cuotaMensual(contrato.getCuotaMensual())
                .kilometrosIncluidos(contrato.getKilometrosIncluidos())
                .costeKmExtra(contrato.getCosteKmExtra())
                .estado(contrato.getEstado())
                .estadoNombre(contrato.getEstado().getNombre())
                .diaCobroCuota(contrato.getDiaCobroCuota())
                .observaciones(contrato.getObservaciones())
                .fechaCreacion(contrato.getFechaCreacion())
                .fechaActualizacion(contrato.getFechaActualizacion())
                .activo(contrato.getActivo())
                .build();
    }

    public static ContratoRenting toEntity(ContratoRentingRequest request) {
        if (request == null) {
            return null;
        }

        ContratoRenting contrato = new ContratoRenting();
        updateEntityFromRequest(request, contrato);
        return contrato;
    }

    public static void updateEntity(ContratoRentingRequest request, ContratoRenting contrato) {
        if (request == null || contrato == null) {
            return;
        }

        updateEntityFromRequest(request, contrato);
    }

    public static List<ContratoRentingResponse> toListResponse(List<ContratoRenting> contratos) {
        if (contratos == null) {
            return List.of();
        }

        return contratos.stream()
                .map(ContratoRentingMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(ContratoRentingRequest request, ContratoRenting contrato) {
        contrato.setNumeroContrato(request.getNumeroContrato());
        contrato.setFechaInicio(request.getFechaInicio());
        contrato.setFechaFin(request.getFechaFin());
        contrato.setCuotaMensual(request.getCuotaMensual());
        contrato.setKilometrosIncluidos(request.getKilometrosIncluidos());
        contrato.setCosteKmExtra(request.getCosteKmExtra());
        contrato.setEstado(request.getEstado());
        contrato.setObservaciones(request.getObservaciones());
    }
}