package com.unamba.asistenciaapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unamba.asistenciaapi.model.Estudiante;
import com.unamba.asistenciaapi.repository.EstudianteRepository;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public List<Estudiante> listar() {
        return repository.findAll();
    }

    public Estudiante guardar(Estudiante estudiante) {
        return repository.save(estudiante);
    }

    public Optional<Estudiante> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Estudiante actualizar(Long id, Estudiante estudianteActualizado) {
        return repository.findById(id).map(e -> {
            e.setCodigo(estudianteActualizado.getCodigo());
            e.setNombres(estudianteActualizado.getNombres());
            e.setApellidos(estudianteActualizado.getApellidos());
            e.setCarrera(estudianteActualizado.getCarrera());
            return repository.save(e);
        }).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
