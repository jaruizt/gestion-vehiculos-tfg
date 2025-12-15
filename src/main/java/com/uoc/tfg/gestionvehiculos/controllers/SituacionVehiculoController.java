package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import com.uoc.tfg.gestionvehiculos.services.SituacionVehiculoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@RestController
@RequestMapping("/api/situaciones-vehiculo")
@RequiredArgsConstructor
@Slf4j
public class SituacionVehiculoController {

    private final SituacionVehiculoService situacionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<List<SituacionVehiculo>> listarActivas() {
        log.info("Listando situaciones de vehículo activas");
        List<SituacionVehiculo> situaciones = situacionService.listarActivas();
        return ResponseEntity.ok(situaciones);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SituacionVehiculo>> listarTodas() {
        log.info("Listando todas las situaciones");
        List<SituacionVehiculo> situaciones = situacionService.listarTodas();
        return ResponseEntity.ok(situaciones);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<SituacionVehiculo> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo situación {}", id);
        SituacionVehiculo situacion = situacionService.obtenerPorId(id);
        return ResponseEntity.ok(situacion);
    }

    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<SituacionVehiculo> obtenerPorNombre(@PathVariable String nombre) {
        log.info("Obteniendo situación {}", nombre);
        SituacionVehiculo situacion = situacionService.obtenerPorNombre(nombre);
        return ResponseEntity.ok(situacion);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SituacionVehiculo> crear(@RequestBody SituacionVehiculo situacion) {
        log.info("Creando situación {}", situacion.getNombre());
        SituacionVehiculo creada = situacionService.crear(situacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SituacionVehiculo> actualizar(@PathVariable Long id, @RequestBody SituacionVehiculo situacion) {
        log.info("Actualizando situación {}", id);
        SituacionVehiculo actualizada = situacionService.actualizar(id, situacion);
        return ResponseEntity.ok(actualizada);
    }
}