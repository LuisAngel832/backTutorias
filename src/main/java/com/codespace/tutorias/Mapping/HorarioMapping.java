package com.codespace.tutorias.Mapping;

import com.codespace.tutorias.DTO.*;
import com.codespace.tutorias.exceptions.BusinessException;
import com.codespace.tutorias.models.Horario;
import com.codespace.tutorias.models.Tutor;
import com.codespace.tutorias.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HorarioMapping {

    @Autowired
    private TutorRepository tutorRepository;

    public Horario convertirAEntidad(HorariosDTO dto) {
        Horario entidad = new Horario();
        entidad.setIdHorario(dto.getIdHorario());
        entidad.setDia(dto.getDia());
        entidad.setHoraInicio(dto.getHoraInicio());
        entidad.setHoraFin(dto.getHoraFin());
        return entidad;
    }

    public Horario convertirANuevaEntidad(CrearHorarioDTO dto, String matricula){
        Horario entidad = new Horario();

        entidad.setDia(dto.getDia().toString());
        entidad.setHoraInicio(dto.getHoraInicio());
        entidad.setHoraFin(dto.getHoraFin());

        Tutor tutor = tutorRepository.findById(matricula)
                .orElseThrow(() -> new BusinessException("No existe el tutor."));

        entidad.setTutor(tutor);

        return entidad;
    }

    public HorariosDTO convertirADTO(Horario entidad) {
        HorariosDTO dto = new HorariosDTO();
        dto.setIdHorario(entidad.getIdHorario());
        dto.setDia(entidad.getDia());
        dto.setHoraInicio(entidad.getHoraInicio());
        dto.setHoraFin(entidad.getHoraFin());

        TutorDTO tut = new TutorDTO();
        tut.setMatricula(entidad.getTutor().getMatricula());
        tut.setNombre(entidad.getTutor().getNombre());
        tut.setApellidoP(entidad.getTutor().getApellidoP());
        tut.setApellidoM(entidad.getTutor().getApellidoM());
        tut.setCorreo(entidad.getTutor().getCorreo());
        tut.setPassword(entidad.getTutor().getPassword());
        dto.setTutor(tut);

        return dto;
    }

    public HorariosMostrarDTO convertirAPublica(Horario entidad) {
        HorariosMostrarDTO dto = new HorariosMostrarDTO();
        dto.setIdHorario(entidad.getIdHorario());
        dto.setDia(entidad.getDia());
        dto.setHoraInicio(entidad.getHoraInicio());
        dto.setHoraFin(entidad.getHoraFin());
        return dto;
    }
}
