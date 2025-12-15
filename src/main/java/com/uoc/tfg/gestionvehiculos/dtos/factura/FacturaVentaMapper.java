package com.uoc.tfg.gestionvehiculos.dtos.factura;

import com.uoc.tfg.gestionvehiculos.entities.FacturaVenta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class FacturaVentaMapper {

    private FacturaVentaMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static FacturaVentaResponse toResponse(FacturaVenta factura) {
        if (factura == null) {
            return null;
        }

        return FacturaVentaResponse.builder()
                .id(factura.getId())
                .numeroFactura(factura.getNumeroFactura())
                .fechaFactura(factura.getFechaFactura())
                .clienteId(factura.getCliente() != null ? factura.getCliente().getId() : null)
                .clienteNombre(factura.getCliente() != null ? factura.getCliente().getNombreCompleto() : null)
                .vehiculoId(factura.getVehiculo() != null ? factura.getVehiculo().getId() : null)
                .vehiculoMatricula(factura.getVehiculo() != null ? factura.getVehiculo().getMatricula() : null)
                .reservaId(factura.getReserva() != null ? factura.getReserva().getId() : null)
                .importeBase(factura.getImporteBase())
                .iva(factura.getIva())
                .descuento(factura.getDescuento())
                .importeTotal(factura.getImporteTotal())
                .observaciones(factura.getObservaciones())
                .fechaCreacion(factura.getFechaCreacion())
                .fechaActualizacion(factura.getFechaActualizacion())
                .activo(factura.getActivo())
                .build();
    }

    public static FacturaVenta toEntity(FacturaVentaRequest request) {
        if (request == null) {
            return null;
        }

        FacturaVenta factura = new FacturaVenta();
        updateEntityFromRequest(request, factura);
        return factura;
    }

    public static void updateEntity(FacturaVentaRequest request, FacturaVenta factura) {
        if (request == null || factura == null) {
            return;
        }

        updateEntityFromRequest(request, factura);
    }

    public static List<FacturaVentaResponse> toListResponse(List<FacturaVenta> facturas) {
        if (facturas == null) {
            return List.of();
        }

        return facturas.stream()
                .map(FacturaVentaMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(FacturaVentaRequest request, FacturaVenta factura) {
        factura.setNumeroFactura(request.getNumeroFactura());
        factura.setFechaFactura(request.getFechaFactura());
        factura.setImporteBase(request.getImporteBase());
        factura.setIva(request.getIva());
        factura.setDescuento(request.getDescuento());
        factura.setObservaciones(request.getObservaciones());
    }
}