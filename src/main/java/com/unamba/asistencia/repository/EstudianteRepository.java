package com.unamba.asistencia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unamba.asistencia.model.Estudiante;

public interface EstudianteRepository
        extends JpaRepository<Estudiante, Long> {

    List<Estudiante> findByCodigo(String codigo);
    
    List<Estudiante> findByNombresContainingIgnoreCase(String nombres);
    
    List<Estudiante> findByApellidosContainingIgnoreCase(String apellidos);
    
    List<Estudiante> findByCarreraContainingIgnoreCase(String carrera);

}
