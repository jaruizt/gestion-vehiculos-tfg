package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.FacturaVenta;
import com.uoc.tfg.gestionvehiculos.entities.ReservaVenta;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.EstadoReserva;
import com.uoc.tfg.gestionvehiculos.exceptions.BusinessRuleException;
import com.uoc.tfg.gestionvehiculos.exceptions.DuplicateResourceException;
import com.uoc.tfg.gestionvehiculos.repositories.FacturaVentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
public class FacturaVentaService {

    private final FacturaVentaRepository facturaVentaRepository;
    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;
    private final ReservaVentaService reservaVentaService;

    public List<FacturaVenta> listarActivas() {
        log.debug("Listando facturas de venta activas");
        return facturaVentaRepository.findAll().stream()
                .filter(f -> Boolean.TRUE.equals(f.getActivo()))
                .toList();
    }

    public FacturaVenta obtenerPorId(Long id) {
        log.debug("Buscando factura de venta con id: {}", id);
        return facturaVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura de venta no encontrada con id: " + id));
    }

    public FacturaVenta obtenerPorNumero(String numeroFactura) {
        log.debug("Buscando factura de venta con número: {}", numeroFactura);
        return facturaVentaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada: " + numeroFactura));
    }

    public List<FacturaVenta> obtenerPorCliente(Long clienteId) {
        log.debug("Listando facturas del cliente id: {}", clienteId);
        Cliente cliente = clienteService.obtenerPorId(clienteId);
        return facturaVentaRepository.findByCliente(cliente);
    }

    public List<FacturaVenta> obtenerPorFechas(LocalDate inicio, LocalDate fin) {
        log.debug("Listando facturas entre {} y {}", inicio, fin);
        return facturaVentaRepository.findByFechaFacturaBetween(inicio, fin);
    }

    @Transactional
    public FacturaVenta crear(FacturaVenta factura) {
        log.info("Creando factura de venta: {}", factura.getNumeroFactura());

        if (facturaVentaRepository.existsByNumeroFactura(factura.getNumeroFactura())) {
            throw new DuplicateResourceException("factura", "número", factura.getNumeroFactura());
        }

        Vehiculo vehiculo = factura.getVehiculo();

        if (facturaVentaRepository.findByVehiculo(vehiculo).isPresent()) {
            throw new RuntimeException("El vehículo ya ha sido vendido");
        }

        if (vehiculo.estaEnRenting()) {
            throw new BusinessRuleException("No se puede vender un vehículo que está en renting");
        }

        factura.calcularImporteTotal();

        FacturaVenta guardada = facturaVentaRepository.save(factura);

        vehiculoService.cambiarSituacion(vehiculo.getId(), "VENDIDO");

        if (factura.getReserva() != null) {
            reservaVentaService.completar(factura.getReserva().getId());
        }

        log.info("Factura de venta creada con id: {}", guardada.getId());
        return guardada;
    }

    @Transactional
    public FacturaVenta actualizar(Long id, FacturaVenta facturaActualizada) {
        log.info("Actualizando factura de venta con id: {}", id);

        FacturaVenta facturaExistente = obtenerPorId(id);

        if (!facturaExistente.getNumeroFactura().equals(facturaActualizada.getNumeroFactura())) {
            if (facturaVentaRepository.existsByNumeroFactura(facturaActualizada.getNumeroFactura())) {
                throw new DuplicateResourceException("factura", "número", facturaActualizada.getNumeroFactura());
            }
        }

        facturaExistente.setNumeroFactura(facturaActualizada.getNumeroFactura());
        facturaExistente.setFechaFactura(facturaActualizada.getFechaFactura());
        facturaExistente.setCliente(facturaActualizada.getCliente());
        facturaExistente.setImporteBase(facturaActualizada.getImporteBase());
        facturaExistente.setIva(facturaActualizada.getIva());
        facturaExistente.setDescuento(facturaActualizada.getDescuento());
        facturaExistente.setObservaciones(facturaActualizada.getObservaciones());

        facturaExistente.calcularImporteTotal();

        FacturaVenta actualizada = facturaVentaRepository.save(facturaExistente);
        log.info("Factura de venta actualizada ");

        return actualizada;
    }

    public BigDecimal calcularBeneficio(Long id) {
        log.debug("Calculando beneficio de factura id: {}", id);
        FacturaVenta factura = obtenerPorId(id);
        return factura.calcularBeneficio();
    }

    public BigDecimal calcularBeneficioTotal(LocalDate inicio, LocalDate fin) {
        log.debug("Calculando beneficio total entre {} y {}", inicio, fin);

        List<FacturaVenta> facturas = obtenerPorFechas(inicio, fin);

        return facturas.stream()
                .map(FacturaVenta::calcularBeneficio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}