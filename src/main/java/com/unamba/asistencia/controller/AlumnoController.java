package com.unamba.asistencia.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.asistencia.model.Asistencia;
import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.repository.AsistenciaRepository;
import com.unamba.asistencia.repository.EstudianteRepository;

@RestController
@RequestMapping("/api")
public class AlumnoController {

    private final EstudianteRepository estudianteRepository;
    private final AsistenciaRepository asistenciaRepository;

    public AlumnoController(EstudianteRepository estudianteRepository,
                            AsistenciaRepository asistenciaRepository) {
        this.estudianteRepository = estudianteRepository;
        this.asistenciaRepository = asistenciaRepository;
    }

    @GetMapping("/mis-cursos")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<?> obtenerMisCursos() {
        String username = getAuthenticatedUsername();
        Optional<Estudiante> estudiante = findEstudianteByUsername(username);
        if (estudiante.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró un estudiante vinculado al usuario autenticado."));
        }
        List<Asistencia> asistencias = asistenciaRepository.findByEstudiante(estudiante.get());
        Map<Long, Curso> cursosPorId = new LinkedHashMap<>();
        for (Asistencia asistencia : asistencias) {
            if (asistencia.getCurso() != null && asistencia.getCurso().getId() != null) {
                cursosPorId.putIfAbsent(asistencia.getCurso().getId(), asistencia.getCurso());
            }
        }
        return ResponseEntity.ok(new ArrayList<>(cursosPorId.values()));
    }

    @GetMapping("/mi-asistencia")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<?> obtenerMiAsistencia() {
        String username = getAuthenticatedUsername();
        Optional<Estudiante> estudiante = findEstudianteByUsername(username);
        if (estudiante.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró un estudiante vinculado al usuario autenticado."));
        }
        List<Asistencia> asistencias = asistenciaRepository.findByEstudiante(estudiante.get());
        return ResponseEntity.ok(asistencias);
    }

    private Optional<Estudiante> findEstudianteByUsername(String username) {
        return estudianteRepository.findAll().stream()
                .filter(estudiante -> estudiante.getUsuario() != null)
                .filter(estudiante -> username.equals(estudiante.getUsuario().getUsername()))
                .findFirst();
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
