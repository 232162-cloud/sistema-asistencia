package com.unamba.asistencia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.asistencia.dto.CursoRequest;
import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.service.CursoService;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        Optional<Curso> curso = service.obtenerPorId(id);
        return curso.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<List<Curso>> buscar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String profesor,
            @RequestParam(required = false) String ciclo) {

        if (codigo != null && !codigo.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorCodigo(codigo));
        }
        if (nombre != null && !nombre.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorNombre(nombre));
        }
        if (profesor != null && !profesor.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorProfesor(profesor));
        }
        if (ciclo != null && !ciclo.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorCiclo(ciclo));
        }
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> guardar(@RequestBody CursoRequest request) {
        Curso curso = new Curso();
        curso.setCodigo(request.getCodigo());
        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setCreditos(request.getCreditos());
        curso.setProfesor(request.getProfesor());
        curso.setCiclo(request.getCiclo());
        try {
            Curso creado = service.guardar(curso, request.getDocenteId());
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CursoRequest request) {
        Curso curso = new Curso();
        curso.setCodigo(request.getCodigo());
        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setCreditos(request.getCreditos());
        curso.setProfesor(request.getProfesor());
        curso.setCiclo(request.getCiclo());
        try {
            Curso actualizado = service.actualizar(id, curso, request.getDocenteId());
            if (actualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Curso> curso = service.obtenerPorId(id);
        if (curso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
