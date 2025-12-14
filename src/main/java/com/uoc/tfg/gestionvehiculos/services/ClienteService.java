package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
import com.uoc.tfg.gestionvehiculos.exceptions.BusinessRuleException;
import com.uoc.tfg.gestionvehiculos.exceptions.DuplicateResourceException;
import com.uoc.tfg.gestionvehiculos.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Lista todos los clientes activos
     */
    public List<Cliente> listarActivos() {
        log.debug("Listando todos los clientes activos");
        return clienteRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .toList();
    }

    /**
     * Obtiene un cliente por ID
     */
    public Cliente obtenerPorId(Long id) {
        log.debug("Buscando cliente con id: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    /**
     * Obtiene un cliente por documento
     */
    public Cliente obtenerPorDocumento(String documento) {
        log.debug("Buscando cliente con documento: {}", documento);
        return clienteRepository.findByDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con documento: " + documento));
    }

    /**
     * Busca clientes por nombre (búsqueda parcial)
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        log.debug("Buscando clientes con nombre: {}", nombre);
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene clientes por tipo
     */
    public List<Cliente> obtenerPorTipo(TipoCliente tipo) {
        log.debug("Listando clientes de tipo: {}", tipo);
        return clienteRepository.findByTipoCliente(tipo);
    }

    /**
     * Crea un nuevo cliente
     */
    @Transactional
    public Cliente crear(Cliente cliente) {
        log.info("Creando nuevo cliente: {}", cliente.getNombreCompleto());

        if (clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new DuplicateResourceException("cliente", "documento", cliente.getDocumento());
        }

        validarDatosSegunTipo(cliente);

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente creado con id: {}", guardado.getId());

        return guardado;
    }

    /**
     * Actualiza un cliente existente
     */
    @Transactional
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        log.info("Actualizando cliente con id: {}", id);

        Cliente clienteExistente = obtenerPorId(id);

        if (!clienteExistente.getDocumento().equals(clienteActualizado.getDocumento())) {
            if (clienteRepository.existsByDocumento(clienteActualizado.getDocumento())) {
                throw new DuplicateResourceException("cliente", "documento", clienteActualizado.getDocumento());
            }
        }

        validarDatosSegunTipo(clienteActualizado);

        clienteExistente.setTipoCliente(clienteActualizado.getTipoCliente());
        clienteExistente.setDocumento(clienteActualizado.getDocumento());
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellidos(clienteActualizado.getApellidos());
        clienteExistente.setRazonSocial(clienteActualizado.getRazonSocial());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setCiudad(clienteActualizado.getCiudad());
        clienteExistente.setProvincia(clienteActualizado.getProvincia());
        clienteExistente.setCodigoPostal(clienteActualizado.getCodigoPostal());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setObservaciones(clienteActualizado.getObservaciones());

        Cliente actualizado = clienteRepository.save(clienteExistente);
        log.info("Cliente actualizado");

        return actualizado;
    }

    /**
     * Desactiva un cliente
     */
    @Transactional
    public void desactivar(Long id) {
        log.info("Desactivando cliente con id: {}", id);

        Cliente cliente = obtenerPorId(id);
        cliente.setActivo(false);

        clienteRepository.save(cliente);
        log.info("Cliente desactivado");
    }

    /**
     * Reactiva un cliente
     */
    @Transactional
    public void reactivar(Long id) {
        log.info("Reactivando cliente con id: {}", id);

        Cliente cliente = obtenerPorId(id);
        cliente.setActivo(true);

        clienteRepository.save(cliente);
        log.info("Cliente reactivado");
    }

    /**
     * Valida coherencia de datos según el tipo de cliente
     */
    private void validarDatosSegunTipo(Cliente cliente) {
        if (cliente.getTipoCliente() == TipoCliente.EMPRESA) {
            if (cliente.getRazonSocial() == null || cliente.getRazonSocial().isBlank()) {
                throw new BusinessRuleException("La razón social es obligatoria para empresas");
            }
        } else {
            if (cliente.getApellidos() == null || cliente.getApellidos().isBlank()) {
                throw new BusinessRuleException("Los apellidos son obligatorios para particulares y autónomos");
            }
        }
    }
}