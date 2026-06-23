package com.unamba.asistencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unamba.asistencia.model.Asistencia;
import com.unamba.asistencia.service.AsistenciaService;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService service;

    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("asistencias", service.listar());
        return "asistencias";
    }

    @PostMapping("/guardar")
    public String guardar(Asistencia asistencia) {
        service.guardar(asistencia);
        return "redirect:/asistencias";
    }

}
