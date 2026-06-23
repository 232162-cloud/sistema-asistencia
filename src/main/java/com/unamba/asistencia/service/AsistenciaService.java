package com.unamba.asistencia.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unamba.asistencia.model.Asistencia;
import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.repository.AsistenciaRepository;

@Service
public class AsistenciaService {

    private final AsistenciaRepository repository;

    public AsistenciaService(AsistenciaRepository repository) {
        this.repository = repository;
    }

    public List<Asistencia> listar() {
        return repository.findAll();
    }

    public Asistencia guardar(Asistencia asistencia) {
        return repository.save(asistencia);
    }

    public Optional<Asistencia> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public List<Asistencia> buscarPorEstudiante(Estudiante estudiante) {
        return repository.findByEstudiante(estudiante);
    }

    public List<Asistencia> buscarPorCurso(Curso curso) {
        return repository.findByCurso(curso);
    }

    public List<Asistencia> buscarPorFecha(LocalDate fecha) {
        return repository.findByFecha(fecha);
    }

    public List<Asistencia> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public List<Asistencia> buscarPorEstudianteYCurso(Estudiante estudiante, Curso curso) {
        return repository.findByEstudianteAndCurso(estudiante, curso);
    }

    public Asistencia actualizar(Long id, Asistencia asistenciaActualizada) {
        Optional<Asistencia> asistencia = repository.findById(id);
        if (asistencia.isPresent()) {
            Asistencia a = asistencia.get();
            if (asistenciaActualizada.getEstudiante() != null) {
                a.setEstudiante(asistenciaActualizada.getEstudiante());
            }
            if (asistenciaActualizada.getCurso() != null) {
                a.setCurso(asistenciaActualizada.getCurso());
            }
            if (asistenciaActualizada.getFecha() != null) {
                a.setFecha(asistenciaActualizada.getFecha());
            }
            if (asistenciaActualizada.getEstado() != null) {
                a.setEstado(asistenciaActualizada.getEstado());
            }
            if (asistenciaActualizada.getObservaciones() != null) {
                a.setObservaciones(asistenciaActualizada.getObservaciones());
            }
            return repository.save(a);
        }
        return null;
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
