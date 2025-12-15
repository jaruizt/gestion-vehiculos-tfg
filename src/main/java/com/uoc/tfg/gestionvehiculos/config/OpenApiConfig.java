package com.uoc.tfg.gestionvehiculos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Vehículos")
                        .description("Sistema de gestión integral para empresas del sector automovilístico. " +
                                "Permite gestionar el ciclo completo de vehículos desde la compra, " +
                                "pasando por contratos de renting, hasta la venta final.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("José Antonio Ruiz Traid")
                                .email("jaruizt@uoc.edu")
                                .url("https://github.com/jaruizt/gestion-vehiculos-tfg"))
                        .license(new License()
                                .name("TFG - UOC")
                                .url("https://www.uoc.edu")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de desarrollo local")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introduce el token JWT obtenido del endpoint /api/auth/login")));
    }
}