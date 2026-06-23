package com.unamba.asistencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unamba.asistencia.model.Curso;

public interface CursoRepository
        extends JpaRepository<Curso, Long> {

}
