package com.codespace.tutorias.Mapping;

import com.codespace.tutorias.DTO.TutorDTO;
import com.codespace.tutorias.DTO.TutoresPublicosDTO;
import com.codespace.tutorias.models.EnumRol;
import com.codespace.tutorias.models.Tutor;
import org.springframework.stereotype.Component;

@Component
public class TutorMapping {
    public TutoresPublicosDTO convertirAFront(Tutor entidad){
        TutoresPublicosDTO dto = new TutoresPublicosDTO();
        dto.setMatricula(entidad.getMatricula());
        dto.setNombre(entidad.getNombre());
        dto.setApellidoP(entidad.getApellidoP());
        dto.setApellidoM(entidad.getApellidoM());
        dto.setCorreo(entidad.getCorreo());
        return dto;
    }

    public TutorDTO convertirADTO(Tutor entidad) {
        TutorDTO dto = new TutorDTO();
        dto.setMatricula(entidad.getMatricula());
        dto.setNombre(entidad.getNombre());
        dto.setApellidoP(entidad.getApellidoP());
        dto.setApellidoM(entidad.getApellidoM());
        dto.setCorreo(entidad.getCorreo());
        dto.setPassword(entidad.getPassword());
        dto.setRol(entidad.getRol().toString());
        return dto;
    }

    public Tutor convertirAEntidad(TutorDTO dto){
        Tutor entidad = new Tutor();
        entidad.setMatricula(dto.getMatricula());
        entidad.setNombre(dto.getNombre());
        entidad.setApellidoP(dto.getApellidoP());
        entidad.setApellidoM(dto.getApellidoM());
        entidad.setCorreo(dto.getCorreo());
        entidad.setPassword(dto.getPassword());
        entidad.setRol(Enum.valueOf(EnumRol.class, dto.getRol()));
        return entidad;
    }
}
