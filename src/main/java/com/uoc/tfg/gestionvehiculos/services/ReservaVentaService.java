package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.ReservaVenta;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.EstadoReserva;
import com.uoc.tfg.gestionvehiculos.exceptions.BusinessRuleException;
import com.uoc.tfg.gestionvehiculos.exceptions.DuplicateResourceException;
import com.uoc.tfg.gestionvehiculos.repositories.ReservaVentaRepository;
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
public class ReservaVentaService {

    private final ReservaVentaRepository reservaRepository;
    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;

    public List<ReservaVenta> listarActivas() {
        log.debug("Listando reservas activas");
        return reservaRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getActivo()))
                .toList();
    }

    public ReservaVenta obtenerPorId(Long id) {
        log.debug("Buscando reserva con id: {}", id);
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    public List<ReservaVenta> obtenerPorCliente(Long clienteId) {
        log.debug("Listando reservas del cliente id: {}", clienteId);
        Cliente cliente = clienteService.obtenerPorId(clienteId);
        return reservaRepository.findByCliente(cliente);
    }

    public List<ReservaVenta> obtenerPorVehiculo(Long vehiculoId) {
        log.debug("Listando reservas del vehículo id: {}", vehiculoId);
        Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);
        return reservaRepository.findByVehiculo(vehiculo);
    }

    public List<ReservaVenta> obtenerPorEstado(EstadoReserva estado) {
        log.debug("Listando reservas con estado: {}", estado);
        return reservaRepository.findByEstado(estado);
    }

    public List<ReservaVenta> obtenerExpiradas() {
        log.debug("Listando reservas expiradas");
        return reservaRepository.findByEstadoAndFechaLimiteBefore(
                EstadoReserva.PENDIENTE,
                LocalDate.now()
        );
    }

    @Transactional
    public ReservaVenta crear(ReservaVenta reserva, Long clienteId, Long vehiculoId) {
        log.info("Creando reserva de venta");

        Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);

        // Validar que el vehículo esté disponible
        if (!vehiculo.getSituacion().getNombre().equals("DISPONIBLE")) {
            throw new BusinessRuleException(
                    "El vehículo debe estar DISPONIBLE. Estado actual: " + vehiculo.getSituacion().getNombre()
            );
        }

        if (reservaRepository.existsByVehiculoIdAndActivoTrue(vehiculoId)) {
            throw new DuplicateResourceException("reserva", "vehículo", vehiculoId.toString());
        }

        reserva.setVehiculo(vehiculo);

        Cliente cliente = clienteService.obtenerPorId(clienteId);
        reserva.setCliente(cliente);

        // Guardar reserva
        ReservaVenta guardada = reservaRepository.save(reserva);

        // Cambiar situación del vehículo a RESERVADO
        vehiculoService.cambiarSituacion(vehiculoId, "RESERVADO");

        log.info("Reserva creada para vehículo {}", vehiculo.getMatricula()); 

        return guardada;
    }

    @Transactional
    public ReservaVenta confirmar(Long id) {
        log.info("Confirmando reserva id: {}", id);

        ReservaVenta reserva = obtenerPorId(id);

        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new RuntimeException("Solo se pueden confirmar reservas pendientes");
        }

        if (reserva.estaExpirada()) {
            throw new BusinessRuleException("La reserva ha expirado");
        }

        reserva.confirmar();

        ReservaVenta actualizada = reservaRepository.save(reserva);
        log.info("Reserva confirmada exitosamente");

        return actualizada;
    }

    @Transactional
    public void cancelar(Long id, String motivo) {
        log.info("Cancelando reserva id: {}", id);

        ReservaVenta reserva = obtenerPorId(id);

        if (reserva.getEstado() == EstadoReserva.COMPLETADA) {
            throw new BusinessRuleException("No se puede cancelar una reserva completada");
        }

        reserva.cancelar();
        reserva.setObservaciones(reserva.getObservaciones() + "\nMotivo cancelación: " + motivo);
        reservaRepository.save(reserva);

        vehiculoService.cambiarSituacion(reserva.getVehiculo().getId(), "DISPONIBLE");

        log.info("Reserva cancelada ");
    }

    @Transactional
    public void completar(Long id) {
        log.info("Completando reserva id: {}", id);

        ReservaVenta reserva = obtenerPorId(id);

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new RuntimeException("Solo se pueden completar reservas confirmadas");
        }

        reserva.completar();
        reservaRepository.save(reserva);

        log.info("Reserva completada ");
    }

    @Transactional
    public void actualizarReservasExpiradas() {
        log.info("Actualizando reservas expiradas");

        List<ReservaVenta> reservasExpiradas = obtenerExpiradas();

        for (ReservaVenta reserva : reservasExpiradas) {
            reserva.cancelar();
            reserva.setObservaciones(reserva.getObservaciones() + "\nCancelada automáticamente por expiración");
            vehiculoService.cambiarSituacion(reserva.getVehiculo().getId(), "DISPONIBLE");
        }

        reservaRepository.saveAll(reservasExpiradas);
        log.info("Actualizadas {} reservas expiradas", reservasExpiradas.size());
    }
}