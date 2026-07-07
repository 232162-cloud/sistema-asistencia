package com.unamba.asistencia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.model.Usuario;
import com.unamba.asistencia.repository.EstudianteRepository;
import com.unamba.asistencia.repository.UsuarioRepository;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public EstudianteService(
            EstudianteRepository repository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Estudiante> listar() {
        return repository.findAll();
    }

    public Estudiante guardar(Estudiante estudiante) {

        String username = estudiante.getCodigo();

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(username));
        usuario.setRol("USER");
        usuario.setNombreCompleto(
                estudiante.getNombres() + " " + estudiante.getApellidos());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        estudiante.setUsuario(usuarioGuardado);

        return repository.save(estudiante);
    }

    public Optional<Estudiante> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public List<Estudiante> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    public List<Estudiante> buscarPorNombres(String nombres) {
        return repository.findByNombresContainingIgnoreCase(nombres);
    }

    public List<Estudiante> buscarPorApellidos(String apellidos) {
        return repository.findByApellidosContainingIgnoreCase(apellidos);
    }

    public List<Estudiante> buscarPorCarrera(String carrera) {
        return repository.findByCarreraContainingIgnoreCase(carrera);
    }

    public Estudiante actualizar(Long id, Estudiante estudianteActualizado) {

        Optional<Estudiante> estudiante = repository.findById(id);

        if (estudiante.isPresent()) {

            Estudiante est = estudiante.get();

            if (estudianteActualizado.getCodigo() != null) {
                est.setCodigo(estudianteActualizado.getCodigo());
            }

            if (estudianteActualizado.getNombres() != null) {
                est.setNombres(estudianteActualizado.getNombres());
            }

            if (estudianteActualizado.getApellidos() != null) {
                est.setApellidos(estudianteActualizado.getApellidos());
            }

            if (estudianteActualizado.getCarrera() != null) {
                est.setCarrera(estudianteActualizado.getCarrera());
            }

            if (estudianteActualizado.getUsuario() != null) {
                est.setUsuario(estudianteActualizado.getUsuario());
            }

            return repository.save(est);
        }

        return null;
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}