package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Ficha;
import com.Proyecto.BienestarMind.repository.ProgramaRepository;
import com.Proyecto.BienestarMind.service.FichaService;
// import com.Proyecto.BienestarMind.service.ReservaElementosService; // ❌ ELIMINADA: Importación no utilizada

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import com.Proyecto.BienestarMind.utils.PdfGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/fichas")
public class FichaWebController {

    private final PdfGenerator pdfGenerator;

    @Autowired
    private FichaService fichaService;

    @Autowired
    private ProgramaRepository programaRepository;

    @GetMapping
    public String listarFichas(@RequestParam(required = false) String ficha,
            @RequestParam(required = false) String programa,
            Model model) {
        List<Ficha> listaFichas = fichaService.filtrarFicha(ficha, programa);
        model.addAttribute("listaFichas", listaFichas);
        model.addAttribute("ficha", ficha);
        model.addAttribute("programa", programa);
        return "ficha-list";
    }

    @GetMapping("/nueva")
    public String formularioCrear(Model model) {
        model.addAttribute("ficha", new Ficha());
        model.addAttribute("esEdicion", false);
        model.addAttribute("listaProgramas", programaRepository.findAll());
        return "ficha-form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable String id, Model model) { // ✅ Usa String
        Ficha ficha = fichaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));

        model.addAttribute("ficha", ficha);
        model.addAttribute("esEdicion", true);
        model.addAttribute("listaProgramas", programaRepository.findAll());
        return "ficha-form";
    }

    @PostMapping("/guardar")
    public String guardarFicha(@ModelAttribute("ficha") Ficha ficha, RedirectAttributes redirectAttributes) {
        try {
            fichaService.save(ficha);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ficha guardada correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error: El programa seleccionado no es válido o hay un conflicto de datos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/app/fichas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFicha(@PathVariable String id, RedirectAttributes redirectAttributes) { // ✅ Usa String
        try {
            fichaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Ficha eliminada correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se puede eliminar: Esta ficha tiene aprendices o asesorías asociadas.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error inesperado al eliminar.");
        }
        return "redirect:/app/fichas";
    }

    public FichaWebController(FichaService fichaService, PdfGenerator pdfGenerator) {
        this.fichaService = fichaService;
        this.pdfGenerator = pdfGenerator;
    }

    @GetMapping("/reporte-fichas")
    public void generarReporteFichas(
            @RequestParam(required = false) String ficha,
            @RequestParam(required = false) String programa,
            HttpServletResponse response) throws Exception {
        var listaFichas = fichaService.filtrarFicha(ficha, programa);
        pdfGenerator.generarPdfFichas("reporte-fichas", listaFichas, ficha, programa,
                response);
    }
}