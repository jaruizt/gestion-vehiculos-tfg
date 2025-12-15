package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.cliente.ClienteMapper;
import com.uoc.tfg.gestionvehiculos.dtos.cliente.ClienteRequest;
import com.uoc.tfg.gestionvehiculos.dtos.cliente.ClienteResponse;
import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
import com.uoc.tfg.gestionvehiculos.services.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ClienteResponse>> listarActivos() {
        log.info("Listando clientes activos");
        List<Cliente> clientes = clienteService.listarActivos();
        List<ClienteResponse> response = ClienteMapper.toListResponse(clientes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo cliente {}", id);
        Cliente cliente = clienteService.obtenerPorId(id);
        ClienteResponse response = ClienteMapper.toResponse(cliente);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/documento/{documento}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ClienteResponse> obtenerPorDocumento(@PathVariable String documento) {
        log.info("Obteniendo cliente con documento {}", documento);
        Cliente cliente = clienteService.obtenerPorDocumento(documento);
        ClienteResponse response = ClienteMapper.toResponse(cliente);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ClienteResponse>> buscarPorNombre(@RequestParam String nombre) {
        log.info("Buscando clientes con nombre: {}", nombre);
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        List<ClienteResponse> response = ClienteMapper.toListResponse(clientes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<List<ClienteResponse>> obtenerPorTipo(@PathVariable TipoCliente tipo) {
        log.info("Listando clientes de tipo {}", tipo);
        List<Cliente> clientes = clienteService.obtenerPorTipo(tipo);
        List<ClienteResponse> response = ClienteMapper.toListResponse(clientes);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        log.info("Creando cliente {}", request.getNombre());

        Cliente cliente = ClienteMapper.toEntity(request);
        Cliente creado = clienteService.crear(cliente);
        ClienteResponse response = ClienteMapper.toResponse(creado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COMERCIAL')")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        log.info("Actualizando cliente {}", id);

        Cliente cliente = clienteService.obtenerPorId(id);
        ClienteMapper.updateEntity(request, cliente);

        Cliente actualizado = clienteService.actualizar(id, cliente);
        ClienteResponse response = ClienteMapper.toResponse(actualizado);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desactivar(@PathVariable Long id) {
        log.info("Desactivando cliente {}", id);

        clienteService.desactivar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cliente desactivado exitosamente");

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