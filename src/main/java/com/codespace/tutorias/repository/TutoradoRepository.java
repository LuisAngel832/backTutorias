package com.codespace.tutorias.repository;

import com.codespace.tutorias.models.Tutorado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TutoradoRepository extends JpaRepository <Tutorado, String> {

    @Query("SELECT t FROM Tutorado t WHERE t.correo = :correo")
    Tutorado findByCorreo(String correo);

    Optional<Tutorado> findByTokenRecuperacion(String token);
}
