package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.CuotaRenting;
import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
import com.uoc.tfg.gestionvehiculos.services.CuotaRentingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/cuotas-renting")
@RequiredArgsConstructor
@Slf4j
public class CuotaRentingController {

    private final CuotaRentingService cuotaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRenting>> listarActivas() {
        log.info("Listando cuotas activas");
        List<CuotaRenting> cuotas = cuotaService.listarActivas();
        return ResponseEntity.ok(cuotas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<CuotaRenting> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo cuota {}", id);
        CuotaRenting cuota = cuotaService.obtenerPorId(id);
        return ResponseEntity.ok(cuota);
    }

    @GetMapping("/contrato/{contratoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRenting>> obtenerPorContrato(@PathVariable Long contratoId) {
        log.info("Listando cuotas del contrato {}", contratoId);
        List<CuotaRenting> cuotas = cuotaService.obtenerPorContrato(contratoId);
        return ResponseEntity.ok(cuotas);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRenting>> obtenerPorEstado(@PathVariable EstadoCuota estado) {
        log.info("Listando cuotas con estado {}", estado);
        List<CuotaRenting> cuotas = cuotaService.obtenerPorEstado(estado);
        return ResponseEntity.ok(cuotas);
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRenting>> obtenerPendientes() {
        log.info("Listando cuotas pendientes");
        List<CuotaRenting> cuotas = cuotaService.obtenerPendientes();
        return ResponseEntity.ok(cuotas);
    }

    @GetMapping("/vencidas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRenting>> obtenerVencidas() {
        log.info("Listando cuotas vencidas");
        List<CuotaRenting> cuotas = cuotaService.obtenerVencidas();
        return ResponseEntity.ok(cuotas);
    }

    @GetMapping("/proximas-vencer")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRenting>> obtenerProximasAVencer(@RequestParam(defaultValue = "7") int dias) {
        log.info("Listando cuotas que vencen en {} días", dias);
        List<CuotaRenting> cuotas = cuotaService.obtenerProximasAVencer(dias);
        return ResponseEntity.ok(cuotas);
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<CuotaRenting> marcarComoPagada(@PathVariable Long id) {
        log.info("Marcando cuota {} como pagada", id);
        CuotaRenting cuota = cuotaService.marcarComoPagada(id);
        return ResponseEntity.ok(cuota);
    }

    @PatchMapping("/actualizar-vencidas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> actualizarCuotasVencidas() {
        log.info("Actualizando estado de cuotas vencidas");

        cuotaService.actualizarCuotasVencidas();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cuotas vencidas actualizadas");

        return ResponseEntity.ok(response);
    }
}