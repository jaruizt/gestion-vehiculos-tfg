package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.exceptions.BusinessRuleException;
import com.uoc.tfg.gestionvehiculos.exceptions.DuplicateResourceException;
import com.uoc.tfg.gestionvehiculos.exceptions.ResourceNotFoundException;
import com.uoc.tfg.gestionvehiculos.repositories.SituacionVehiculoRepository;
import com.uoc.tfg.gestionvehiculos.repositories.VehiculoRepository;
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
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final SituacionVehiculoService situacionVehiculoService;

    /**
     * Lista todos los vehículos activos
     */
    public List<Vehiculo> listarActivos() {
        log.debug("Listando todos los vehículos activos");
        return vehiculoRepository.findAll().stream()
                .filter(v -> Boolean.TRUE.equals(v.getActivo()))
                .toList();
    }

    /**
     * Obtiene un vehículo por ID
     */
    public Vehiculo obtenerPorId(Long id) {
        log.debug("Buscando vehículo con id: {}", id);
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
    }

    /**
     * Obtiene un vehículo por matrícula
     */
    public Vehiculo obtenerPorMatricula(String matricula) {
        log.debug("Buscando vehículo con matrícula: {}", matricula);
        return vehiculoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con matrícula: " + matricula));
    }

    /**
     * Crea un nuevo vehículo
     */
    @Transactional
    public Vehiculo crear(Vehiculo vehiculo, Long situacionId) {
        log.info("Creando nuevo vehículo: {}", vehiculo.getMatricula());

        if (vehiculoRepository.findByMatricula(vehiculo.getMatricula()).isPresent()) {
            throw new DuplicateResourceException("Vehiculo", "matricula", vehiculo.getMatricula());
        }

        if (situacionId != null) {
            SituacionVehiculo situacion = situacionVehiculoService.obtenerPorId(situacionId);
            vehiculo.setSituacion(situacion);
        } else {
            SituacionVehiculo disponible = situacionVehiculoService.obtenerPorNombre("DISPONIBLE");
            vehiculo.setSituacion(disponible);
        }

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo creado exitosamente con id: {}", guardado.getId());

        return guardado;
    }

    /**
     * Actualiza un vehículo existente
     */
    @Transactional
    public Vehiculo actualizar(Long id, Vehiculo vehiculoActualizado, Long situacionId) {
        log.info("Actualizando vehículo con id: {}", id);

        Vehiculo vehiculoExistente = obtenerPorId(id);

        if (!vehiculoExistente.getMatricula().equals(vehiculoActualizado.getMatricula())) {
            if (vehiculoRepository.findByMatricula(vehiculoActualizado.getMatricula()).isPresent()) {
                throw new DuplicateResourceException("Vehiculo", "matricula", vehiculoActualizado.getMatricula());
            }
        }

        vehiculoExistente.setMatricula(vehiculoActualizado.getMatricula());
        vehiculoExistente.setMarca(vehiculoActualizado.getMarca());
        vehiculoExistente.setModelo(vehiculoActualizado.getModelo());
        vehiculoExistente.setAnyoFabricacion(vehiculoActualizado.getAnyoFabricacion());
        vehiculoExistente.setColor(vehiculoActualizado.getColor());
        vehiculoExistente.setKilometros(vehiculoActualizado.getKilometros());
        vehiculoExistente.setNumeroBastidor(vehiculoActualizado.getNumeroBastidor());
        vehiculoExistente.setTipoCombustible(vehiculoActualizado.getTipoCombustible());

        if (situacionId != null) {
            SituacionVehiculo situacion = situacionVehiculoService.obtenerPorId(situacionId);
            vehiculoExistente.setSituacion(situacion);
        }

        Vehiculo actualizado = vehiculoRepository.save(vehiculoExistente);
        log.info("Vehículo actualizado exitosamente");

        return actualizado;
    }

    /**
     * Cambia la situación de un vehículo
     */
    @Transactional
    public Vehiculo cambiarSituacion(Long id, String nombreSituacion) {
        log.info("Cambiando situación de vehículo id: {} a: {}", id, nombreSituacion);

        Vehiculo vehiculo = obtenerPorId(id);
        SituacionVehiculo nuevaSituacion = situacionVehiculoService.obtenerPorNombre(nombreSituacion);
        vehiculo.setSituacion(nuevaSituacion);

        Vehiculo actualizado = vehiculoRepository.save(vehiculo);
        log.info("Situación cambiada exitosamente");

        return actualizado;
    }

    /**
     * Actualiza los kilómetros de un vehículo
     */
    @Transactional
    public Vehiculo actualizarKilometros(Long id, Integer kilometros) {
        log.info("Actualizando kilómetros de vehículo id: {} a: {}", id, kilometros);

        Vehiculo vehiculo = obtenerPorId(id);

        if (kilometros < vehiculo.getKilometros()) {
            throw new BusinessRuleException("Los kilómetros no pueden ser menores a los actuales");
        }

        vehiculo.setKilometros(kilometros);

        Vehiculo actualizado = vehiculoRepository.save(vehiculo);
        log.info("Kilómetros actualizados exitosamente");

        return actualizado;
    }

    /**
     * Desactiva un vehículo
     */
    @Transactional
    public void desactivar(Long id) {
        log.info("Desactivando vehículo con id: {}", id);

        Vehiculo vehiculo = obtenerPorId(id);

        if (vehiculo.estaEnRenting()) {
            throw new BusinessRuleException("No se puede desactivar un vehículo que está en renting");
        }

        vehiculo.setActivo(false);
        vehiculoRepository.save(vehiculo);

        log.info("Vehículo desactivado exitosamente");
    }
}