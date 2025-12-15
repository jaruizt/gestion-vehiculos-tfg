package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import com.uoc.tfg.gestionvehiculos.exceptions.DuplicateResourceException;
import com.uoc.tfg.gestionvehiculos.repositories.SituacionVehiculoRepository;
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
public class SituacionVehiculoService {

    private final SituacionVehiculoRepository situacionRepository;

    public List<SituacionVehiculo> listarTodas() {
        log.debug("Listando todas las situaciones de vehículo");
        return situacionRepository.findAll();
    }

    public List<SituacionVehiculo> listarActivas() {
        log.debug("Listando situaciones activas");
        return situacionRepository.findAll().stream()
                .filter(s -> Boolean.TRUE.equals(s.getActivo()))
                .toList();
    }

    public SituacionVehiculo obtenerPorId(Long id) {
        log.debug("Buscando situación con id: {}", id);
        return situacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Situación no encontrada con id: " + id));
    }

    public SituacionVehiculo obtenerPorNombre(String nombre) {
        log.debug("Buscando situación con nombre: {}", nombre);
        return situacionRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Situación no encontrada: " + nombre));
    }

    @Transactional
    public SituacionVehiculo crear(SituacionVehiculo situacion) {
        log.info("Creando situación: {}", situacion.getNombre());

        if (situacionRepository.findByNombre(situacion.getNombre()).isPresent()) {
            throw new DuplicateResourceException("situación", "nombre", situacion.getNombre());
        }

        SituacionVehiculo guardada = situacionRepository.save(situacion);
        log.info("Situación creada con id: {}", guardada.getId());

        return guardada;
    }

    @Transactional
    public SituacionVehiculo actualizar(Long id, SituacionVehiculo situacionActualizada) {
        log.info("Actualizando situación con id: {}", id);

        SituacionVehiculo situacionExistente = obtenerPorId(id);

        situacionExistente.setNombre(situacionActualizada.getNombre());
        situacionExistente.setDescripcion(situacionActualizada.getDescripcion());
        situacionExistente.setOrden(situacionActualizada.getOrden());

        SituacionVehiculo actualizada = situacionRepository.save(situacionExistente);
        log.info("Situación actualizada exitosamente");

        return actualizada;
    }
}