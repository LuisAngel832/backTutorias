package com.codespace.tutorias.controllers;

import com.codespace.tutorias.DTO.CrearHorarioDTO;
import com.codespace.tutorias.DTO.HorariosMostrarDTO;
import com.codespace.tutorias.DTO.HorariosDTO;
import com.codespace.tutorias.exceptions.ApiResponse;
import com.codespace.tutorias.services.HorarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/horarios")
public class HorarioController {
    @Autowired
    private HorarioService horarioService;

    @GetMapping()
    public List<HorariosMostrarDTO> obtenerHorarios() {
        return horarioService.listarHorariosPublicos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorariosMostrarDTO> obtenerHorario(@PathVariable("id") int id) {
        return horarioService.buscarHorarioPublico(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN','TUTOR')")
    @PostMapping()
    public ResponseEntity<?> crearHorario(@Valid @RequestBody CrearHorarioDTO dto, Authentication auth) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Horario creado correctamente", horarioService.crearHorario(dto, auth.getName())));
    }


    @PreAuthorize("hasAnyRole('ADMIN','TUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarHorario(@PathVariable("id") int id, @Valid @RequestBody HorariosDTO dto) {
        return horarioService.actualizarHorario(id, dto)
                .map(h -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN','TUTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable("id") int id, Authentication auth) {
        String matricula = auth.getName();
        horarioService.eliminarHorario(id, matricula);
        return ResponseEntity.noContent().build();
    }


}
