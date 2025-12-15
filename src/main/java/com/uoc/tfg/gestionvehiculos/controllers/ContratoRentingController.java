package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;
import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import com.uoc.tfg.gestionvehiculos.services.ContratoRentingService;
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
@RequestMapping("/api/contratos-renting")
@RequiredArgsConstructor
@Slf4j
public class ContratoRentingController {

    private final ContratoRentingService contratoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRenting>> listarActivos() {
        log.info("Listando contratos de renting activos");
        List<ContratoRenting> contratos = contratoService.listarActivos();
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ContratoRenting> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo contrato {}", id);
        ContratoRenting contrato = contratoService.obtenerPorId(id);
        return ResponseEntity.ok(contrato);
    }

    @GetMapping("/numero/{numeroContrato}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ContratoRenting> obtenerPorNumero(@PathVariable String numeroContrato) {
        log.info("Obteniendo contrato {}", numeroContrato);
        ContratoRenting contrato = contratoService.obtenerPorNumero(numeroContrato);
        return ResponseEntity.ok(contrato);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRenting>> obtenerPorCliente(@PathVariable Long clienteId) {
        log.info("Listando contratos del cliente {}", clienteId);
        List<ContratoRenting> contratos = contratoService.obtenerPorCliente(clienteId);
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRenting>> obtenerPorVehiculo(@PathVariable Long vehiculoId) {
        log.info("Listando contratos del vehículo {}", vehiculoId);
        List<ContratoRenting> contratos = contratoService.obtenerPorVehiculo(vehiculoId);
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRenting>> obtenerPorEstado(@PathVariable EstadoContrato estado) {
        log.info("Listando contratos con estado {}", estado);
        List<ContratoRenting> contratos = contratoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/proximos-vencer")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRenting>> obtenerProximosAVencer(@RequestParam(defaultValue = "30") int dias) {
        log.info("Listando contratos que vencen en {} días", dias);
        List<ContratoRenting> contratos = contratoService.obtenerProximosAVencer(dias);
        return ResponseEntity.ok(contratos);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ContratoRenting> crear(@RequestBody ContratoRenting contrato) {
        log.info("Creando contrato de renting {}", contrato.getNumeroContrato());
        ContratoRenting creado = contratoService.crear(contrato);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ContratoRenting> actualizar(@PathVariable Long id, @RequestBody ContratoRenting contrato) {
        log.info("Actualizando contrato {}", id);
        ContratoRenting actualizado = contratoService.actualizar(id, contrato);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Map<String, String>> finalizar(@PathVariable Long id) {
        log.info("Finalizando contrato {}", id);

        contratoService.finalizar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Contrato finalizado exitosamente");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> cancelar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String motivo = body.get("motivo");
        log.info("Cancelando contrato {} - Motivo: {}", id, motivo);

        contratoService.cancelar(id, motivo);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Contrato cancelado exitosamente");

        return ResponseEntity.ok(response);
    }
}