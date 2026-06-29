package com.unamba.asistencia.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unamba.asistencia.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

}
