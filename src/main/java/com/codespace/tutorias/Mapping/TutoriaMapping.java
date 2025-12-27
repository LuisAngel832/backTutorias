package com.codespace.tutorias.Mapping;

import com.codespace.tutorias.DTO.*;
import com.codespace.tutorias.exceptions.BusinessException;
import com.codespace.tutorias.models.*;
import com.codespace.tutorias.repository.HorarioRepository;
import com.codespace.tutorias.repository.MateriaRepository;
import com.codespace.tutorias.repository.TutoriasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TutoriaMapping {

    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private TutoriasRepository tutoriasRepository;

    public Tutoria convertirAEntidad(TutoriasDTO dto) {
        Tutoria entidad = new Tutoria();
        entidad.setIdTutoria(dto.getIdTutoria());

        Horario horario = horarioRepository.findById(dto.getHorario().getIdHorario()).orElseThrow();
        entidad.setHorario(horario);

        entidad.setFecha(dto.getFecha());
        entidad.setEdificio(dto.getEdificio());
        entidad.setAula(dto.getAula());


        entidad.setTutoriasTutorados(List.of());

        entidad.setEstado(dto.getEstado());

        if (dto.getMateria() == null) {
            throw new BusinessException("Debe proporcionar una materia válida");
        }else {
            Materia materia = materiaRepository.findById(dto.getMateria().getNrc())
                    .orElseThrow(() -> new BusinessException("Materia no encontrada con NRC: " + dto.getMateria().getNrc()));

            entidad.setMateria(materia);
        }

        return entidad;
    }

    public Tutoria convertirANuevaEntidad(CrearTutoriaDTO dto){
        Tutoria entidad = new Tutoria();

        Horario horario = horarioRepository.findById(dto.getIdHorario())
                .orElseThrow(() -> new BusinessException("Horario no existente."));
        Materia materia = materiaRepository.findById(dto.getNrcMateria())
                .orElseThrow(() -> new BusinessException("Materia no encontrada."));

        entidad.setHorario(horario);
        entidad.setTutoriasTutorados(List.of());
        entidad.setMateria(materia);
        entidad.setFecha(dto.getFecha());
        entidad.setEdificio(dto.getEdificio());
        entidad.setAula(dto.getAula());
        entidad.setEstado("PENDIENTE");

        return entidad;
    }

    public Tutoria convertirEdicion(int id, ActualizarTutoriaDTO dto){
        Tutoria entidad = tutoriasRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Tutoria no existente."));

        entidad.setHorario(horarioRepository.findById(dto.getIdHorario())
                .orElseThrow(() -> new BusinessException("Horario no existente.")));

        entidad.setFecha(dto.getFecha());
        entidad.setEdificio(dto.getEdificio());
        entidad.setAula(dto.getAula());
        entidad.setEstado(dto.getEstado());
        entidad.setMateria(materiaRepository.findById(dto.getNrcMateria())
                .orElseThrow(() -> new BusinessException("Materia no existente.")));

        return entidad;
    }

    public TutoriasDTO convertirADTO(Tutoria entidad){
        TutoriasDTO dto = new TutoriasDTO();
        dto.setIdTutoria(entidad.getIdTutoria());

        HorariosDTO horariosDTO = new HorariosDTO();
        horariosDTO.setIdHorario(entidad.getHorario().getIdHorario());
        horariosDTO.setDia(entidad.getHorario().getDia());
        horariosDTO.setHoraInicio(entidad.getHorario().getHoraInicio());
        horariosDTO.setHoraFin(entidad.getHorario().getHoraFin());

        TutorDTO tutoresDTO = new TutorDTO();
        tutoresDTO.setMatricula(entidad.getHorario().getTutor().getMatricula());
        tutoresDTO.setNombre(entidad.getHorario().getTutor().getNombre());
        tutoresDTO.setApellidoP(entidad.getHorario().getTutor().getApellidoP());
        tutoresDTO.setApellidoM(entidad.getHorario().getTutor().getApellidoM());
        tutoresDTO.setCorreo(entidad.getHorario().getTutor().getCorreo());
        tutoresDTO.setPassword(entidad.getHorario().getTutor().getPassword());

        horariosDTO.setTutor(tutoresDTO);

        dto.setHorario(horariosDTO);
        dto.setFecha(entidad.getFecha());
        dto.setEdificio(entidad.getEdificio());
        dto.setAula(entidad.getAula());

        List<TutoradoDTO> tutoradosDTO = entidad.getTutoriasTutorados().stream()
                .map(t -> {
                    TutoradoDTO dtoT = new TutoradoDTO();
                    dtoT.setMatricula(t.getTutorado().getMatricula());
                    dtoT.setNombre(t.getTutorado().getNombre());
                    dtoT.setApellidoP(t.getTutorado().getApellidoP());
                    dtoT.setApellidoM(t.getTutorado().getApellidoM());
                    dtoT.setCorreo(t.getTutorado().getCorreo());
                    dtoT.setPassword(t.getTutorado().getPassword());
                    return dtoT;
                }).toList();


        dto.setTutorados(tutoradosDTO);
        dto.setEstado(entidad.getEstado());

        MateriaDTO materiaDTO = new MateriaDTO();
        materiaDTO.setNrc(entidad.getMateria().getNrc());
        materiaDTO.setNombreMateria(entidad.getMateria().getNombreMateria());

        dto.setMateria(materiaDTO);
        return dto;
    }

    public TutoriasPublicasDTO convertirAPublicas(Tutoria entidad){
        TutoriasPublicasDTO dto = new TutoriasPublicasDTO();
        dto.setIdTutoria(entidad.getIdTutoria());

        HorariosPublicosDTO horariosDTO = new HorariosPublicosDTO();
        horariosDTO.setIdHorario(entidad.getHorario().getIdHorario());
        horariosDTO.setDia(entidad.getHorario().getDia());
        horariosDTO.setHoraInicio(entidad.getHorario().getHoraInicio());
        horariosDTO.setHoraFin(entidad.getHorario().getHoraFin());

        TutoresPublicosDTO tutoresDTO = new TutoresPublicosDTO();
        tutoresDTO.setMatricula(entidad.getHorario().getTutor().getMatricula());
        tutoresDTO.setNombre(entidad.getHorario().getTutor().getNombre());
        tutoresDTO.setApellidoP(entidad.getHorario().getTutor().getApellidoP());
        tutoresDTO.setApellidoM(entidad.getHorario().getTutor().getApellidoM());
        tutoresDTO.setCorreo(entidad.getHorario().getTutor().getCorreo());

        horariosDTO.setTutor(tutoresDTO);

        dto.setHorario(horariosDTO);
        dto.setFecha(entidad.getFecha());
        dto.setEdificio(entidad.getEdificio());
        dto.setAula(entidad.getAula());

        List<TutoradosPublicosDTO> tutoradosDTO = entidad.getTutoriasTutorados().stream()
                .map(t -> {
                    TutoradosPublicosDTO dtoT = new TutoradosPublicosDTO();
                    dtoT.setMatricula(t.getTutorado().getMatricula());
                    dtoT.setNombre(t.getTutorado().getNombre());
                    dtoT.setApellidoP(t.getTutorado().getApellidoP());
                    dtoT.setApellidoM(t.getTutorado().getApellidoM());
                    dtoT.setCorreo(t.getTutorado().getCorreo());
                    return dtoT;
                }).toList();

        dto.setTutorados(tutoradosDTO);
        dto.setEstado(entidad.getEstado());

        MateriaDTO materiaDTO = new MateriaDTO();
        materiaDTO.setNrc(entidad.getMateria().getNrc());
        materiaDTO.setNombreMateria(entidad.getMateria().getNombreMateria());

        dto.setMateria(materiaDTO);

        return dto;
    }
}
