package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.vehiculo.VehiculoMapper;
import com.uoc.tfg.gestionvehiculos.dtos.vehiculo.VehiculoRequest;
import com.uoc.tfg.gestionvehiculos.dtos.vehiculo.VehiculoResponse;
import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.exceptions.ResourceNotFoundException;
import com.uoc.tfg.gestionvehiculos.repositories.SituacionVehiculoRepository;
import com.uoc.tfg.gestionvehiculos.services.SituacionVehiculoService;
import com.uoc.tfg.gestionvehiculos.services.VehiculoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<VehiculoResponse>> listarActivos() {
        log.info("Listando vehículos activos");
        List<Vehiculo> vehiculos = vehiculoService.listarActivos();
        List<VehiculoResponse> response = vehiculos.stream()
                .map(VehiculoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<VehiculoResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo vehículo {}", id);
        Vehiculo vehiculo = vehiculoService.obtenerPorId(id);
        VehiculoResponse response = VehiculoMapper.toResponse(vehiculo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/matricula/{matricula}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<VehiculoResponse> obtenerPorMatricula(@PathVariable String matricula) {
        log.info("Obteniendo vehículo con matrícula {}", matricula);
        Vehiculo vehiculo = vehiculoService.obtenerPorMatricula(matricula);
        VehiculoResponse response = VehiculoMapper.toResponse(vehiculo);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest request) {
        log.info("Creando vehículo {}", request.getMatricula());

        Vehiculo vehiculo = VehiculoMapper.toEntity(request);
        Vehiculo creado = vehiculoService.crear(vehiculo, request.getSituacionId());
        VehiculoResponse response = VehiculoMapper.toResponse(creado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<VehiculoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoRequest request) {
        log.info("Actualizando vehículo {}", id);

        Vehiculo vehiculoActualizado = new Vehiculo();
        VehiculoMapper.updateEntity(request, vehiculoActualizado);
        Vehiculo actualizado = vehiculoService.actualizar(id, vehiculoActualizado, request.getSituacionId());
        VehiculoResponse response = VehiculoMapper.toResponse(actualizado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/situacion")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<VehiculoResponse> cambiarSituacion(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String nombreSituacion = body.get("situacion");
        log.info("Cambiando situación de vehículo {} a {}", id, nombreSituacion);

        Vehiculo actualizado = vehiculoService.cambiarSituacion(id, nombreSituacion);
        VehiculoResponse response = VehiculoMapper.toResponse(actualizado);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/kilometros")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'OPERARIO')")
    public ResponseEntity<VehiculoResponse> actualizarKilometros(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {

        Integer kilometros = body.get("kilometros");
        log.info("Actualizando kilómetros de vehículo {} a {}", id, kilometros);

        Vehiculo actualizado = vehiculoService.actualizarKilometros(id, kilometros);
        VehiculoResponse response = VehiculoMapper.toResponse(actualizado);
        return ResponseEntity.ok(response);
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