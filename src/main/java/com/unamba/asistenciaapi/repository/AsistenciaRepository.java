package com.unamba.asistenciaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unamba.asistenciaapi.model.Asistencia;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

}
