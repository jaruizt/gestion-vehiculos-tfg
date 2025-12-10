package com.uoc.tfg.gestionvehiculos.config;

import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import com.uoc.tfg.gestionvehiculos.entities.Usuario;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.Rol;
import com.uoc.tfg.gestionvehiculos.enums.TipoCombustible;
import com.uoc.tfg.gestionvehiculos.repositories.SituacionVehiculoRepository;
import com.uoc.tfg.gestionvehiculos.repositories.UsuarioRepository;
import com.uoc.tfg.gestionvehiculos.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            SituacionVehiculoRepository situacionRepository,
            VehiculoRepository vehiculoRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            log.info("Inicializando datos de prueba:");

            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setEmail("admin@gestionvehiculos.com");
                admin.setNombre("Administrador");
                admin.setApellidos("Unico");
                admin.setTelefono("666666666");
                admin.setRol(Rol.ADMIN);
                admin.setFechaCambioPassword(LocalDateTime.now());
                usuarioRepository.save(admin);

                Usuario gerente = new Usuario();
                gerente.setUsername("gerente");
                gerente.setPassword(passwordEncoder.encode("gerente1234"));
                gerente.setEmail("gerente@gestionvehiculos.com");
                gerente.setNombre("gerente");
                gerente.setApellidos("Normal");
                gerente.setTelefono("666666667");
                gerente.setRol(Rol.GERENTE);
                gerente.setFechaCambioPassword(LocalDateTime.now());
                usuarioRepository.save(gerente);

                Usuario comercial = new Usuario();
                comercial.setUsername("comercial");
                comercial.setPassword(passwordEncoder.encode("comercial1234"));
                comercial.setEmail("comercial@gestionvehiculos.com");
                comercial.setNombre("Comercial");
                comercial.setApellidos("Ventas");
                comercial.setTelefono("666666667");
                comercial.setRol(Rol.COMERCIAL);
                comercial.setFechaCambioPassword(LocalDateTime.now());
                usuarioRepository.save(comercial);

                log.info("Usuarios creados");
            }

            if (situacionRepository.count() == 0) {
                SituacionVehiculo disponible = new SituacionVehiculo();
                disponible.setNombre("DISPONIBLE");
                disponible.setDescripcion("Vehículo disponible para renting");
                disponible.setOrden(1);
                situacionRepository.save(disponible);

                SituacionVehiculo enRenting = new SituacionVehiculo();
                enRenting.setNombre("EN_RENTING");
                enRenting.setDescripcion("Vehículo actualmente en contrato de renting");
                enRenting.setOrden(2);
                situacionRepository.save(enRenting);

                SituacionVehiculo reservado = new SituacionVehiculo();
                reservado.setNombre("RESERVADO");
                reservado.setDescripcion("Vehículo reservado para venta");
                reservado.setOrden(3);
                situacionRepository.save(reservado);

                SituacionVehiculo vendido = new SituacionVehiculo();
                vendido.setNombre("VENDIDO");
                vendido.setDescripcion("Vehículo vendido");
                vendido.setOrden(4);
                situacionRepository.save(vendido);

                log.info("Situaciones creadas");
            }

            if (vehiculoRepository.count() == 0) {
                SituacionVehiculo disponible = situacionRepository.findByNombre("DISPONIBLE").orElseThrow();

                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setMatricula("1234ABC");
                vehiculo.setMarca("Mercedes-Benz");
                vehiculo.setModelo("Vito Tourer");
                vehiculo.setAnyoFabricacion(2023);
                vehiculo.setColor("Blanco");
                vehiculo.setKilometros(15000);
                vehiculo.setNumeroBastidor("WDF63970012345678");
                vehiculo.setTipoCombustible(TipoCombustible.DIESEL);
                vehiculo.setSituacion(disponible);

                vehiculoRepository.save(vehiculo);

                log.info("Vehículo creado - ID: {}, Fecha creacion: {}",
                        vehiculo.getId(),
                        vehiculo.getFechaCreacion());
            }

            log.info("Datos inicializados correctamente");
        };
    }
}
