package com.unamba.asistencia.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unamba.asistencia.model.Asistencia;
import com.unamba.asistencia.service.AsistenciaService;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService service) {
        this.service = service;
    }

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

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String estado,
            Model model) {
        
        List<Asistencia> resultados = null;
        
        if (fecha != null && !fecha.isEmpty()) {
            try {
                LocalDate fechaBuscar = LocalDate.parse(fecha);
                resultados = service.buscarPorFecha(fechaBuscar);
            } catch (Exception e) {
                resultados = service.listar();
            }
        } else if (estado != null && !estado.isEmpty()) {
            resultados = service.buscarPorEstado(estado);
        } else {
            resultados = service.listar();
        }
        
        model.addAttribute("asistencias", resultados);
        return "asistencias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Asistencia> asistencia = service.obtenerPorId(id);
        if (asistencia.isPresent()) {
            model.addAttribute("asistencia", asistencia.get());
            return "editar-asistencia";
        }
        return "redirect:/asistencias";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, Asistencia asistencia) {
        service.actualizar(id, asistencia);
        return "redirect:/asistencias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/asistencias";
    }

}
