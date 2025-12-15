package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.factura.FacturaVentaMapper;
import com.uoc.tfg.gestionvehiculos.dtos.factura.FacturaVentaRequest;
import com.uoc.tfg.gestionvehiculos.dtos.factura.FacturaVentaResponse;
import com.uoc.tfg.gestionvehiculos.entities.FacturaVenta;
import com.uoc.tfg.gestionvehiculos.services.FacturaVentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@RestController
@RequestMapping("/api/facturas-venta")
@RequiredArgsConstructor
@Slf4j
public class FacturaVentaController {

    private final FacturaVentaService facturaVentaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<FacturaVentaResponse>> listarActivas() {
        log.info("Listando facturas de venta activas");
        List<FacturaVenta> facturas = facturaVentaService.listarActivas();
        List<FacturaVentaResponse> response = FacturaVentaMapper.toListResponse(facturas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<FacturaVentaResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo factura de venta {}", id);
        FacturaVenta factura = facturaVentaService.obtenerPorId(id);
        FacturaVentaResponse response = FacturaVentaMapper.toResponse(factura);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numeroFactura}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<FacturaVentaResponse> obtenerPorNumero(@PathVariable String numeroFactura) {
        log.info("Obteniendo factura de venta {}", numeroFactura);
        FacturaVenta factura = facturaVentaService.obtenerPorNumero(numeroFactura);
        FacturaVentaResponse response = FacturaVentaMapper.toResponse(factura);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<FacturaVentaResponse>> obtenerPorCliente(@PathVariable Long clienteId) {
        log.info("Listando facturas del cliente {}", clienteId);
        List<FacturaVenta> facturas = facturaVentaService.obtenerPorCliente(clienteId);
        List<FacturaVentaResponse> response = FacturaVentaMapper.toListResponse(facturas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaVentaResponse>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Listando facturas entre {} y {}", inicio, fin);
        List<FacturaVenta> facturas = facturaVentaService.obtenerPorFechas(inicio, fin);
        List<FacturaVentaResponse> response = FacturaVentaMapper.toListResponse(facturas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/beneficio")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Map<String, BigDecimal>> calcularBeneficio(@PathVariable Long id) {
        log.info("Calculando beneficio de factura {}", id);

        BigDecimal beneficio = facturaVentaService.calcularBeneficio(id);

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("beneficio", beneficio);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/beneficio-total")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Map<String, BigDecimal>> calcularBeneficioTotal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Calculando beneficio total entre {} y {}", inicio, fin);

        BigDecimal beneficioTotal = facturaVentaService.calcularBeneficioTotal(inicio, fin);

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("beneficioTotal", beneficioTotal);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<FacturaVentaResponse> crear(@Valid @RequestBody FacturaVentaRequest request) {
        log.info("Creando factura de venta {}", request.getNumeroFactura());
        FacturaVenta facturaVenta = FacturaVentaMapper.toEntity(request);
        FacturaVenta creada = facturaVentaService.crear(facturaVenta, request.getClienteId(), request.getVehiculoId());
        FacturaVentaResponse response = FacturaVentaMapper.toResponse(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaVentaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody FacturaVentaRequest request) {
        log.info("Actualizando factura de venta {}", id);
        FacturaVenta facturaVenta = FacturaVentaMapper.toEntity(request);
        FacturaVenta actualizada = facturaVentaService.actualizar(id, facturaVenta, request.getClienteId());
        FacturaVentaResponse response = FacturaVentaMapper.toResponse(actualizada);
        return ResponseEntity.ok(response);
    }
}