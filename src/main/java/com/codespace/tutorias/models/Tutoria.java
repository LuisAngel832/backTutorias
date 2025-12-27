package com.codespace.tutorias.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Tutoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTutoria;

    @ManyToOne
    @JoinColumn(name= "idHorario")
    private Horario horario;

    @OneToMany(mappedBy = "tutoria")
    private List<TutoriaTutorado> tutoriasTutorados = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name= "nrc")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name= "matriculaTutor", nullable = false)
    private Tutor tutor;

    private LocalDate fecha;
    private int edificio;
    private int aula;
    private String estado;

    public Tutoria(){}

}