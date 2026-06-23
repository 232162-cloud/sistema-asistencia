package com.unamba.asistenciaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unamba.asistenciaapi.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

}
