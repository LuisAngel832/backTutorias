package com.codespace.tutorias.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.codespace.tutorias.JWT.JWTUtil;
import com.codespace.tutorias.exceptions.BusinessException;
import com.codespace.tutorias.models.Tutor;
import com.codespace.tutorias.models.Tutorado;
import com.codespace.tutorias.DTO.CambioPasswordDTO;
import com.codespace.tutorias.DTO.CorreoDTO;
import com.codespace.tutorias.DTO.LoginDTO;
import com.codespace.tutorias.DTO.LoginResponse;
import com.codespace.tutorias.DTO.RegisterDto;
import com.codespace.tutorias.DTO.TutorDTO;
import com.codespace.tutorias.DTO.TutoradoDTO;
import com.codespace.tutorias.repository.TutorRepository;
import com.codespace.tutorias.repository.TutoradoRepository;

@Service
public class AuthService {

    @Autowired
    private TutorService tutorService;
    @Autowired
    private TutoradoService tutoradoService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private TutoradoRepository tutoradoRepository;
    @Autowired
    private TutorRepository tutorRepository;

    public LoginResponse login(LoginDTO dto) {

        Optional<TutorDTO> tutor = tutorService.buscarTutorPrivado(dto.getMatricula());

        if (tutor.isPresent()) {
            TutorDTO t = tutor.get();

            if (t.getRol().equals("ADMIN") && passwordEncoder.matches(dto.getPassword(), t.getPassword())) {

                String token = jwtUtil.generateToken(t.getMatricula(), "ADMIN");

                return new LoginResponse(token, "admin", t.getNombre(), t.getCorreo());
            }

            if (passwordEncoder.matches(dto.getPassword(), t.getPassword())) {

                String token = jwtUtil.generateToken(t.getMatricula(), "TUTOR");

                return new LoginResponse(token, "tutor", t.getNombre(), t.getCorreo());
            }
        }

        Optional<TutoradoDTO> tutorado = tutoradoService.buscarTutoradoPrivado(dto.getMatricula());

        if (tutorado.isPresent() && passwordEncoder.matches(dto.getPassword(), tutorado.get().getPassword())) {

            TutoradoDTO tut = tutorado.get();

            String token = jwtUtil.generateToken(tut.getMatricula(), "TUTORADO");

            return new LoginResponse(token, "tutorado", tut.getNombre(), tut.getCorreo());
        }

        throw new BusinessException("Usuario o contraseña incorrectos");
    }

    public void recovery(CorreoDTO dto) {
        String correo = dto.getCorreo();

        Tutor tutor = tutorRepository.findByCorreo(correo);

        if (tutor != null) {
            tutorService.mandarCorreoRecuperacion(correo);
            return;
        }

        Tutorado tutorado = tutoradoRepository.findByCorreo(correo);
        if (tutorado != null) {
            tutoradoService.mandarCorreoRecuperacion(correo);
            return;
        }

        throw new BusinessException("Correo no registrado");
    }


    public void newPassword(CambioPasswordDTO dto){
        String token = dto.getToken();
        String nuevaPassword = dto.getPasswordNueva();
        Optional<Tutor> tutor = tutorRepository.findByTokenRecuperacion(token);
        if(tutor.isPresent()){
            tutorService.cambiarPasswordConToken(token, nuevaPassword);
            return;
        }

        Optional<Tutorado> tutorado = tutoradoRepository.findByTokenRecuperacion(token);
        if(tutorado.isPresent()){
            tutoradoService.cambiarPasswordConToken(token, nuevaPassword);
            return;
        }
        throw new BusinessException("Token inválido o expirado");
    }


    public void register(RegisterDto dto) {
        String matricula = dto.getMatricula();

        Optional<TutorDTO> tutorOptional = tutorService.buscarTutorPrivado(matricula);
        Optional<TutoradoDTO> tutoradoOptional = tutoradoService.buscarTutoradoPrivado(matricula);

        if (tutorOptional.isPresent() || tutoradoOptional.isPresent()) {
            throw new BusinessException("La matrícula ya está registrada");
        }

        String passwordEncripted = passwordEncoder.encode(dto.getPassword());

        switch (dto.getRol().toUpperCase()) {
            case "TUTOR":
                    Tutor tutor = new Tutor();
                    tutor.setMatricula(dto.getMatricula());
                    tutor.setNombre(dto.getNombre());
                    tutor.setApellidoP(dto.getApellidoP());
                    tutor.setApellidoM(dto.getApellidoM());
                    tutor.setCorreo(dto.getCorreo());
                    tutor.setPassword(passwordEncripted);
                    tutorRepository.save(tutor);
                break;

            case "TUTORADO":
                    Tutorado tutorado = new Tutorado();
                    tutorado.setMatricula(dto.getMatricula());
                    tutorado.setNombre(dto.getNombre());
                    tutorado.setApellidoP(dto.getApellidoP());
                    tutorado.setApellidoM(dto.getApellidoM());
                    tutorado.setCorreo(dto.getCorreo());
                    tutorado.setPassword(passwordEncripted);
                    tutoradoRepository.save(tutorado);
                break;
        
            default:
                throw new BusinessException("Rol inválido");
        }
    }
}
