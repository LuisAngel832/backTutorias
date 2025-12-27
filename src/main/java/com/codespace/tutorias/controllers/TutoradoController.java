package com.codespace.tutorias.controllers;

import com.codespace.tutorias.DTO.TutoradoDTO;
import com.codespace.tutorias.DTO.TutoradosPublicosDTO;
import com.codespace.tutorias.exceptions.ApiResponse;
import com.codespace.tutorias.services.TutoradoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.codespace.tutorias.DTO.NotificacionesDto;

import java.util.List;

@RestController
@RequestMapping("/tutorados")
public class TutoradoController {

    @Autowired
    private TutoradoService tutoradoService;

    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping
    public List<TutoradosPublicosDTO> getTutorados() {
        return tutoradoService.listarTutoradosPublicos();
    }

    @PreAuthorize("hasRole('TUTOR')")
    @PostMapping
    public TutoradoDTO registrarTutorado(@Valid @RequestBody TutoradoDTO dto) {
        return tutoradoService.crearTutorados(dto);
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<TutoradosPublicosDTO> getTutorado(@PathVariable String matricula) {
        return tutoradoService.buscarTutoradoPublico(matricula).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TUTORADO')")
    @GetMapping("/me/tutorias")
    public ResponseEntity<?> misTutorias(Authentication authentication) {
        String matricula = authentication.getName();
        return ResponseEntity.ok(new ApiResponse<>(true, "Mis tutorias",
                tutoradoService.findMisTutorias(matricula)));
    }

    @PostMapping("/me/tutorias/{id}")
    @PreAuthorize("hasRole('TUTORADO')")
    public ResponseEntity<?> inscribirATutoria(@PathVariable("id") int idTutoria, Authentication authentication) {
        String matricula = authentication.getName();
        tutoradoService.inscribirATutoria(matricula, idTutoria);
        return ResponseEntity.ok(new ApiResponse<>(true, "Te has inscrito", null));
    }

    @DeleteMapping("/me/tutorias/{id}")
    @PreAuthorize("hasRole('TUTORADO')")
    public ResponseEntity<?> cancelarInscripcion(@PathVariable("id") int idTutoria, Authentication authentication) {

        String matricula = authentication.getName();

        tutoradoService.cancelarInscripcion(matricula, idTutoria);
        return ResponseEntity.ok(new ApiResponse<>(true, "Has cancelado tu inscripción a esta tutoria.", null));
    }

    @PutMapping("/me/notificaciones")
    @PreAuthorize("hasRole('TUTORADO')")
    public ResponseEntity<?> actualizarNotificaciones(
            Authentication authentication,
            @RequestBody NotificacionesDto dto) {

        if (dto.getNotificarme() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Valor no proporcionado", null));
        }

        tutoradoService.actualizarNotificaciones(
                authentication.getName(),
                dto.getNotificarme());

        String msg = dto.getNotificarme()
                ? "Notificaciones activadas"
                : "Notificaciones desactivadas";

        return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
    }

    @GetMapping("/me/tutorias-canceladas")
    @PreAuthorize("hasRole('TUTORADO')")
    public ResponseEntity<?> misTutoriasCanceladas(Authentication authentication) {
        return ResponseEntity
                .ok(new ApiResponse<>(true,
                        "Tutorias Canceladas",
                        tutoradoService.misTutoriasCanceladas(authentication.getName())));
    }

}
