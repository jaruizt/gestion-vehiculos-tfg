package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.FacturaVenta;
import com.uoc.tfg.gestionvehiculos.services.FacturaVentaService;
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
    public ResponseEntity<List<FacturaVenta>> listarActivas() {
        log.info("Listando facturas de venta activas");
        List<FacturaVenta> facturas = facturaVentaService.listarActivas();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<FacturaVenta> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo factura de venta {}", id);
        FacturaVenta factura = facturaVentaService.obtenerPorId(id);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/numero/{numeroFactura}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<FacturaVenta> obtenerPorNumero(@PathVariable String numeroFactura) {
        log.info("Obteniendo factura de venta {}", numeroFactura);
        FacturaVenta factura = facturaVentaService.obtenerPorNumero(numeroFactura);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<FacturaVenta>> obtenerPorCliente(@PathVariable Long clienteId) {
        log.info("Listando facturas del cliente {}", clienteId);
        List<FacturaVenta> facturas = facturaVentaService.obtenerPorCliente(clienteId);
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaVenta>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Listando facturas entre {} y {}", inicio, fin);
        List<FacturaVenta> facturas = facturaVentaService.obtenerPorFechas(inicio, fin);
        return ResponseEntity.ok(facturas);
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
    public ResponseEntity<FacturaVenta> crear(@RequestBody FacturaVenta factura) {
        log.info("Creando factura de venta {}", factura.getNumeroFactura());
        FacturaVenta creada = facturaVentaService.crear(factura);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaVenta> actualizar(@PathVariable Long id, @RequestBody FacturaVenta factura) {
        log.info("Actualizando factura de venta {}", id);
        FacturaVenta actualizada = facturaVentaService.actualizar(id, factura);
        return ResponseEntity.ok(actualizada);
    }
}