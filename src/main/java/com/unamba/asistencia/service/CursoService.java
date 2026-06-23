package com.unamba.asistencia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.repository.CursoRepository;

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

    public List<Curso> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Curso> buscarPorProfesor(String profesor) {
        return repository.findByProfesorContainingIgnoreCase(profesor);
    }

    public List<Curso> buscarPorCiclo(String ciclo) {
        return repository.findByCicloContainingIgnoreCase(ciclo);
    }

    public List<Curso> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    public Curso actualizar(Long id, Curso cursoActualizado) {
        Optional<Curso> curso = repository.findById(id);
        if (curso.isPresent()) {
            Curso c = curso.get();
            if (cursoActualizado.getCodigo() != null) {
                c.setCodigo(cursoActualizado.getCodigo());
            }
            if (cursoActualizado.getNombre() != null) {
                c.setNombre(cursoActualizado.getNombre());
            }
            if (cursoActualizado.getDescripcion() != null) {
                c.setDescripcion(cursoActualizado.getDescripcion());
            }
            if (cursoActualizado.getCreditos() != null) {
                c.setCreditos(cursoActualizado.getCreditos());
            }
            if (cursoActualizado.getProfesor() != null) {
                c.setProfesor(cursoActualizado.getProfesor());
            }
            if (cursoActualizado.getCiclo() != null) {
                c.setCiclo(cursoActualizado.getCiclo());
            }
            return repository.save(c);
        }
        return null;
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
