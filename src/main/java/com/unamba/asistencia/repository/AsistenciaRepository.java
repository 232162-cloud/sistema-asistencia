package com.unamba.asistencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unamba.asistencia.model.Asistencia;

public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Long> {

}
