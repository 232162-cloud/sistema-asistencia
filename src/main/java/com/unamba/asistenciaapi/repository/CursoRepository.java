package com.unamba.asistenciaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unamba.asistenciaapi.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

}
