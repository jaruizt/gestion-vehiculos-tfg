package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.factura.FacturaCompraMapper;
import com.uoc.tfg.gestionvehiculos.dtos.factura.FacturaCompraRequest;
import com.uoc.tfg.gestionvehiculos.dtos.factura.FacturaCompraResponse;
import com.uoc.tfg.gestionvehiculos.entities.FacturaCompra;
import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.services.FacturaCompraService;
import com.uoc.tfg.gestionvehiculos.services.ProveedorService;
import com.uoc.tfg.gestionvehiculos.services.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Facturas de Compra", description = "Gestión de facturas de compra")
@SecurityRequirement(name = "bearerAuth")
public class FacturaCompraController {

    private final FacturaCompraService facturaCompraService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaCompraResponse>> listarActivas() {
        log.info("Listando facturas de compra activas");
        List<FacturaCompra> facturas = facturaCompraService.listarActivas();
        List<FacturaCompraResponse> response = FacturaCompraMapper.toListResponse(facturas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompraResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo factura de compra {}", id);
        FacturaCompra factura = facturaCompraService.obtenerPorId(id);
        FacturaCompraResponse response = FacturaCompraMapper.toResponse(factura);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numeroFactura}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompraResponse> obtenerPorNumero(@PathVariable String numeroFactura) {
        log.info("Obteniendo factura de compra {}", numeroFactura);
        FacturaCompra factura = facturaCompraService.obtenerPorNumero(numeroFactura);
        FacturaCompraResponse response = FacturaCompraMapper.toResponse(factura);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proveedor/{proveedorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaCompraResponse>> obtenerPorProveedor(@PathVariable Long proveedorId) {
        log.info("Listando facturas del proveedor {}", proveedorId);
        List<FacturaCompra> facturas = facturaCompraService.obtenerPorProveedor(proveedorId);
        List<FacturaCompraResponse> response = FacturaCompraMapper.toListResponse(facturas);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener facturas por rango de fechas",
            description = "Lista todas las facturas de compra entre dos fechas específicas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facturas obtenidas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Formato de fecha inválido (usar ISO: yyyy-MM-dd)")
    })
    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<FacturaCompraResponse>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Listando facturas entre {} y {}", inicio, fin);
        List<FacturaCompra> facturas = facturaCompraService.obtenerPorFechas(inicio, fin);
        List<FacturaCompraResponse> response = FacturaCompraMapper.toListResponse(facturas);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear factura de compra",
            description = "Registra la compra de un vehículo. El vehículo no puede tener otra factura de compra previa"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada exitosamente"),
            @ApiResponse(responseCode = "422", description = "El vehículo ya tiene una factura de compra asociada")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompraResponse> crear(@Valid @RequestBody FacturaCompraRequest request) {
        log.info("Creando factura de compra {}", request.getNumeroFactura());

        FacturaCompra factura = FacturaCompraMapper.toEntity(request);

        FacturaCompra creada = facturaCompraService.crear(factura,request.getProveedorId(), request.getVehiculoId());
        FacturaCompraResponse response = FacturaCompraMapper.toResponse(creada);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<FacturaCompraResponse> actualizar(@PathVariable Long id, @Valid @RequestBody FacturaCompraRequest request) {
        log.info("Actualizando factura de compra {}", id);

        FacturaCompra factura = facturaCompraService.obtenerPorId(id);
        FacturaCompraMapper.updateEntity(request, factura);

        FacturaCompra actualizada = facturaCompraService.actualizar(id, factura, request.getProveedorId(), request.getVehiculoId());
        FacturaCompraResponse response = FacturaCompraMapper.toResponse(actualizada);

        return ResponseEntity.ok(response);
    }
}