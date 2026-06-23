package com.unamba.asistenciaapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unamba.asistenciaapi.model.Asistencia;
import com.unamba.asistenciaapi.repository.AsistenciaRepository;

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

    public Asistencia actualizar(Long id, Asistencia asistenciaActualizada) {
        return repository.findById(id).map(a -> {
            a.setEstudiante(asistenciaActualizada.getEstudiante());
            a.setCurso(asistenciaActualizada.getCurso());
            a.setFecha(asistenciaActualizada.getFecha());
            a.setEstado(asistenciaActualizada.getEstado());
            a.setObservaciones(asistenciaActualizada.getObservaciones());
            return repository.save(a);
        }).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
