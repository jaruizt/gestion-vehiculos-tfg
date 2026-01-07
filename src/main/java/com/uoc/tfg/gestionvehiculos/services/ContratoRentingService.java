package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.dtos.cuota.CuotaRentingResponse;
import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;
import com.uoc.tfg.gestionvehiculos.entities.CuotaRenting;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
import com.uoc.tfg.gestionvehiculos.exceptions.BusinessRuleException;
import com.uoc.tfg.gestionvehiculos.exceptions.DuplicateResourceException;
import com.uoc.tfg.gestionvehiculos.exceptions.ResourceNotFoundException;
import com.uoc.tfg.gestionvehiculos.repositories.ContratoRentingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContratoRentingService {

    private final ContratoRentingRepository contratoRepository;
    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;

    public List<ContratoRenting> listarActivos() {
        log.debug("Listando contratos de renting activos");
        return contratoRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .toList();

    }

    public ContratoRenting obtenerPorId(Long id) {
        log.debug("Buscando contrato con id: {}", id);
        return contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con id: " + id));
    }

    public ContratoRenting obtenerPorNumero(String numeroContrato) {
        log.debug("Buscando contrato con número: {}", numeroContrato);
        return contratoRepository.findByNumeroContrato(numeroContrato)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado: " + numeroContrato));
    }

    public List<ContratoRenting> obtenerPorCliente(Long clienteId) {
        log.debug("Listando contratos del cliente id: {}", clienteId);
        Cliente cliente = clienteService.obtenerPorId(clienteId);
        return contratoRepository.findByCliente(cliente);
    }

    public List<ContratoRenting> obtenerPorVehiculo(Long vehiculoId) {
        log.debug("Listando contratos del vehículo id: {}", vehiculoId);
        Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);
        return contratoRepository.findByVehiculo(vehiculo);
    }

    public List<ContratoRenting> obtenerPorEstado(EstadoContrato estado) {
        log.debug("Listando contratos con estado: {}", estado);
        return contratoRepository.findByEstado(estado);
    }

    public List<ContratoRenting> obtenerProximosAVencer(int dias) {
        log.debug("Listando contratos que vencen en {} días", dias);
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return contratoRepository.findByFechaFinBefore(fechaLimite).stream()
                .filter(c -> c.getEstado() == EstadoContrato.ACTIVO)
                .toList();
    }

    @Transactional
    public ContratoRenting crear(ContratoRenting contrato, Long clienteId, Long vehiculoId) {

        String numeroContrato = generarNumeroContrato();
        contrato.setNumeroContrato(numeroContrato);

        log.info("Creando contrato de renting: {}", contrato.getNumeroContrato());

        Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);
        if (!vehiculo.estaDisponibleParaRenting()) {
            throw new RuntimeException("El vehículo no está disponible para renting");
        }
        contrato.setVehiculo(vehiculo);
        contrato.setCliente(clienteService.obtenerPorId(clienteId));
        contrato.calcularDuracionMeses();

        ContratoRenting guardado = contratoRepository.save(contrato);

        generarCuotas(guardado);

        vehiculoService.cambiarSituacion(vehiculo.getId(), "EN_RENTING");

        log.info("Contrato creado con id: {} y número: {}", guardado.getId(), guardado.getNumeroContrato());
        return guardado;
    }

    private String generarNumeroContrato() {
        int anio = LocalDate.now().getYear();
        long totalContratos = contratoRepository.count();
        int numeroSecuencial = (int) (totalContratos + 1);

        return String.format("RENT-%d-%04d", anio, numeroSecuencial);
    }

    @Transactional
    public ContratoRenting actualizar(Long id, ContratoRenting contratoActualizado,Long clienteId, Long vehiculoId) {
        log.info("Actualizando contrato con id: {}", id);


        ContratoRenting contratoExistente = obtenerPorId(id);

        if (!contratoExistente.getNumeroContrato().equals(contratoActualizado.getNumeroContrato())) {
            if (contratoRepository.existsByNumeroContrato(contratoActualizado.getNumeroContrato())) {
                throw new DuplicateResourceException("contrato", "número", contratoActualizado.getNumeroContrato());
            }
        }

        contratoExistente.setNumeroContrato(contratoActualizado.getNumeroContrato());
        contratoExistente.setCliente(clienteService.obtenerPorId(clienteId));
        contratoExistente.setFechaInicio(contratoActualizado.getFechaInicio());
        contratoExistente.setFechaFin(contratoActualizado.getFechaFin());
        contratoExistente.setCuotaMensual(contratoActualizado.getCuotaMensual());
        contratoExistente.setKilometrosIncluidos(contratoActualizado.getKilometrosIncluidos());
        contratoExistente.setCosteKmExtra(contratoActualizado.getCosteKmExtra());
        contratoExistente.setObservaciones(contratoActualizado.getObservaciones());
        contratoExistente.setVehiculo(vehiculoService.obtenerPorId(vehiculoId));

        contratoExistente.calcularDuracionMeses();

        ContratoRenting actualizado = contratoRepository.save(contratoExistente);
        log.info("Contrato actualizado");

        return actualizado;
    }

    @Transactional
    public void finalizar(Long id) {
        log.info("Finalizando contrato id: {}", id);

        ContratoRenting contrato = obtenerPorId(id);

        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new RuntimeException("Solo se pueden finalizar contratos activos");
        }

        contrato.setEstado(EstadoContrato.FINALIZADO);
        contratoRepository.save(contrato);

        vehiculoService.cambiarSituacion(contrato.getVehiculo().getId(), "DISPONIBLE");

        log.info("Contrato finalizado");
    }

    @Transactional
    public void cancelar(Long id, String motivo) {
        log.info("Cancelando contrato id: {}", id);

        ContratoRenting contrato = obtenerPorId(id);

        if (contrato.getEstado() == EstadoContrato.FINALIZADO) {
            throw new BusinessRuleException("No se puede cancelar un contrato finalizado");
        }

        contrato.setEstado(EstadoContrato.CANCELADO);
        contrato.setObservaciones(contrato.getObservaciones() + "\nMotivo cancelación: " + motivo);
        contratoRepository.save(contrato);

        vehiculoService.cambiarSituacion(contrato.getVehiculo().getId(), "DISPONIBLE");

        log.info("Contrato cancelado");
    }

    private void generarCuotas(ContratoRenting contrato) {
        List<CuotaRenting> cuotas = new ArrayList<>();
        LocalDate fechaVencimiento = contrato.getFechaInicio();

        for (int i = 1; i <= contrato.getDuracionMeses(); i++) {
            fechaVencimiento = fechaVencimiento.plusMonths(1);

            CuotaRenting cuota = new CuotaRenting();
            cuota.setContrato(contrato);
            cuota.setNumeroCuota(i);
            cuota.setFechaVencimiento(fechaVencimiento);
            cuota.setImporte(contrato.getCuotaMensual());
            cuota.setEstado(EstadoCuota.PENDIENTE);

            cuotas.add(cuota);
        }

        contrato.getCuotas().addAll(cuotas);
    }

    @Transactional(readOnly = true)
    public List<CuotaRentingResponse> obtenerCuotasPorContrato(Long contratoId) {
        log.info("Obteniendo cuotas del contrato ID: {}", contratoId);

        ContratoRenting contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato", "id", contratoId));

        return contrato.getCuotas().stream()
                .map(this::convertirCuotaAResponse)
                .collect(Collectors.toList());
    }

    private CuotaRentingResponse convertirCuotaAResponse(CuotaRenting cuota) {
        CuotaRentingResponse response = new CuotaRentingResponse();
        response.setId(cuota.getId());
        response.setContratoRentingId(cuota.getContrato().getId());
        response.setNumeroCuota(cuota.getNumeroCuota());
        response.setFechaVencimiento(cuota.getFechaVencimiento());
        response.setImporte(cuota.getImporte());
        response.setEstadoNombre(cuota.getEstado().name());
        response.setFechaPago(cuota.getFechaPago());
        response.setActivo(cuota.getActivo());
        response.setObservaciones(cuota.getObservaciones());
        return response;
    }

    @Transactional
    public void activar(Long id) {
        log.info("Activando contrato con ID: {}", id);

        ContratoRenting contrato = obtenerPorId(id);

        if (contrato.getEstado() != EstadoContrato.PENDIENTE) {
            throw new BusinessRuleException(
                    "Solo se pueden activar contratos en estado PENDIENTE. Estado actual: " + contrato.getEstado()
            );
        }

        contrato.setEstado(EstadoContrato.ACTIVO);
        contratoRepository.save(contrato);

        log.info("Contrato {} activado correctamente", contrato.getNumeroContrato());
    }
}