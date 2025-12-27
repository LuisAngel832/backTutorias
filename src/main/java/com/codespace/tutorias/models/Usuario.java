package com.codespace.tutorias.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @Column(nullable = false, unique = true)
    private String matricula;

    private String nombre;
    private String apellidoP;
    private String apellidoM;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private EnumRol rol;

    private String tokenRecuperacion;
    private LocalDateTime tokenExpiracion;
}
