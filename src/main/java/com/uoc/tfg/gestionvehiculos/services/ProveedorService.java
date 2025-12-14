package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
import com.uoc.tfg.gestionvehiculos.repositories.ProveedorRepository;
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
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    /**
     * Lista todos los proveedores activos
     */
    public List<Proveedor> listarActivos() {
        log.debug("Listando todos los proveedores activos");
        return proveedorRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .toList();
    }

    /**
     * Obtiene un proveedor por ID
     */
    public Proveedor obtenerPorId(Long id) {
        log.debug("Buscando proveedor con id: {}", id);
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
    }

    /**
     * Obtiene un proveedor por CIF
     */
    public Proveedor obtenerPorCif(String cif) {
        log.debug("Buscando proveedor con CIF: {}", cif);
        return proveedorRepository.findByCif(cif)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con CIF: " + cif));
    }

    /**
     * Crea un nuevo proveedor
     */
    @Transactional
    public Proveedor crear(Proveedor proveedor) {
        log.info("Creando nuevo proveedor: {}", proveedor.getNombreComercial());

        if (proveedorRepository.existsByCif(proveedor.getCif())) {
            throw new RuntimeException("Ya existe un proveedor con el CIF: " + proveedor.getCif());
        }

        Proveedor guardado = proveedorRepository.save(proveedor);
        log.info("Proveedor creado con id: {}", guardado.getId());

        return guardado;
    }

    /**
     * Actualiza un proveedor existente
     */
    @Transactional
    public Proveedor actualizar(Long id, Proveedor proveedorAActualizar) {
        log.info("Actualizando proveedor con id: {}", id);

        Proveedor proveedor = obtenerPorId(id);

        if (!proveedor.getCif().equals(proveedorAActualizar.getCif())) {
            if (proveedorRepository.existsByCif(proveedorAActualizar.getCif())) {
                throw new RuntimeException("Ya existe un proveedor con el CIF: " + proveedorAActualizar.getCif());
            }
        }

        proveedor.setCif(proveedorAActualizar.getCif());
        proveedor.setRazonSocial(proveedorAActualizar.getRazonSocial());
        proveedor.setNombreComercial(proveedorAActualizar.getNombreComercial());
        proveedor.setDireccion(proveedorAActualizar.getDireccion());
        proveedor.setCiudad(proveedorAActualizar.getCiudad());
        proveedor.setProvincia(proveedorAActualizar.getProvincia());
        proveedor.setCodigoPostal(proveedorAActualizar.getCodigoPostal());
        proveedor.setTelefono(proveedorAActualizar.getTelefono());
        proveedor.setEmail(proveedorAActualizar.getEmail());
        proveedor.setPersonaContacto(proveedorAActualizar.getPersonaContacto());
        proveedor.setObservaciones(proveedorAActualizar.getObservaciones());

        Proveedor actualizado = proveedorRepository.save(proveedor);
        log.info("Proveedor actualizado");

        return actualizado;
    }

    /**
     * Desactiva un proveedor
     */
    @Transactional
    public void desactivar(Long id) {
        log.info("Desactivando proveedor con id: {}", id);

        Proveedor proveedor = obtenerPorId(id);
        proveedor.setActivo(false);

        proveedorRepository.save(proveedor);
        log.info("Proveedor desactivado");
    }

    /**
     * Reactiva un proveedor
     */
    @Transactional
    public void reactivar(Long id) {
        log.info("Reactivando proveedor con id: {}", id);

        Proveedor proveedor = obtenerPorId(id);
        proveedor.setActivo(true);

        proveedorRepository.save(proveedor);
        log.info("Proveedor reactivado");
    }
}