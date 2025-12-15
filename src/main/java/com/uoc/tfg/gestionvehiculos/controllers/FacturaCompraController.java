package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.FacturaCompra;
import com.uoc.tfg.gestionvehiculos.services.FacturaCompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@RestController
@RequestMapping("/api/facturas-compra")
@RequiredArgsConstructor
@Slf4j
public class FacturaCompraController {

    private final FacturaCompraService facturaCompraService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaCompra>> listarActivas() {
        log.info("Listando facturas de compra activas");
        List<FacturaCompra> facturas = facturaCompraService.listarActivas();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompra> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo factura de compra {}", id);
        FacturaCompra factura = facturaCompraService.obtenerPorId(id);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/numero/{numeroFactura}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompra> obtenerPorNumero(@PathVariable String numeroFactura) {
        log.info("Obteniendo factura de compra {}", numeroFactura);
        FacturaCompra factura = facturaCompraService.obtenerPorNumero(numeroFactura);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/proveedor/{proveedorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaCompra>> obtenerPorProveedor(@PathVariable Long proveedorId) {
        log.info("Listando facturas del proveedor {}", proveedorId);
        List<FacturaCompra> facturas = facturaCompraService.obtenerPorProveedor(proveedorId);
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaCompra>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Listando facturas entre {} y {}", inicio, fin);
        List<FacturaCompra> facturas = facturaCompraService.obtenerPorFechas(inicio, fin);
        return ResponseEntity.ok(facturas);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompra> crear(@RequestBody FacturaCompra factura) {
        log.info("Creando factura de compra {}", factura.getNumeroFactura());
        FacturaCompra creada = facturaCompraService.crear(factura);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompra> actualizar(@PathVariable Long id, @RequestBody FacturaCompra factura) {
        log.info("Actualizando factura de compra {}", id);
        FacturaCompra actualizada = facturaCompraService.actualizar(id, factura);
        return ResponseEntity.ok(actualizada);
    }
}