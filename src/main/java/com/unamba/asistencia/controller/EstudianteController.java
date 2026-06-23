package com.unamba.asistencia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.service.EstudianteService;

@Controller
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("estudiantes", service.listar());
        return "estudiantes";
    }

    @PostMapping("/guardar")
    public String guardar(Estudiante estudiante) {
        service.guardar(estudiante);
        return "redirect:/";
    }

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String apellidos,
            @RequestParam(required = false) String carrera,
            Model model) {
        
        List<Estudiante> resultados = null;
        
        if (codigo != null && !codigo.isEmpty()) {
            resultados = service.buscarPorCodigo(codigo);
        } else if (nombres != null && !nombres.isEmpty()) {
            resultados = service.buscarPorNombres(nombres);
        } else if (apellidos != null && !apellidos.isEmpty()) {
            resultados = service.buscarPorApellidos(apellidos);
        } else if (carrera != null && !carrera.isEmpty()) {
            resultados = service.buscarPorCarrera(carrera);
        } else {
            resultados = service.listar();
        }
        
        model.addAttribute("estudiantes", resultados);
        return "estudiantes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Estudiante> estudiante = service.obtenerPorId(id);
        if (estudiante.isPresent()) {
            model.addAttribute("estudiante", estudiante.get());
            return "editar-estudiante";
        }
        return "redirect:/";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, Estudiante estudiante) {
        service.actualizar(id, estudiante);
        return "redirect:/";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/";
    }

}
