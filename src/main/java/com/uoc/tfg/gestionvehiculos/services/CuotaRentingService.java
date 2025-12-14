package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;
import com.uoc.tfg.gestionvehiculos.entities.CuotaRenting;
import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
import com.uoc.tfg.gestionvehiculos.repositories.CuotaRentingRepository;
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
public class CuotaRentingService {

    private final CuotaRentingRepository cuotaRepository;
    private final ContratoRentingService contratoService;

    public List<CuotaRenting> listarActivas() {
        log.debug("Listando cuotas activas");
        return cuotaRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .toList();
    }

    public CuotaRenting obtenerPorId(Long id) {
        log.debug("Buscando cuota con id: {}", id);
        return cuotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada con id: " + id));
    }

    public List<CuotaRenting> obtenerPorContrato(Long contratoId) {
        log.debug("Listando cuotas del contrato id: {}", contratoId);
        ContratoRenting contrato = contratoService.obtenerPorId(contratoId);
        return cuotaRepository.findByContrato(contrato);
    }

    public List<CuotaRenting> obtenerPorEstado(EstadoCuota estado) {
        log.debug("Listando cuotas con estado: {}", estado);
        return cuotaRepository.findByEstado(estado);
    }

    public List<CuotaRenting> obtenerPendientes() {
        log.debug("Listando cuotas pendientes");
        return cuotaRepository.findByEstado(EstadoCuota.PENDIENTE);
    }

    public List<CuotaRenting> obtenerVencidas() {
        log.debug("Listando cuotas vencidas");
        return cuotaRepository.findByEstadoAndFechaVencimientoBefore(
                EstadoCuota.PENDIENTE,
                LocalDate.now()
        );
    }

    public List<CuotaRenting> obtenerProximasAVencer(int dias) {
        log.debug("Listando cuotas que vencen en {} días", dias);
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return cuotaRepository.findByFechaVencimientoBefore(fechaLimite).stream()
                .filter(c -> c.getEstado() == EstadoCuota.PENDIENTE)
                .toList();
    }

    @Transactional
    public CuotaRenting marcarComoPagada(Long id) {
        log.info("Marcando cuota {} como pagada", id);

        CuotaRenting cuota = obtenerPorId(id);

        if (cuota.getEstado() == EstadoCuota.PAGADA) {
            throw new BusinessRuleException("La cuota ya está marcada como pagada");
        }

        if (cuota.getEstado() == EstadoCuota.CANCELADA) {
            throw new BusinessRuleException("No se puede marcar como pagada una cuota cancelada");
        }

        cuota.marcarComoPagada();

        CuotaRenting actualizada = cuotaRepository.save(cuota);
        log.info("Cuota marcada como pagada");

        return actualizada;
    }

    @Transactional
    public void marcarComoVencida(Long id) {
        log.info("Marcando cuota {} como vencida", id);

        CuotaRenting cuota = obtenerPorId(id);

        if (cuota.getEstado() != EstadoCuota.PENDIENTE) {
            throw new RuntimeException("Solo se pueden marcar como vencidas cuotas pendientes");
        }

        if (!cuota.estaVencida()) {
            throw new BusinessRuleException("La cuota aún no ha vencido");
        }

        cuota.setEstado(EstadoCuota.VENCIDA);
        cuotaRepository.save(cuota);

        log.info("Cuota marcada como vencida");
    }

    @Transactional
    public void actualizarCuotasVencidas() {
        log.info("Actualizando estado de cuotas vencidas");

        List<CuotaRenting> cuotasVencidas = obtenerVencidas();

        for (CuotaRenting cuota : cuotasVencidas) {
            cuota.setEstado(EstadoCuota.VENCIDA);
        }

        cuotaRepository.saveAll(cuotasVencidas);
        log.info("Actualizadas {} cuotas vencidas", cuotasVencidas.size());
    }
}