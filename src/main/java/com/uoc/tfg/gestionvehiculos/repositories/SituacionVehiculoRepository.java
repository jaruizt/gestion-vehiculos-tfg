package com.uoc.tfg.gestionvehiculos.repositories;

import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SituacionVehiculoRepository extends JpaRepository<SituacionVehiculo, Long> {

    Optional<SituacionVehiculo> findByNombre(String codigo);
}
