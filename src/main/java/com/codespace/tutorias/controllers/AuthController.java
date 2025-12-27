package com.codespace.tutorias.controllers;

import com.codespace.tutorias.DTO.*;
import com.codespace.tutorias.exceptions.ApiResponse;
import com.codespace.tutorias.exceptions.BusinessException;
import com.codespace.tutorias.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService ;

    public AuthController(AuthService authService) {
        this.authService =  authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginDTO dto){
        try{
            return ResponseEntity.ok(authService.login(dto));
        }catch (BusinessException e) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, e.getMessage(), null));
        }
        
    }

    @PostMapping("/recovery")
    public ResponseEntity<?> enviarCorreoRecuperacion(@RequestBody CorreoDTO dto){
        try{
            authService.recovery(dto);
            return ResponseEntity.ok(new ApiResponse<>(
                true, "Correo de recuperación enviado", null));
        }catch (BusinessException e) {
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/newPassword")
    public ResponseEntity<?> cambioPassword(@Valid @RequestBody CambioPasswordDTO dto){
        try{
            authService.newPassword(dto);
            return ResponseEntity.ok(new ApiResponse<>(
                true, "Contraseña actualizada exitosamente", null));
        }catch (BusinessException e) {
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto){
        try{
            authService.register(dto);
            return ResponseEntity.ok(new ApiResponse<>(
                true, "Usuario registrado exitosamente", null));
        }catch (BusinessException e) {
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
