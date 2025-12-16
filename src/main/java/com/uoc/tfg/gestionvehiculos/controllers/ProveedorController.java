package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.proveedor.ProveedorMapper;
import com.uoc.tfg.gestionvehiculos.dtos.proveedor.ProveedorRequest;
import com.uoc.tfg.gestionvehiculos.dtos.proveedor.ProveedorResponse;
import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
import com.uoc.tfg.gestionvehiculos.services.ProveedorService;
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
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Proveedores", description = "Gestión de proveedores")
@SecurityRequirement(name = "bearerAuth")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ProveedorResponse>> listarActivos() {
        log.info("Listando proveedores activos");
        List<Proveedor> proveedores = proveedorService.listarActivos();
        List<ProveedorResponse> response = ProveedorMapper.toListResponse(proveedores);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ProveedorResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo proveedor {}", id);
        Proveedor proveedor = proveedorService.obtenerPorId(id);
        ProveedorResponse response = ProveedorMapper.toResponse(proveedor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cif/{cif}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ProveedorResponse> obtenerPorCif(@PathVariable String cif) {
        log.info("Obteniendo proveedor con CIF {}", cif);
        Proveedor proveedor = proveedorService.obtenerPorCif(cif);
        ProveedorResponse response = ProveedorMapper.toResponse(proveedor);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ProveedorResponse> crear(@Valid @RequestBody ProveedorRequest request) {
        log.info("Creando proveedor {}", request.getNombreComercial());

        Proveedor proveedor = ProveedorMapper.toEntity(request);
        Proveedor creado = proveedorService.crear(proveedor);
        ProveedorResponse response = ProveedorMapper.toResponse(creado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ProveedorResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        log.info("Actualizando proveedor {}", id);

        Proveedor proveedor = proveedorService.obtenerPorId(id);
        ProveedorMapper.updateEntity(request, proveedor);

        Proveedor actualizado = proveedorService.actualizar(id, proveedor);
        ProveedorResponse response = ProveedorMapper.toResponse(actualizado);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desactivar(@PathVariable Long id) {
        log.info("Desactivando proveedor {}", id);

        proveedorService.desactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Proveedor desactivado exitosamente");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> reactivar(@PathVariable Long id) {
        log.info("Reactivando proveedor {}", id);

        proveedorService.reactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Proveedor reactivado exitosamente");

        return ResponseEntity.ok(response);
    }
}