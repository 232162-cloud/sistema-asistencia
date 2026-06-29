package com.unamba.asistencia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.service.EstudianteService;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable Long id) {
        Optional<Estudiante> estudiante = service.obtenerPorId(id);
        return estudiante.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Estudiante>> buscar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String apellidos,
            @RequestParam(required = false) String carrera) {

        if (codigo != null && !codigo.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorCodigo(codigo));
        }
        if (nombres != null && !nombres.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorNombres(nombres));
        }
        if (apellidos != null && !apellidos.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorApellidos(apellidos));
        }
        if (carrera != null && !carrera.isEmpty()) {
            return ResponseEntity.ok(service.buscarPorCarrera(carrera));
        }
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    public ResponseEntity<Estudiante> guardar(@RequestBody Estudiante estudiante) {
        Estudiante guardado = service.guardar(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable Long id, @RequestBody Estudiante estudiante) {
        Estudiante actualizado = service.actualizar(id, estudiante);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Estudiante> estudiante = service.obtenerPorId(id);
        if (estudiante.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
