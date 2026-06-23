package com.unamba.asistenciaapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unamba.asistenciaapi.model.Curso;
import com.unamba.asistenciaapi.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    public List<Curso> listar() {
        return repository.findAll();
    }

    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    public Optional<Curso> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Curso actualizar(Long id, Curso cursoActualizado) {
        return repository.findById(id).map(c -> {
            c.setCodigo(cursoActualizado.getCodigo());
            c.setNombre(cursoActualizado.getNombre());
            c.setDescripcion(cursoActualizado.getDescripcion());
            c.setCreditos(cursoActualizado.getCreditos());
            c.setProfesor(cursoActualizado.getProfesor());
            c.setCiclo(cursoActualizado.getCiclo());
            return repository.save(c);
        }).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
