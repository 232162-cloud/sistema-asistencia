package com.unamba.asistencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unamba.asistencia.model.Curso;
import com.unamba.asistencia.service.CursoService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

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

}
