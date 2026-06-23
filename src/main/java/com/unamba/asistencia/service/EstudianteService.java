package com.unamba.asistencia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.repository.EstudianteRepository;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public List<Estudiante> listar() {

        return repository.findAll();
    }

    public Estudiante guardar(
            Estudiante estudiante) {

        return repository.save(estudiante);
    }
}
