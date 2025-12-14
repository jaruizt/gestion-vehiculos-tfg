package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.FacturaVenta;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
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
public interface FacturaVentaRepository extends JpaRepository<FacturaVenta, Long> {

    Optional<FacturaVenta> findByNumeroFactura(String numeroFactura);

    Optional<FacturaVenta> findByVehiculo(Vehiculo vehiculo);

    List<FacturaVenta> findByCliente(Cliente cliente);

    List<FacturaVenta> findByFechaFacturaBetween(LocalDate inicio, LocalDate fin);

    boolean existsByNumeroFactura(String numeroFactura);
}