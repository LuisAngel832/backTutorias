package com.codespace.tutorias.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TutorDTO {
    @NotBlank(message = "Debes tener una matricula.")
    @Pattern(regexp = "^[a-zA-Z]\\d{8}$",
            message = "La matrícula debe comenzar con una letra y tener 8 dígitos")
    private String matricula;
    @NotBlank(message = "Debes tener un nombre.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$",
            message = "Debes ingresar un nombre valido")
    private String nombre;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$",
            message = "Debes ingresar un apellido valido")
    private String apellidoP;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$",
            message = "Debes ingresar un apellido valido")
    private String apellidoM;
    @NotBlank(message = "Debes ingresar un correo.")
    @Email(message = "Debes ingresar un correo valido.")
    private String correo;
    @NotBlank(message = "Debes ingresar una contraseña.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.")
    private String password;

    private String rol;


    public TutorDTO(){}

    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getRol() {
        return rol;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
