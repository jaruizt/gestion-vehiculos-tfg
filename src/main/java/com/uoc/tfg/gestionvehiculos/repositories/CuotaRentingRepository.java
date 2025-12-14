package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;
import com.uoc.tfg.gestionvehiculos.entities.CuotaRenting;
import com.uoc.tfg.gestionvehiculos.enums.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Repository
public interface CuotaRentingRepository extends JpaRepository<CuotaRenting, Long> {

    List<CuotaRenting> findByContrato(ContratoRenting contrato);

    List<CuotaRenting> findByEstado(EstadoCuota estado);

    List<CuotaRenting> findByFechaVencimientoBefore(LocalDate fecha);

    List<CuotaRenting> findByEstadoAndFechaVencimientoBefore(
            EstadoCuota estado,
            LocalDate fecha
    );
}