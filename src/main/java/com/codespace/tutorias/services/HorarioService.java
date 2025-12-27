package com.codespace.tutorias.services;

import com.codespace.tutorias.DTO.CrearHorarioDTO;
import com.codespace.tutorias.DTO.HorariosDTO;
import com.codespace.tutorias.DTO.HorariosMostrarDTO;
import com.codespace.tutorias.Helpers.DateHelper;
import com.codespace.tutorias.Mapping.HorarioMapping;
import com.codespace.tutorias.exceptions.BusinessException;
import com.codespace.tutorias.models.Horario;
import com.codespace.tutorias.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioService {
    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private HorarioMapping horarioMapping;

    public List<HorariosMostrarDTO> listarHorariosPublicos() {
        return horarioRepository.findAll()
                .stream()
                .map(horarioMapping::convertirAPublica)
                .toList();
    }

    public Optional<HorariosMostrarDTO> buscarHorarioPublico(int id) {
        return horarioRepository.findById(id)
                .map(horarioMapping::convertirAPublica);
    }

    public HorariosDTO crearHorario(CrearHorarioDTO dto, String matricula) {
        Horario horario = horarioMapping.convertirANuevaEntidad(dto, matricula);

        List<Horario> horariosTutor = findByTutor(matricula);

        for(Horario h: horariosTutor){
            if(horario.getDia().equals(h.getDia()) &&
                    DateHelper.haySolapamiento(h.getHoraInicio(), h.getHoraFin(),
                            horario.getHoraInicio(), horario.getHoraFin())){
                throw new BusinessException("Ya tienes un horario con estos datos.");
            }
        }
        return horarioMapping.convertirADTO(horarioRepository.save(horario));
    }

    public Optional<Horario> actualizarHorario(int id, HorariosDTO dto) {
        return horarioRepository.findById(id)
                .map(existing -> {
                    var entidad = horarioMapping.convertirAEntidad(dto);
                    entidad.setIdHorario(id);
                    return horarioRepository.save(entidad);
                });
    }

    public void eliminarHorario(int id, String matricula) {
        Horario horario = horarioRepository.findByIdHorarioAndTutor_Matricula(id, matricula)
                .orElseThrow(() -> new BusinessException("No se encontró el horario o no tienes permiso para eliminarlo."));

        horarioRepository.delete(horario);
    }


    private List<Horario> findByTutor(String matricula){
        return horarioRepository.findByTutor(matricula);
    }

    public List<HorariosMostrarDTO> misHorarios(String matricula){
        return horarioRepository.findByTutor(matricula)
                .stream()
                .map(horarioMapping::convertirAPublica)
                .toList();

    }
}