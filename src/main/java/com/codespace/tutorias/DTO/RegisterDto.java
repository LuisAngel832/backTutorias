package com.codespace.tutorias.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDto {
    private String matricula;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String correo;
    private String password;
    private String rol;
}
