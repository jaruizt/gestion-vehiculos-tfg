package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.services.VehiculoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
@Slf4j
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<List<Vehiculo>> listarActivos() {
        log.info("Listando vehículos activos");
        List<Vehiculo> vehiculos = vehiculoService.listarActivos();
        return ResponseEntity.ok(vehiculos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo vehículo {}", id);
        Vehiculo vehiculo = vehiculoService.obtenerPorId(id);
        return ResponseEntity.ok(vehiculo);
    }

    @GetMapping("/matricula/{matricula}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<Vehiculo> obtenerPorMatricula(@PathVariable String matricula) {
        log.info("Obteniendo vehículo con matrícula {}", matricula);
        Vehiculo vehiculo = vehiculoService.obtenerPorMatricula(matricula);
        return ResponseEntity.ok(vehiculo);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        log.info("Creando vehículo {}", vehiculo.getMatricula());
        Vehiculo creado = vehiculoService.crear(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        log.info("Actualizando vehículo {}", id);
        Vehiculo actualizado = vehiculoService.actualizar(id, vehiculo);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/situacion")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Vehiculo> cambiarSituacion(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String nombreSituacion = body.get("situacion");
        log.info("Cambiando situación de vehículo {} a {}", id, nombreSituacion);

        Vehiculo actualizado = vehiculoService.cambiarSituacion(id, nombreSituacion);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/kilometros")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'OPERARIO')")
    public ResponseEntity<Vehiculo> actualizarKilometros(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {

        Integer kilometros = body.get("kilometros");
        log.info("Actualizando kilómetros de vehículo {} a {}", id, kilometros);

        Vehiculo actualizado = vehiculoService.actualizarKilometros(id, kilometros);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desactivar(@PathVariable Long id) {
        log.info("Desactivando vehículo {}", id);

        vehiculoService.desactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Vehículo desactivado exitosamente");

        return ResponseEntity.ok(response);
    }
}