package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Repository
public interface ContratoRentingRepository extends JpaRepository<ContratoRenting, Long> {

    Optional<ContratoRenting> findByNumeroContrato(String numeroContrato);

    List<ContratoRenting> findByCliente(Cliente cliente);

    List<ContratoRenting> findByVehiculo(Vehiculo vehiculo);

    List<ContratoRenting> findByEstado(EstadoContrato estado);

    List<ContratoRenting> findByFechaFinBefore(LocalDate fecha);

    boolean existsByNumeroContrato(String numeroContrato);
}