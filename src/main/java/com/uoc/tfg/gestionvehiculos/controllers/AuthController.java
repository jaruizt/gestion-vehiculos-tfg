package com.uoc.tfg.gestionvehiculos.controllers;

import com.uoc.tfg.gestionvehiculos.dtos.auth.LoginRequest;
import com.uoc.tfg.gestionvehiculos.dtos.auth.LoginResponse;
import com.uoc.tfg.gestionvehiculos.dtos.auth.RegistroRequest;
import com.uoc.tfg.gestionvehiculos.entities.Usuario;
import com.uoc.tfg.gestionvehiculos.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Autentica un usuario y retorna un token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * POST /api/auth/registro
     * Registra un nuevo usuario
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            Usuario nuevoUsuario = authService.registrar(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("username", nuevoUsuario.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }
}