package com.uoc.tfg.gestionvehiculos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class GestionVehiculosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionVehiculosApplication.class, args);
	}

}
