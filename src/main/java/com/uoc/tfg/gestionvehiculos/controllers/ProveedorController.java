package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
import com.uoc.tfg.gestionvehiculos.services.ProveedorService;
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
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<Proveedor>> listarActivos() {
        log.info("Listando proveedores activos");
        List<Proveedor> proveedores = proveedorService.listarActivos();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo proveedor {}", id);
        Proveedor proveedor = proveedorService.obtenerPorId(id);
        return ResponseEntity.ok(proveedor);
    }

    @GetMapping("/cif/{cif}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Proveedor> obtenerPorCif(@PathVariable String cif) {
        log.info("Obteniendo proveedor con CIF {}", cif);
        Proveedor proveedor = proveedorService.obtenerPorCif(cif);
        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        log.info("Creando proveedor {}", proveedor.getNombreComercial());
        Proveedor creado = proveedorService.crear(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        log.info("Actualizando proveedor {}", id);
        Proveedor actualizado = proveedorService.actualizar(id, proveedor);
        return ResponseEntity.ok(actualizado);
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