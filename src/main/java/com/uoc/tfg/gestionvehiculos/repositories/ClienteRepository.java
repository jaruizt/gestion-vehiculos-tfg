package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<Cliente> findByTipoCliente(TipoCliente tipoCliente);

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
