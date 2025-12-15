package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.FacturaCompra;
import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.exceptions.DuplicateResourceException;
import com.uoc.tfg.gestionvehiculos.repositories.FacturaCompraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FacturaCompraService {

    private final FacturaCompraRepository facturaCompraRepository;
    private final VehiculoService vehiculoService;
    private final ProveedorService proveedorService;

    /**
     * Lista todas las facturas de compra activas
     */
    public List<FacturaCompra> listarActivas() {
        log.debug("Listando todas las facturas de compra activas");
        return facturaCompraRepository.findAll().stream()
                .filter(f -> Boolean.TRUE.equals(f.getActivo()))
                .toList();
    }

    /**
     * Obtiene una factura por ID
     */
    public FacturaCompra obtenerPorId(Long id) {
        log.debug("Buscando factura de compra con id: {}", id);
        return facturaCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura de compra no encontrada con id: " + id));
    }

    /**
     * Obtiene una factura por número
     */
    public FacturaCompra obtenerPorNumero(String numeroFactura) {
        log.debug("Buscando factura de compra con número: {}", numeroFactura);
        return facturaCompraRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con número: " + numeroFactura));
    }

    /**
     * Obtiene facturas de un proveedor
     */
    public List<FacturaCompra> obtenerPorProveedor(Long proveedorId) {
        log.debug("Listando facturas del proveedor id: {}", proveedorId);
        Proveedor proveedor = proveedorService.obtenerPorId(proveedorId);
        return facturaCompraRepository.findByProveedor(proveedor);
    }

    /**
     * Obtiene facturas en un rango de fechas
     */
    public List<FacturaCompra> obtenerPorFechas(LocalDate inicio, LocalDate fin) {
        log.debug("Listando facturas entre {} y {}", inicio, fin);
        return facturaCompraRepository.findByFechaFacturaBetween(inicio, fin);
    }

    /**
     * Crea una nueva factura de compra
     */
    @Transactional
    public FacturaCompra crear(FacturaCompra factura, Long proveedorId, Long vehiculoId) {
        log.info("Creando nueva factura de compra: {}", factura.getNumeroFactura());

        if (facturaCompraRepository.existsByNumeroFactura(factura.getNumeroFactura())) {
            throw new DuplicateResourceException("factura", "número", factura.getNumeroFactura());
        }

        if (facturaCompraRepository.findByVehiculo(factura.getVehiculo()).isPresent()) {
            throw new RuntimeException("El vehículo ya tiene una factura de compra asociada");
        }

        factura.setProveedor(proveedorService.obtenerPorId(proveedorId));
        factura.setVehiculo(vehiculoService.obtenerPorId(vehiculoId));
        factura.calcularImporteTotal();
        FacturaCompra guardada = facturaCompraRepository.save(factura);
        log.info("Factura de compra creada con id: {}", guardada.getId());

        return guardada;
    }

    /**
     * Actualiza una factura de compra
     */
    @Transactional
    public FacturaCompra actualizar(Long id, FacturaCompra facturaActualizada, Long proveedorId, Long vehiculoId) {
        log.info("Actualizando factura de compra con id: {}", id);

        FacturaCompra facturaExistente = obtenerPorId(id);

        if (!facturaExistente.getNumeroFactura().equals(facturaActualizada.getNumeroFactura())) {
            if (facturaCompraRepository.existsByNumeroFactura(facturaActualizada.getNumeroFactura())) {
                throw new DuplicateResourceException("factura", "número", facturaActualizada.getNumeroFactura());
            }
        }


        facturaExistente.setNumeroFactura(facturaActualizada.getNumeroFactura());
        facturaExistente.setFechaFactura(facturaActualizada.getFechaFactura());
        facturaExistente.setProveedor(proveedorService.obtenerPorId(proveedorId));
        facturaExistente.setImporteBase(facturaActualizada.getImporteBase());
        facturaExistente.setIva(facturaActualizada.getIva());
        facturaExistente.setObservaciones(facturaActualizada.getObservaciones());
        facturaExistente.setVehiculo(vehiculoService.obtenerPorId(vehiculoId));

        facturaExistente.calcularImporteTotal();

        FacturaCompra actualizada = facturaCompraRepository.save(facturaExistente);
        log.info("Factura de compra actualizada");

        return actualizada;
    }
}