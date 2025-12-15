package com.uoc.tfg.gestionvehiculos.dtos.cliente;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
public class ClienteMapper {

    private ClienteMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return ClienteResponse.builder()
                .id(cliente.getId())
                .tipoCliente(cliente.getTipoCliente())
                .documento(cliente.getDocumento())
                .nombre(cliente.getNombre())
                .apellidos(cliente.getApellidos())
                .razonSocial(cliente.getRazonSocial())
                .nombreCompleto(cliente.getNombreCompleto())
                .direccion(cliente.getDireccion())
                .ciudad(cliente.getCiudad())
                .provincia(cliente.getProvincia())
                .codigoPostal(cliente.getCodigoPostal())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .observaciones(cliente.getObservaciones())
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaActualizacion(cliente.getFechaActualizacion())
                .activo(cliente.getActivo())
                .build();
    }

    public static Cliente toEntity(ClienteRequest request) {
        if (request == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        updateEntityFromRequest(request, cliente);
        return cliente;
    }

    public static void updateEntity(ClienteRequest request, Cliente cliente) {
        if (request == null || cliente == null) {
            return;
        }

        updateEntityFromRequest(request, cliente);
    }

    public static List<ClienteResponse> toListResponse(List<Cliente> clientes) {
        if (clientes == null) {
            return List.of();
        }

        return clientes.stream()
                .map(ClienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static void updateEntityFromRequest(ClienteRequest request, Cliente cliente) {
        cliente.setTipoCliente(request.getTipoCliente());
        cliente.setDocumento(request.getDocumento());
        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setRazonSocial(request.getRazonSocial());
        cliente.setDireccion(request.getDireccion());
        cliente.setCiudad(request.getCiudad());
        cliente.setProvincia(request.getProvincia());
        cliente.setCodigoPostal(request.getCodigoPostal());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setObservaciones(request.getObservaciones());
    }
}