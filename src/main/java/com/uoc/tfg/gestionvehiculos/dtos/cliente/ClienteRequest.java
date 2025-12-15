package com.uoc.tfg.gestionvehiculos.dtos.cliente;

import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ClienteRequest {

    @NotNull(message = "El tipo de cliente es obligatorio")
    private TipoCliente tipoCliente;

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede tener más de 20 caracteres")
    private String documento;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede tener más de 200 caracteres")
    private String nombre;

    @Size(max = 200, message = "Los apellidos no pueden tener más de 200 caracteres")
    private String apellidos;

    @Size(max = 200, message = "La razón social no puede tener más de 200 caracteres")
    private String razonSocial;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 300, message = "La dirección no puede tener más de 300 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede tener más de 100 caracteres")
    private String ciudad;

    @Size(max = 100, message = "La provincia no puede tener más de 100 caracteres")
    private String provincia;

    @Size(max = 10, message = "El código postal no puede tener más de 10 caracteres")
    private String codigoPostal;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    @Email(message = "Email inválido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    private String observaciones;
}