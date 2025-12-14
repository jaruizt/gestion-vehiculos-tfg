package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author José Antonio Ruiz Traid
 * @date 11/2025
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByCif(String cif);

    boolean existsByCif(String cif);

    Optional<Proveedor> findByNombreComercial(String nombreComercial);
}