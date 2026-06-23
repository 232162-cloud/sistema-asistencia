package com.unamba.asistencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.repository.EstudianteRepository;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository repository;

    public List<Estudiante> listar() {

        return repository.findAll();
    }

    public Estudiante guardar(
            Estudiante estudiante) {

        return repository.save(estudiante);
    }
}