package com.unamba.asistencia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.service.CursoService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("cursos", service.listar());
        return "cursos";
    }

    @PostMapping("/guardar")
    public String guardar(Curso curso) {
        service.guardar(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String profesor,
            @RequestParam(required = false) String ciclo,
            Model model) {
        
        List<Curso> resultados = null;
        
        if (codigo != null && !codigo.isEmpty()) {
            resultados = service.buscarPorCodigo(codigo);
        } else if (nombre != null && !nombre.isEmpty()) {
            resultados = service.buscarPorNombre(nombre);
        } else if (profesor != null && !profesor.isEmpty()) {
            resultados = service.buscarPorProfesor(profesor);
        } else if (ciclo != null && !ciclo.isEmpty()) {
            resultados = service.buscarPorCiclo(ciclo);
        } else {
            resultados = service.listar();
        }
        
        model.addAttribute("cursos", resultados);
        return "cursos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Curso> curso = service.obtenerPorId(id);
        if (curso.isPresent()) {
            model.addAttribute("curso", curso.get());
            return "editar-curso";
        }
        return "redirect:/cursos";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, Curso curso) {
        service.actualizar(id, curso);
        return "redirect:/cursos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/cursos";
    }

}
