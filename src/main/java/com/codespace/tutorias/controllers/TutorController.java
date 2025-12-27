package com.codespace.tutorias.controllers;

import com.codespace.tutorias.DTO.TutorDTO;
import com.codespace.tutorias.DTO.TutoresPublicosDTO;
import com.codespace.tutorias.exceptions.ApiResponse;
import com.codespace.tutorias.services.HorarioService;
import com.codespace.tutorias.services.TutorService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;
    @Autowired
    private HorarioService horarioService;

    @GetMapping()
    public List<TutoresPublicosDTO> getTutores(){
        return tutorService.mostrarTutoresPublicos();
    }

    @PostMapping()
    public TutorDTO registrarTutorado(@Valid @RequestBody TutorDTO dto){
        return tutorService.crearTutores(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutoresPublicosDTO> findTutor(@PathVariable String id){
        return tutorService.buscarTutorPublico(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/me/horarios")
    public ResponseEntity<?> misHorarios(Authentication auth){   
        return ResponseEntity.ok(new ApiResponse<>(true, "Mis horarios:", horarioService.misHorarios(auth.getName())));
    }

    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/me/tutorias")
    public ResponseEntity<?> misTutorias(Authentication auth){
        return ResponseEntity.ok(new ApiResponse<>(true, "Mis tutorias",tutorService.findMisTutorias(auth.getName())));
    }



}
