package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.reserva.ReservaVentaMapper;
import com.uoc.tfg.gestionvehiculos.dtos.reserva.ReservaVentaRequest;
import com.uoc.tfg.gestionvehiculos.dtos.reserva.ReservaVentaResponse;
import com.uoc.tfg.gestionvehiculos.entities.ReservaVenta;
import com.uoc.tfg.gestionvehiculos.enums.EstadoReserva;
import com.uoc.tfg.gestionvehiculos.services.ReservaVentaService;
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
@RequestMapping("/api/reservas-venta")
@RequiredArgsConstructor
@Slf4j
public class ReservaVentaController {

    private final ReservaVentaService reservaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ReservaVentaResponse>> listarActivas() {
        log.info("Listando reservas activas");
        List<ReservaVenta> reservas = reservaService.listarActivas();
        List<ReservaVentaResponse> response = ReservaVentaMapper.toListResponse(reservas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ReservaVentaResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo reserva {}", id);
        ReservaVenta reserva = reservaService.obtenerPorId(id);
        ReservaVentaResponse response = ReservaVentaMapper.toResponse(reserva);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ReservaVentaResponse>> obtenerPorCliente(@PathVariable Long clienteId) {
        log.info("Listando reservas del cliente {}", clienteId);
        List<ReservaVenta> reservas = reservaService.obtenerPorCliente(clienteId);
        List<ReservaVentaResponse> response = ReservaVentaMapper.toListResponse(reservas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ReservaVentaResponse>> obtenerPorVehiculo(@PathVariable Long vehiculoId) {
        log.info("Listando reservas del vehículo {}", vehiculoId);
        List<ReservaVenta> reservas = reservaService.obtenerPorVehiculo(vehiculoId);
        List<ReservaVentaResponse> response = ReservaVentaMapper.toListResponse(reservas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ReservaVentaResponse>> obtenerPorEstado(@PathVariable EstadoReserva estado) {
        log.info("Listando reservas con estado {}", estado);
        List<ReservaVenta> reservas = reservaService.obtenerPorEstado(estado);
        List<ReservaVentaResponse> response = ReservaVentaMapper.toListResponse(reservas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expiradas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ReservaVentaResponse>> obtenerExpiradas() {
        log.info("Listando reservas expiradas");
        List<ReservaVenta> reservas = reservaService.obtenerExpiradas();
        List<ReservaVentaResponse> response = ReservaVentaMapper.toListResponse(reservas);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ReservaVentaResponse> crear(@Valid @RequestBody ReservaVentaRequest request) {
        log.info("Creando reserva de venta");
        ReservaVenta reserva = ReservaVentaMapper.toEntity(request);
        ReservaVenta creada = reservaService.crear(reserva, request.getClienteId(), request.getVehiculoId());
        ReservaVentaResponse response = ReservaVentaMapper.toResponse(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ReservaVentaResponse> confirmar(@PathVariable Long id) {
        log.info("Confirmando reserva {}", id);
        ReservaVenta confirmada = reservaService.confirmar(id);
        ReservaVentaResponse response = ReservaVentaMapper.toResponse(confirmada);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Map<String, String>> cancelar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String motivo = body.get("motivo");
        log.info("Cancelando reserva {} - Motivo: {}", id, motivo);

        reservaService.cancelar(id, motivo);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reserva cancelada");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/actualizar-expiradas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> actualizarReservasExpiradas() {
        log.info("Actualizando reservas expiradas");

        reservaService.actualizarReservasExpiradas();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reservas expiradas actualizadas");

        return ResponseEntity.ok(response);
    }
}