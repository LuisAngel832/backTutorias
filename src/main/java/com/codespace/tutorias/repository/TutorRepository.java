package com.codespace.tutorias.repository;

import com.codespace.tutorias.models.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, String> {
    @Query("SELECT t FROM Tutor t WHERE t.correo = :correo")
    Tutor findByCorreo(String correo);

    Optional<Tutor> findByTokenRecuperacion(String token);
}
