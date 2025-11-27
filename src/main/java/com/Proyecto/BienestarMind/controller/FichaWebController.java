package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Ficha;
import com.Proyecto.BienestarMind.repository.ProgramaRepository; // 👈 1. IMPORTAR
import com.Proyecto.BienestarMind.service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/fichas")
public class FichaWebController {

    @Autowired
    private FichaService fichaService;

    @Autowired
    private ProgramaRepository programaRepository; // 👈 2. INYECTAR REPOSITORIO

    @GetMapping
    public String listarFichas(Model model) {
        model.addAttribute("listaFichas", fichaService.findAll());
        return "ficha-list";
    }

    @GetMapping("/nueva")
    public String formularioCrear(Model model) {
        model.addAttribute("ficha", new Ficha());
        model.addAttribute("esEdicion", false);
        // 👈 3. ENVIAR LISTA DE PROGRAMAS
        model.addAttribute("listaProgramas", programaRepository.findAll()); 
        return "ficha-form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable String id, Model model) {
        Ficha ficha = fichaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        
        model.addAttribute("ficha", ficha);
        model.addAttribute("esEdicion", true);
        // 👈 3. ENVIAR LISTA DE PROGRAMAS TAMBIÉN EN EDICIÓN
        model.addAttribute("listaProgramas", programaRepository.findAll());
        return "ficha-form";
    }

    @PostMapping("/guardar")
    public String guardarFicha(@ModelAttribute("ficha") Ficha ficha, RedirectAttributes redirectAttributes) {
        try {
            fichaService.save(ficha);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ficha guardada correctamente.");
        } catch (DataIntegrityViolationException e) {
             // 👈 4. MANEJO DE ERROR AMIGABLE
             redirectAttributes.addFlashAttribute("mensajeError", "Error: El programa seleccionado no es válido o hay un conflicto de datos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/app/fichas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFicha(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            fichaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ficha eliminada correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se puede eliminar: Esta ficha tiene aprendices o asesorías asociadas.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error inesperado al eliminar.");
        }
        return "redirect:/app/fichas";
    }
}