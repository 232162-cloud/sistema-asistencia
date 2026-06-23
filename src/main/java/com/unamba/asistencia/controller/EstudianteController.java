package com.unamba.asistencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.unamba.asistencia.model.Estudiante;
import com.unamba.asistencia.service.EstudianteService;

@Controller
public class EstudianteController {

    @Autowired
    private EstudianteService service;

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

}
