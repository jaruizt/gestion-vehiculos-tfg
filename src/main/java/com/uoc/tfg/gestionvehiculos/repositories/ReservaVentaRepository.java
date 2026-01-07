package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.ReservaVenta;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.EstadoReserva;
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
public interface ReservaVentaRepository extends JpaRepository<ReservaVenta, Long> {

    List<ReservaVenta> findByCliente(Cliente cliente);

    List<ReservaVenta> findByVehiculo(Vehiculo vehiculo);

    List<ReservaVenta> findByEstado(EstadoReserva estado);

    List<ReservaVenta> findByFechaLimiteBefore(LocalDate fecha);

    List<ReservaVenta> findByEstadoAndFechaLimiteBefore(
            EstadoReserva estado,
            LocalDate fecha
    );

    boolean existsByVehiculoIdAndActivoTrue(Long vehiculoId);
}