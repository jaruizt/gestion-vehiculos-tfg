package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
import com.uoc.tfg.gestionvehiculos.services.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<Cliente>> listarActivos() {
        log.info("Listando clientes activos");
        List<Cliente> clientes = clienteService.listarActivos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo cliente {}", id);
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/documento/{documento}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Cliente> obtenerPorDocumento(@PathVariable String documento) {
        log.info("Obteniendo cliente con documento {}", documento);
        Cliente cliente = clienteService.obtenerPorDocumento(documento);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        log.info("Buscando clientes con nombre: {}", nombre);
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<Cliente>> obtenerPorTipo(@PathVariable TipoCliente tipo) {
        log.info("Listando clientes de tipo {}", tipo);
        List<Cliente> clientes = clienteService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        log.info("Creando cliente {}", cliente.getNombreCompleto());
        Cliente creado = clienteService.crear(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        log.info("Actualizando cliente {}", id);
        Cliente actualizado = clienteService.actualizar(id, cliente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desactivar(@PathVariable Long id) {
        log.info("Desactivando cliente {}", id);

        clienteService.desactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cliente desactivado ");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> reactivar(@PathVariable Long id) {
        log.info("Reactivando cliente {}", id);

        clienteService.reactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cliente reactivado ");

        return ResponseEntity.ok(response);
    }
}