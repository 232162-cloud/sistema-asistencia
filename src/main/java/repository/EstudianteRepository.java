package com.unamba.asistencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unamba.asistencia.model.Estudiante;

public interface EstudianteRepository
        extends JpaRepository<Estudiante, Long> {

}