package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.contrato.ContratoRentingMapper;
import com.uoc.tfg.gestionvehiculos.dtos.contrato.ContratoRentingRequest;
import com.uoc.tfg.gestionvehiculos.dtos.contrato.ContratoRentingResponse;
import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import com.uoc.tfg.gestionvehiculos.services.ContratoRentingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/contratos-renting")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Contratos de Renting", description = "Gestiona los contratos de renting")
@SecurityRequirement(name = "bearerAuth")
public class ContratoRentingController {

    private final ContratoRentingService contratoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRentingResponse>> listarActivos() {
        log.info("Listando contratos de renting activos");
        List<ContratoRenting> contratos = contratoService.listarActivos();
        List<ContratoRentingResponse> response = ContratoRentingMapper.toListResponse(contratos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ContratoRentingResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo contrato {}", id);
        ContratoRenting contrato = contratoService.obtenerPorId(id);
        ContratoRentingResponse response = ContratoRentingMapper.toResponse(contrato);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numeroContrato}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ContratoRentingResponse> obtenerPorNumero(@PathVariable String numeroContrato) {
        log.info("Obteniendo contrato {}", numeroContrato);
        ContratoRenting contrato = contratoService.obtenerPorNumero(numeroContrato);
        ContratoRentingResponse response = ContratoRentingMapper.toResponse(contrato);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRentingResponse>> obtenerPorCliente(@PathVariable Long clienteId) {
        log.info("Listando contratos del cliente {}", clienteId);
        List<ContratoRenting> contratos = contratoService.obtenerPorCliente(clienteId);
        List<ContratoRentingResponse> response = ContratoRentingMapper.toListResponse(contratos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRentingResponse>> obtenerPorVehiculo(@PathVariable Long vehiculoId) {
        log.info("Listando contratos del vehículo {}", vehiculoId);
        List<ContratoRenting> contratos = contratoService.obtenerPorVehiculo(vehiculoId);
        List<ContratoRentingResponse> response = ContratoRentingMapper.toListResponse(contratos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRentingResponse>> obtenerPorEstado(@PathVariable EstadoContrato estado) {
        log.info("Listando contratos con estado {}", estado);
        List<ContratoRenting> contratos = contratoService.obtenerPorEstado(estado);
        List<ContratoRentingResponse> response = ContratoRentingMapper.toListResponse(contratos);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener contratos próximos a vencer",
            description = "Lista contratos activos que vencen dentro de los próximos X días"
    )
    @GetMapping("/proximos-vencer")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ContratoRentingResponse>> obtenerProximosAVencer(@RequestParam(defaultValue = "30") int dias) {
        log.info("Listando contratos que vencen en {} días", dias);
        List<ContratoRenting> contratos = contratoService.obtenerProximosAVencer(dias);
        List<ContratoRentingResponse> response = ContratoRentingMapper.toListResponse(contratos);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear contrato de renting",
            description = "Crea un nuevo contrato y genera automáticamente todas las cuotas mensuales. El vehículo debe estar disponible"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contrato creado y cuotas generadas exitosamente"),
            @ApiResponse(responseCode = "422", description = "El vehículo no está disponible para renting")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ContratoRentingResponse> crear(@Valid @RequestBody ContratoRentingRequest request) {
        log.info("Creando contrato de renting {}", request.getNumeroContrato());

        ContratoRenting contrato = ContratoRentingMapper.toEntity(request);

        ContratoRenting creado = contratoService.crear(contrato, request.getClienteId(), request.getVehiculoId());
        ContratoRentingResponse response = ContratoRentingMapper.toResponse(creado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ContratoRentingResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ContratoRentingRequest request) {
        log.info("Actualizando contrato {}", id);

        ContratoRenting contrato = new ContratoRenting();
        ContratoRentingMapper.updateEntity(request, contrato);

        ContratoRenting actualizado = contratoService.actualizar(id, contrato, request.getClienteId(), request.getVehiculoId());
        ContratoRentingResponse response = ContratoRentingMapper.toResponse(actualizado);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Finalizar contrato",
            description = "Marca el contrato como finalizado y cambia el vehículo a estado DISPONIBLE"
    )
    @PatchMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Map<String, String>> finalizar(@PathVariable Long id) {
        log.info("Finalizando contrato {}", id);

        contratoService.finalizar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Contrato finalizado exitosamente");

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cancelar contrato",
            description = "Cancela el contrato por un motivo específico y libera el vehículo"
    )
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