package com.uoc.tfg.gestionvehiculos.dtos.vehiculo;

import com.uoc.tfg.gestionvehiculos.enums.TipoCombustible;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoRequest {

    @NotBlank(message = "La matrícula es obligatoria")
    @Size(max = 20, message = "La matrícula no puede tener más de 20 caracteres")
    private String matricula;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 100, message = "La marca no puede tener más de 100 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 100, message = "El modelo no puede tener más de 100 caracteres")
    private String modelo;

    @NotNull(message = "El año de fabricación es obligatorio")
    @Min(value = 1900, message = "El año debe ser posterior a 1900")
    @Max(value = 2100, message = "El año no puede ser posterior a 2100")
    private Integer anyoFabricacion;

    @Size(max = 50, message = "El color no puede tener más de 50 caracteres")
    private String color;

    @NotNull(message = "Los kilómetros son obligatorios")
    @Min(value = 0, message = "Los kilómetros no pueden ser negativos")
    private Integer kilometros;

    @NotBlank(message = "El número de bastidor es obligatorio")
    @Size(max = 50, message = "El número de bastidor no puede tener más de 50 caracteres")
    private String numeroBastidor;

    @NotNull(message = "El tipo de combustible es obligatorio")
    private TipoCombustible tipoCombustible;

    private Long situacionId;
}