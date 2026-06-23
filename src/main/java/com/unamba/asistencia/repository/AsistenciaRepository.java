package com.unamba.asistencia.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unamba.asistencia.model.Asistencia;
import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.model.Curso;

public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEstudiante(Estudiante estudiante);
    
    List<Asistencia> findByCurso(Curso curso);
    
    List<Asistencia> findByFecha(LocalDate fecha);
    
    List<Asistencia> findByEstado(String estado);
    
    List<Asistencia> findByEstudianteAndCurso(Estudiante estudiante, Curso curso);

}
