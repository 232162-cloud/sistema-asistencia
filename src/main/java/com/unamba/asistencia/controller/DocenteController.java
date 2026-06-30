package com.unamba.asistencia.controller;

import com.unamba.asistencia.dto.DocenteRequest;
import com.unamba.asistencia.dto.DocenteResponse;
import com.unamba.asistencia.model.Usuario;
import com.unamba.asistencia.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DocenteController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crearDocente(@RequestBody DocenteRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("username es requerido");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("password es requerido");
        }

        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("username ya existe");
        }

        Usuario usuario = new Usuario(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                "DOCENTE",
                request.getNombreCompleto()
        );

        Usuario guardado = usuarioRepository.save(usuario);
        DocenteResponse response = new DocenteResponse(
                guardado.getId(),
                guardado.getUsername(),
                guardado.getNombreCompleto(),
                guardado.getRol()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocenteResponse>> listarDocentes() {
        List<Usuario> docentes = usuarioRepository.findByRol("DOCENTE");
        List<DocenteResponse> response = docentes.stream()
                .map(usuario -> new DocenteResponse(
                        usuario.getId(),
                        usuario.getUsername(),
                        usuario.getNombreCompleto(),
                        usuario.getRol()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
