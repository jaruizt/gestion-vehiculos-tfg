package com.uoc.tfg.gestionvehiculos.dtos.proveedor;

import com.uoc.tfg.gestionvehiculos.entities.Proveedor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class ProveedorMapper {

    private ProveedorMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ProveedorResponse toResponse(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        return ProveedorResponse.builder()
                .id(proveedor.getId())
                .cif(proveedor.getCif())
                .razonSocial(proveedor.getRazonSocial())
                .nombreComercial(proveedor.getNombreComercial())
                .direccion(proveedor.getDireccion())
                .ciudad(proveedor.getCiudad())
                .provincia(proveedor.getProvincia())
                .codigoPostal(proveedor.getCodigoPostal())
                .telefono(proveedor.getTelefono())
                .email(proveedor.getEmail())
                .personaContacto(proveedor.getPersonaContacto())
                .observaciones(proveedor.getObservaciones())
                .fechaCreacion(proveedor.getFechaCreacion())
                .fechaActualizacion(proveedor.getFechaActualizacion())
                .activo(proveedor.getActivo())
                .build();
    }

    public static Proveedor toEntity(ProveedorRequest request) {
        if (request == null) {
            return null;
        }

        Proveedor proveedor = new Proveedor();
        updateEntityFromRequest(request, proveedor);
        return proveedor;
    }

    public static void updateEntity(ProveedorRequest request, Proveedor proveedor) {
        if (request == null || proveedor == null) {
            return;
        }

        updateEntityFromRequest(request, proveedor);
    }

    public static List<ProveedorResponse> toListResponse(List<Proveedor> proveedores) {
        if (proveedores == null) {
            return List.of();
        }

        return proveedores.stream()
                .map(ProveedorMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(ProveedorRequest request, Proveedor proveedor) {
        proveedor.setCif(request.getCif());
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setNombreComercial(request.getNombreComercial());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setCiudad(request.getCiudad());
        proveedor.setProvincia(request.getProvincia());
        proveedor.setCodigoPostal(request.getCodigoPostal());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());
        proveedor.setPersonaContacto(request.getPersonaContacto());
        proveedor.setObservaciones(request.getObservaciones());
    }
}