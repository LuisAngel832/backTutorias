package com.codespace.tutorias.services;

import com.codespace.tutorias.DTO.CambioPasswordDTO;
import com.codespace.tutorias.DTO.TutoradoDTO;
import com.codespace.tutorias.DTO.TutoradosPublicosDTO;
import com.codespace.tutorias.DTO.TutoriasPublicasDTO;
import com.codespace.tutorias.Helpers.DateHelper;
import com.codespace.tutorias.Mapping.TutoradoMapping;
import com.codespace.tutorias.Mapping.TutoriaMapping;
import com.codespace.tutorias.exceptions.BusinessException;
import com.codespace.tutorias.models.*;
import com.codespace.tutorias.repository.TutoradoRepository;

import com.codespace.tutorias.repository.TutoriaTutoradoRepository;
import com.codespace.tutorias.repository.TutoriasRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TutoradoService {

    @Autowired
    private TutoradoRepository tutoradoRepository;
    @Autowired
    private TutoradoMapping tutoradoMapping;
    @Autowired
    private TutoriaMapping tutoriaMapping;
    @Autowired
    private TutoriasRepository tutoriasRepository;
    @Autowired
    private TutoriaTutoradoRepository tutoriaTutoradoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


    public List<TutoradoDTO> listarTutoradosPrivados() {
        return tutoradoRepository.findAll().stream()
                .map(tutoradoMapping::convertirADTO).toList();
    }

    public List<TutoradosPublicosDTO> listarTutoradosPublicos() {
        return tutoradoRepository.findAll().stream()
                .map(tutoradoMapping::convertirAFront).toList();
    }

    public TutoradoDTO crearTutorados(TutoradoDTO dto) {
        if(tutoradoRepository.findById(dto.getMatricula()).isPresent()){
            throw new BusinessException("Ya existe un tutorado con esa matricula asignada.");
        }
        if(tutoradoRepository.findByCorreo(dto.getCorreo()) != null){
            throw new BusinessException("Ya existe un tutorado con ese correo electronico");
        }
        Tutorado tutorado = tutoradoMapping.convertirAEntidad(dto);
        tutorado.setPassword(passwordEncoder.encode(tutorado.getPassword()));
        return tutoradoMapping.convertirADTO(tutoradoRepository.save(tutorado));
    }

    public void eliminarTutorado(String id){
        tutoradoRepository.deleteById(id);
    }

    public Optional<TutoradosPublicosDTO> buscarTutoradoPublico(String id){
        return tutoradoRepository.findById(id).map(tutoradoMapping::convertirAFront);
    }

    public Optional<TutoradoDTO> buscarTutoradoPrivado(String id){
        return tutoradoRepository.findById(id).map(tutoradoMapping::convertirADTO);
    }

    public List<TutoriasPublicasDTO> findMisTutorias(String matricula) {
        return tutoriaTutoradoRepository.findByTutoradoMatricula(matricula).stream()
                .map(TutoriaTutorado::getTutoria)
                .map(tutoriaMapping::convertirAPublicas)
                .toList();
    }

    public void inscribirATutoria(String matricula, int idTutoria){
        Tutoria tutoria = tutoriasRepository.findById(idTutoria)
                .orElseThrow(() ->  new BusinessException("La tutoría no existe."));

        Tutorado tutorado = tutoradoRepository.findById(matricula)
                .orElseThrow(() -> new BusinessException("El tutorado no existe."));

        if(DateHelper.yaComenzo(tutoria.getFecha(), tutoria.getHorario().getHoraInicio())){
            throw new BusinessException("La tutoria ya ha comenzado, ya no puedes inscribirte.");
        }

        TutoriaTutoradoId id = new TutoriaTutoradoId(idTutoria, matricula);
        if (tutoriaTutoradoRepository.existsById(id)) {
            throw new BusinessException("Ya estás inscrito a esta tutoría.");
        }

        long inscritos = tutoriaTutoradoRepository.countByTutoriaIdTutoria(idTutoria);
        if (inscritos >= 5) {
            throw new BusinessException("La tutoría está llena.");
        }

        List<Tutoria> tutoriasInscritas = tutoriaTutoradoRepository.findByTutoradoMatricula(matricula)
                .stream()
                .map(TutoriaTutorado::getTutoria)
                .toList();

        for (Tutoria t : tutoriasInscritas) {
            Horario h = t.getHorario();
            if (h.getDia().equals(tutoria.getHorario().getDia()) &&
                    DateHelper.haySolapamiento(
                            h.getHoraInicio(), h.getHoraFin(),
                            tutoria.getHorario().getHoraInicio(), tutoria.getHorario().getHoraFin())) {
                throw new BusinessException("Ya estás inscrito en otra tutoría con el mismo horario.");
            }
        }

        TutoriaTutorado relacion = new TutoriaTutorado(tutoria, tutorado);
        tutoriaTutoradoRepository.save(relacion);
    }

    public void cancelarInscripcion(String matricula, int idTutoria){
        Tutoria tutoria = tutoriasRepository.findById(idTutoria)
                .orElseThrow(() -> new BusinessException("La tutoría no existe."));
        Tutorado tutorado = tutoradoRepository.findById(matricula)
                .orElseThrow(() -> new BusinessException("El tutorado no existe."));

        if (DateHelper.faltaMenosDe15Minutos(tutoria.getFecha(), tutoria.getHorario().getHoraInicio())) {
            throw new BusinessException("Faltan 15 minutos o menos para que comience la tutoría, ya no puedes cancelar tu inscripción.");
        }

        TutoriaTutoradoId id = new TutoriaTutoradoId(idTutoria, matricula);
        if (!tutoriaTutoradoRepository.existsById(id)) {
            throw new BusinessException("No estás inscrito en esta tutoría.");
        }

        tutoriaTutoradoRepository.deleteById(id);
    }

    public void mandarCorreoRecuperacion(String correo){
        Tutorado tutorado = tutoradoRepository.findByCorreo(correo);

        String token = UUID.randomUUID().toString();
        tutorado.setTokenRecuperacion(token);
        tutorado.setTokenExpiracion(LocalDateTime.now().plusMinutes(15));
        tutoradoRepository.save(tutorado);
        emailService.enviarCorreoRecuperacion(correo, tutorado.getNombre(), token);
    }

    public void cambiarPasswordConToken(String token, String nuevaPassword) {
        Tutorado tutorado = tutoradoRepository.findByTokenRecuperacion(token).orElseThrow(()
                -> new BusinessException("El tutorado no existe."));

        if (tutorado.getTokenExpiracion() == null || tutorado.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token expirado");
        }

        tutorado.setPassword(passwordEncoder.encode(nuevaPassword));
        tutorado.setTokenRecuperacion(null);
        tutorado.setTokenExpiracion(null);

        tutoradoRepository.save(tutorado);
    }


    public void actualizarNotificaciones(String matricula, boolean estado){
        Tutorado tutorado = tutoradoRepository.findById(matricula)
                .orElseThrow(() -> new BusinessException("El tutorado no existe."));

        tutorado.setRecordatorio(estado);

        tutoradoRepository.save(tutorado);
    }

    public List<TutoriasPublicasDTO> misTutoriasCanceladas(String matricula){
        return tutoriasRepository.findTutoriasPorEstadoCancelado(matricula)
                .stream().map(tutoriaMapping::convertirAPublicas)
                .toList();
    }

}
