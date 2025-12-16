package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.cuota.CuotaRentingMapper;
import com.uoc.tfg.gestionvehiculos.dtos.cuota.CuotaRentingResponse;
import com.uoc.tfg.gestionvehiculos.entities.CuotaRenting;
import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
import com.uoc.tfg.gestionvehiculos.services.CuotaRentingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cuotas de Renting", description = "Gestión de cuotas de renting")
@SecurityRequirement(name = "bearerAuth")
public class CuotaRentingController {

    private final CuotaRentingService cuotaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRentingResponse>> listarActivas() {
        log.info("Listando cuotas activas");
        List<CuotaRenting> cuotas = cuotaService.listarActivas();
        List<CuotaRentingResponse> response = CuotaRentingMapper.toListResponse(cuotas);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<CuotaRentingResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo cuota {}", id);
        CuotaRenting cuota = cuotaService.obtenerPorId(id);
        CuotaRentingResponse response = CuotaRentingMapper.toResponse(cuota);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contrato/{contratoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRentingResponse>> obtenerPorContrato(@PathVariable Long contratoId) {
        log.info("Listando cuotas del contrato {}", contratoId);
        List<CuotaRenting> cuotas = cuotaService.obtenerPorContrato(contratoId);
        List<CuotaRentingResponse> response = CuotaRentingMapper.toListResponse(cuotas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRentingResponse>> obtenerPorEstado(@PathVariable EstadoCuota estado) {
        log.info("Listando cuotas con estado {}", estado);
        List<CuotaRenting> cuotas = cuotaService.obtenerPorEstado(estado);
        List<CuotaRentingResponse> response = CuotaRentingMapper.toListResponse(cuotas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRentingResponse>> obtenerPendientes() {
        log.info("Listando cuotas pendientes");
        List<CuotaRenting> cuotas = cuotaService.obtenerPendientes();
        List<CuotaRentingResponse> response = CuotaRentingMapper.toListResponse(cuotas);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener cuotas vencidas",
            description = "Lista todas las cuotas vencids"
    )
    @GetMapping("/vencidas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRentingResponse>> obtenerVencidas() {
        log.info("Listando cuotas vencidas");
        List<CuotaRenting> cuotas = cuotaService.obtenerVencidas();
        List<CuotaRentingResponse> response = CuotaRentingMapper.toListResponse(cuotas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proximas-vencer")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<CuotaRentingResponse>> obtenerProximasAVencer(@RequestParam(defaultValue = "7") int dias) {
        log.info("Listando cuotas que vencen en {} días", dias);
        List<CuotaRenting> cuotas = cuotaService.obtenerProximasAVencer(dias);
        List<CuotaRentingResponse> response = CuotaRentingMapper.toListResponse(cuotas);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<CuotaRentingResponse> marcarComoPagada(@PathVariable Long id) {
        log.info("Marcando cuota {} como pagada", id);
        CuotaRenting cuota = cuotaService.marcarComoPagada(id);
        CuotaRentingResponse response = CuotaRentingMapper.toResponse(cuota);
        return ResponseEntity.ok(response);
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