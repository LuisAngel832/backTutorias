package com.codespace.tutorias.controllers;

import com.codespace.tutorias.DTO.ActualizarTutoriaDTO;
import com.codespace.tutorias.DTO.CrearTutoriaDTO;
import com.codespace.tutorias.exceptions.ApiResponse;
import com.codespace.tutorias.services.TutoriasService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutorias")
public class TutoriasController {
    @Autowired
    private TutoriasService tutoriasService;

    @GetMapping()
    public ResponseEntity<?> obtenerTutorias() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Tutorias ", tutoriasService.mostrarTutorias()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTutoria(@PathVariable("id") int id) {
        return tutoriasService.findTutoriaPublica(id)
                .map(t -> ResponseEntity.ok(new ApiResponse<>(true, "Tutoría encontrada", t)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Tutoría no encontrada", null)));
    }

    @PreAuthorize("hasAnyRole('TUTOR','ADMIN')")
    @PostMapping()
    public ResponseEntity<?> generarTutoria(@Valid @RequestBody CrearTutoriaDTO dto, Authentication auth) {
        String matricula = auth.getName();
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Tutoria generada correctamente", tutoriasService.generarTutoria(dto, matricula)));
    }

    @PreAuthorize("hasAnyRole('TUTORADO','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<?> buscarPorMatriculaONombre(@RequestParam String valor) {
        if (valor.matches("^[a-zA-Z]\\d{8}$")) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Tutorias Disponibles",
                    tutoriasService.findTutoriasPorMatriculaTutor(valor)));
        } else {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Tutorias Disponibles", tutoriasService.findTutoriasPorNombreTutor(valor)));
        }
    }

    @PreAuthorize("hasAnyRole('TUTOR','ADMIN')")
    @PutMapping("/{idTutoria}")
    public ResponseEntity<?> editarTutoria(@PathVariable("idTutoria") int idTutoria,
            @RequestBody ActualizarTutoriaDTO dto, Authentication auth) {

        String matricula = auth.getName();
        return ResponseEntity.ok(new ApiResponse<>(true, "Tutoria editada correctamente ",
                tutoriasService.editarTutoria(idTutoria, dto, matricula)));
    }

    @PreAuthorize("hasAnyRole('TUTOR','ADMIN')")
    @DeleteMapping("/{idTutoria}")
    public ResponseEntity<?> eliminarTutoria(@PathVariable("idTutoria") int idTutoria, Authentication auth) {

        tutoriasService.eliminarTutoria(idTutoria, auth.getName());

        return ResponseEntity.ok(new ApiResponse<>(true, "Tutoria eliminada correctamente ", null));
    }

    @PreAuthorize("hasAnyRole('TUTOR','ADMIN')")
    @PutMapping("/completa/{idTutoria}")
    public ResponseEntity<?> tutoriaCompletada(@PathVariable("idTutoria") int idTutoria, Authentication auth) {
        tutoriasService.tutoriaCompletada(idTutoria, auth.getName());

        return ResponseEntity.ok(new ApiResponse<>(true, "Tutoria completa", null));
    }

    @PreAuthorize("hasAnyRole('TUTOR','ADMIN')")
    @PutMapping("/cancelar/{idTutoria}")
    public ResponseEntity<?> cancelarTutoria(@PathVariable("idTutoria") int idTutoria, Authentication auth) {
        tutoriasService.cancelarTutoria(idTutoria, auth.getName());

        return ResponseEntity.ok(new ApiResponse<>(true, "Has cancelado la tutoria.", null));
    }

    @PreAuthorize("hasAnyRole('TUTOR','ADMIN')")
    @GetMapping("/inscritos/{idTutoria}")
    public ResponseEntity<?> verInscritos(@PathVariable("idTutoria") int idTutoria) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Tutorado inscritos:", tutoriasService.mostrarTutoradosInscritos(idTutoria)));
    }
}
