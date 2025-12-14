package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.FacturaCompra;
import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
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
public interface FacturaCompraRepository extends JpaRepository<FacturaCompra, Long> {

    Optional<FacturaCompra> findByNumeroFactura(String numeroFactura);

    Optional<FacturaCompra> findByVehiculo(Vehiculo vehiculo);

    List<FacturaCompra> findByProveedor(Proveedor proveedor);

    List<FacturaCompra> findByFechaFacturaBetween(LocalDate inicio, LocalDate fin);

    boolean existsByNumeroFactura(String numeroFactura);
}