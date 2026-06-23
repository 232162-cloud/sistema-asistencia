package com.unamba.asistencia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unamba.asistencia.model.Curso;

public interface CursoRepository
        extends JpaRepository<Curso, Long> {

    List<Curso> findByNombreContainingIgnoreCase(String nombre);
    
    List<Curso> findByProfesorContainingIgnoreCase(String profesor);
    
    List<Curso> findByCicloContainingIgnoreCase(String ciclo);
    
    List<Curso> findByCodigo(String codigo);

}
