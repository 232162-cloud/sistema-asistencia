package com.unamba.asistencia.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Asistencia>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asistencia> obtenerPorId(@PathVariable Long id) {
        Optional<Asistencia> asistencia = service.obtenerPorId(id);
        return asistencia.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Asistencia>> buscar(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long estudianteId,
            @RequestParam(required = false) Long cursoId) {

        if (estudianteId != null && cursoId != null) {
            Optional<Estudiante> estudiante = estudianteRepository.findById(estudianteId);
            Optional<Curso> curso = cursoRepository.findById(cursoId);
            if (estudiante.isPresent() && curso.isPresent()) {
                return ResponseEntity.ok(service.buscarPorEstudianteYCurso(estudiante.get(), curso.get()));
            }
            return ResponseEntity.ok(List.of());
        }
        if (estudianteId != null) {
            return estudianteRepository.findById(estudianteId)
                    .map(estudiante -> ResponseEntity.ok(service.buscarPorEstudiante(estudiante)))
                    .orElseGet(() -> ResponseEntity.ok(List.of()));
        }
        if (cursoId != null) {
            return cursoRepository.findById(cursoId)
                    .map(curso -> ResponseEntity.ok(service.buscarPorCurso(curso)))
                    .orElseGet(() -> ResponseEntity.ok(List.of()));
        }
        if (fecha != null && !fecha.isEmpty()) {
            try {
                LocalDate fechaBuscar = LocalDate.parse(fecha);
                return ResponseEntity.ok(service.buscarPorFecha(fechaBuscar));
            } catch (Exception e) {
                return ResponseEntity.ok(service.listar());
            }
        }
        if (estado != null && !estado.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorEstado(estado));
        }
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    public ResponseEntity<Asistencia> guardar(@RequestBody AsistenciaRequest request) {
        Optional<Estudiante> estudiante = estudianteRepository.findById(request.estudianteId());
        Optional<Curso> curso = cursoRepository.findById(request.cursoId());
        if (estudiante.isEmpty() || curso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    public ResponseEntity<Asistencia> actualizar(@PathVariable Long id, @RequestBody AsistenciaRequest request) {
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
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Asistencia> asistencia = service.obtenerPorId(id);
        if (asistencia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private record AsistenciaRequest(Long estudianteId, Long cursoId, LocalDate fecha, String estado, String observaciones) {
    }
}
