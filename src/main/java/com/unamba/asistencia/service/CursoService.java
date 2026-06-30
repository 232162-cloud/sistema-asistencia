package com.unamba.asistencia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.model.Usuario;
import com.unamba.asistencia.repository.CursoRepository;
import com.unamba.asistencia.repository.UsuarioRepository;

@Service
public class CursoService {

    private final CursoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public CursoService(CursoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Curso> listar() {
        return repository.findAll();
    }

    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    public Curso guardar(Curso curso, Long docenteId) {
        asignarDocente(curso, docenteId);
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
            if (cursoActualizado.getDocente() != null) {
                c.setDocente(cursoActualizado.getDocente());
            }
            return repository.save(c);
        }
        return null;
    }

    public Curso actualizar(Long id, Curso cursoActualizado, Long docenteId) {
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
            if (docenteId != null) {
                asignarDocente(c, docenteId);
            }
            return repository.save(c);
        }
        return null;
    }

    private void asignarDocente(Curso curso, Long docenteId) {
        if (docenteId == null) {
            return;
        }
        Optional<Usuario> usuario = usuarioRepository.findById(docenteId);
        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado");
        }
        Usuario docente = usuario.get();
        if (!"DOCENTE".equals(docente.getRol())) {
            throw new IllegalArgumentException("Usuario no tiene rol DOCENTE");
        }
        curso.setDocente(docente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
