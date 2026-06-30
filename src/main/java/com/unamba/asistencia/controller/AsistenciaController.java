package com.unamba.asistencia.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.asistencia.model.Asistencia;
import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.repository.CursoRepository;
import com.unamba.asistencia.repository.EstudianteRepository;
import com.unamba.asistencia.service.AsistenciaService;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService service;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    public AsistenciaController(AsistenciaService service,
                                EstudianteRepository estudianteRepository,
                                CursoRepository cursoRepository) {
        this.service = service;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ALUMNO')")
    public ResponseEntity<List<Asistencia>> listar() {
        String username = getAuthenticatedUsername();
        boolean isAdmin = hasRole("ADMIN");
        boolean isDocente = hasRole("DOCENTE");
        boolean isAlumno = hasRole("ALUMNO");

        List<Asistencia> asistencias = service.listar();
        if (isAdmin) {
            return ResponseEntity.ok(asistencias);
        }
        return ResponseEntity.ok(filterByIdentity(asistencias, username, isDocente, isAlumno));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ALUMNO')")
    public ResponseEntity<Asistencia> obtenerPorId(@PathVariable Long id) {
        Optional<Asistencia> asistencia = service.obtenerPorId(id);
        if (asistencia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (isAuthorizedForAsistencia(asistencia.get())) {
            return ResponseEntity.ok(asistencia.get());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ALUMNO')")
    public ResponseEntity<List<Asistencia>> buscar(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long estudianteId,
            @RequestParam(required = false) Long cursoId) {

        String username = getAuthenticatedUsername();
        boolean isAdmin = hasRole("ADMIN");
        boolean isDocente = hasRole("DOCENTE");
        boolean isAlumno = hasRole("ALUMNO");

        List<Asistencia> asistencias;
        if (estudianteId != null && cursoId != null) {
            Optional<Estudiante> estudiante = estudianteRepository.findById(estudianteId);
            Optional<Curso> curso = cursoRepository.findById(cursoId);
            if (estudiante.isPresent() && curso.isPresent()) {
                asistencias = service.buscarPorEstudianteYCurso(estudiante.get(), curso.get());
            } else {
                return ResponseEntity.ok(List.of());
            }
        } else if (estudianteId != null) {
            asistencias = estudianteRepository.findById(estudianteId)
                    .map(service::buscarPorEstudiante)
                    .orElse(List.of());
        } else if (cursoId != null) {
            asistencias = cursoRepository.findById(cursoId)
                    .map(service::buscarPorCurso)
                    .orElse(List.of());
        } else if (fecha != null && !fecha.isEmpty()) {
            try {
                LocalDate fechaBuscar = LocalDate.parse(fecha);
                asistencias = service.buscarPorFecha(fechaBuscar);
            } catch (Exception e) {
                asistencias = service.listar();
            }
        } else if (estado != null && !estado.isEmpty()) {
            asistencias = service.buscarPorEstado(estado);
        } else {
            asistencias = service.listar();
        }

        if (isAdmin) {
            return ResponseEntity.ok(asistencias);
        }
        return ResponseEntity.ok(filterByIdentity(asistencias, username, isDocente, isAlumno));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<Asistencia> guardar(@RequestBody AsistenciaRequest request) {
        String username = getAuthenticatedUsername();
        boolean isDocente = hasRole("DOCENTE");

        Optional<Estudiante> estudiante = estudianteRepository.findById(request.estudianteId());
        Optional<Curso> curso = cursoRepository.findById(request.cursoId());
        if (estudiante.isEmpty() || curso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (isDocente && !isDocenteOfCourse(curso.get(), username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Asistencia asistencia = new Asistencia();
        asistencia.setEstudiante(estudiante.get());
        asistencia.setCurso(curso.get());
        asistencia.setFecha(request.fecha());
        asistencia.setEstado(request.estado());
        asistencia.setObservaciones(request.observaciones());
        Asistencia guardada = service.guardar(asistencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<Asistencia> actualizar(@PathVariable Long id, @RequestBody AsistenciaRequest request) {
        String username = getAuthenticatedUsername();
        boolean isDocente = hasRole("DOCENTE");

        Optional<Asistencia> existente = service.obtenerPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (isDocente) {
            Curso cursoAValidar = existente.get().getCurso();
            if (request.cursoId() != null) {
                Optional<Curso> cursoNuevo = cursoRepository.findById(request.cursoId());
                if (cursoNuevo.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                cursoAValidar = cursoNuevo.get();
            }
            if (!isDocenteOfCourse(cursoAValidar, username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        Asistencia asistenciaActualizada = new Asistencia();
        if (request.estudianteId() != null) {
            Optional<Estudiante> estudiante = estudianteRepository.findById(request.estudianteId());
            if (estudiante.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            asistenciaActualizada.setEstudiante(estudiante.get());
        }
        if (request.cursoId() != null) {
            Optional<Curso> curso = cursoRepository.findById(request.cursoId());
            if (curso.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            asistenciaActualizada.setCurso(curso.get());
        }
        asistenciaActualizada.setFecha(request.fecha());
        asistenciaActualizada.setEstado(request.estado());
        asistenciaActualizada.setObservaciones(request.observaciones());

        Asistencia actualizado = service.actualizar(id, asistenciaActualizada);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Asistencia> asistencia = service.obtenerPorId(id);
        if (asistencia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_" + role));
    }

    private boolean isDocenteOfCourse(Curso curso, String username) {
        return curso != null && curso.getDocente() != null && username != null
                && username.equals(curso.getDocente().getUsername());
    }

    private boolean isAlumnoOfAsistencia(Asistencia asistencia, String username) {
        return asistencia != null
                && asistencia.getEstudiante() != null
                && asistencia.getEstudiante().getUsuario() != null
                && username != null
                && username.equals(asistencia.getEstudiante().getUsuario().getUsername());
    }

    private boolean isDocenteOfAsistencia(Asistencia asistencia, String username) {
        return asistencia != null
                && asistencia.getCurso() != null
                && asistencia.getCurso().getDocente() != null
                && username != null
                && username.equals(asistencia.getCurso().getDocente().getUsername());
    }

    private boolean isAuthorizedForAsistencia(Asistencia asistencia) {
        String username = getAuthenticatedUsername();
        if (hasRole("ADMIN")) {
            return true;
        }
        if (hasRole("DOCENTE")) {
            return isDocenteOfAsistencia(asistencia, username);
        }
        if (hasRole("ALUMNO")) {
            return isAlumnoOfAsistencia(asistencia, username);
        }
        return false;
    }

    private List<Asistencia> filterByIdentity(List<Asistencia> asistencias, String username, boolean isDocente, boolean isAlumno) {
        if (isDocente) {
            return asistencias.stream()
                    .filter(asistencia -> isDocenteOfAsistencia(asistencia, username))
                    .toList();
        }
        if (isAlumno) {
            return asistencias.stream()
                    .filter(asistencia -> isAlumnoOfAsistencia(asistencia, username))
                    .toList();
        }
        return List.of();
    }

    private record AsistenciaRequest(Long estudianteId, Long cursoId, LocalDate fecha, String estado, String observaciones) {
    }
}
