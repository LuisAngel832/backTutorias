package com.codespace.tutorias.repository;

import com.codespace.tutorias.models.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    @Query("SELECT h FROM Horario h JOIN h.tutor t WHERE t.matricula = :matricula")
    List<Horario> findByTutor(String matricula);

    Optional<Horario> findByIdHorarioAndTutor_Matricula(int id, String matricula);
}
