package com.uoc.tfg.gestionvehiculos.dtos.factura;

import com.uoc.tfg.gestionvehiculos.entities.FacturaCompra;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class FacturaCompraMapper {

    private FacturaCompraMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static FacturaCompraResponse toResponse(FacturaCompra factura) {
        if (factura == null) {
            return null;
        }

        return FacturaCompraResponse.builder()
                .id(factura.getId())
                .numeroFactura(factura.getNumeroFactura())
                .fechaFactura(factura.getFechaFactura())
                .proveedorId(factura.getProveedor() != null ? factura.getProveedor().getId() : null)
                .proveedorNombre(factura.getProveedor() != null ? factura.getProveedor().getNombreComercial() : null)
                .vehiculoId(factura.getVehiculo() != null ? factura.getVehiculo().getId() : null)
                .vehiculoMatricula(factura.getVehiculo() != null ? factura.getVehiculo().getMatricula() : null)
                .vehiculoMarcaModelo(factura.getVehiculo().getMarca() + " " + factura.getVehiculo().getModelo())
                .importeBase(factura.getImporteBase())
                .iva(factura.getIva())
                .importeTotal(factura.getImporteTotal())
                .observaciones(factura.getObservaciones())
                .fechaCreacion(factura.getFechaCreacion())
                .fechaActualizacion(factura.getFechaActualizacion())
                .activo(factura.getActivo())
                .build();
    }

    public static FacturaCompra toEntity(FacturaCompraRequest request) {
        if (request == null) {
            return null;
        }

        FacturaCompra factura = new FacturaCompra();
        updateEntityFromRequest(request, factura);
        return factura;
    }

    public static void updateEntity(FacturaCompraRequest request, FacturaCompra factura) {
        if (request == null || factura == null) {
            return;
        }

        updateEntityFromRequest(request, factura);
    }

    public static List<FacturaCompraResponse> toListResponse(List<FacturaCompra> facturas) {
        if (facturas == null) {
            return List.of();
        }

        return facturas.stream()
                .map(FacturaCompraMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(FacturaCompraRequest request, FacturaCompra factura) {
        factura.setNumeroFactura(request.getNumeroFactura());
        factura.setFechaFactura(request.getFechaFactura());
        factura.setImporteBase(request.getImporteBase());
        factura.setIva(request.getIva());
        factura.setObservaciones(request.getObservaciones());
    }
}