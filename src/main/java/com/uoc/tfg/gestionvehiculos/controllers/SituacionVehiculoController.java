package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.vehiculo.SituacionVehiculoMapper;
import com.uoc.tfg.gestionvehiculos.dtos.vehiculo.SituacionVehiculoRequest;
import com.uoc.tfg.gestionvehiculos.dtos.vehiculo.SituacionVehiculoResponse;
import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import com.uoc.tfg.gestionvehiculos.services.SituacionVehiculoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Situaciones de Vehículo", description = "Catálogo de situaciones")
@SecurityRequirement(name = "bearerAuth")
public class SituacionVehiculoController {

    private final SituacionVehiculoService situacionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<List<SituacionVehiculoResponse>> listarActivas() {
        log.info("Listando situaciones de vehículo activas");
        List<SituacionVehiculo> situaciones = situacionService.listarActivas();
        List<SituacionVehiculoResponse> responses = SituacionVehiculoMapper.toListResponse(situaciones);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SituacionVehiculoResponse>> listarTodas() {
        log.info("Listando todas las situaciones");
        List<SituacionVehiculo> situaciones = situacionService.listarTodas();
        List<SituacionVehiculoResponse> responses = SituacionVehiculoMapper.toListResponse(situaciones);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<SituacionVehiculoResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo situación {}", id);
        SituacionVehiculo situacion = situacionService.obtenerPorId(id);
        SituacionVehiculoResponse response = SituacionVehiculoMapper.toResponse(situacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL', 'OPERARIO')")
    public ResponseEntity<SituacionVehiculoResponse> obtenerPorNombre(@PathVariable String nombre) {
        log.info("Obteniendo situación {}", nombre);
        SituacionVehiculo situacion = situacionService.obtenerPorNombre(nombre);
        SituacionVehiculoResponse response = SituacionVehiculoMapper.toResponse(situacion);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SituacionVehiculoResponse> crear(@RequestBody SituacionVehiculoRequest request) {
        log.info("Creando situación {}", request.getNombre());
        SituacionVehiculo situacion = SituacionVehiculoMapper.toEntity(request);
        SituacionVehiculo creada = situacionService.crear(situacion);
        SituacionVehiculoResponse response = SituacionVehiculoMapper.toResponse(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SituacionVehiculoResponse> actualizar(@PathVariable Long id, @RequestBody SituacionVehiculoRequest request) {
        log.info("Actualizando situación {}", id);
        SituacionVehiculo situacion = SituacionVehiculoMapper.toEntity(request);
        SituacionVehiculo actualizada = situacionService.actualizar(id, situacion);
        SituacionVehiculoResponse response = SituacionVehiculoMapper.toResponse(actualizada);
        return ResponseEntity.ok(response);
    }
}