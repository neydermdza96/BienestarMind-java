package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Espacios; // Importar la clase Espacios
import com.Proyecto.BienestarMind.model.ReservaEspacios;
import com.Proyecto.BienestarMind.service.FichaService;
import com.Proyecto.BienestarMind.service.UsuarioService;
import com.Proyecto.BienestarMind.service.EspaciosService;
import com.Proyecto.BienestarMind.service.ReservaEspaciosService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException; // Importación necesaria para el catch
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import com.Proyecto.BienestarMind.utils.PdfGenerator;

@Controller
@RequestMapping("/app/reservaespacios")
public class ReservaEspaciosController {

    private final PdfGenerator pdfGenerator;

    @Autowired
    private ReservaEspaciosService reservaEspaciosService;

    @Autowired
    private FichaService fichaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EspaciosService espaciosService;

    @GetMapping
    public String listarReservasEspacio(
            @RequestParam(required = false) String motivo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Model model) {
        List<ReservaEspacios> listaReservasEspacio = reservaEspaciosService.filtrarReservaEspacios(motivo, desde, hasta);
        model.addAttribute("listaReservasEspacio", listaReservasEspacio);
        model.addAttribute("motivo", motivo);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        return "lista-reserva-espacio";
    }

    @GetMapping("/nueva")
    public String formularioCrear(Model model) {
        model.addAttribute("reservaEspacio", new ReservaEspacios());
        model.addAttribute("esEdicion", false);
        cargarListas(model);
        return "reserva-espacio-form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        ReservaEspacios reservaEspacio = reservaEspaciosService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva de espacio no encontrada: " + id));

        model.addAttribute("reservaEspacio", reservaEspacio);
        model.addAttribute("esEdicion", true);
        cargarListas(model);
        return "reserva-espacio-form";
    }

    // =======================================================
    // ✅ MÉTODO CORREGIDO PARA ASIGNAR EL ESPACIO
    // =======================================================
    @PostMapping("/guardar")
    public String guardarReservaEspacio(@ModelAttribute("reservaEspacio") ReservaEspacios reserva,
                                    @RequestParam("idEspacio") Integer idEspacio, // 👈 Captura el ID del Espacio
                                    RedirectAttributes redirectAttributes) {

        try {
            // 1. Validar y Asignar el Espacio al objeto Reserva
            if (idEspacio == null) {
                 // Esta excepción es capturada abajo.
                throw new IllegalArgumentException("El Espacio a reservar es obligatorio.");
            }
            
            // Buscar la entidad Espacios completa
            Espacios espacio = espaciosService.findById(idEspacio)
                .orElseThrow(() -> new IllegalArgumentException("El ID de Espacio proporcionado no es válido."));

            reserva.setEspacio(espacio); // 👈 ASIGNACIÓN CRÍTICA: Resuelve el error NOT NULL
            
            // 2. Lógica para asignar el usuario (si aplica en este punto del código)
            // Nota: Se asume que la lógica para asignar el usuario autenticado (Id_Usuario)
            // se maneja en el servicio o se envía correctamente desde la vista.
            
            reservaEspaciosService.save(reserva);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva de espacio guardada correctamente.");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error de integridad de datos. Revise que la ficha, el usuario, la fecha o el espacio estén disponibles.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error de validación: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error inesperado al guardar la reserva: " + e.getMessage());
        }
        return "redirect:/app/reservaespacios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarReservaEspacio(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            reservaEspaciosService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Se eliminó la reserva #" + id + " correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se puede eliminar esta reserva porque tiene registros asociados o un estado no finalizado.");
        }
        return "redirect:/app/reservaespacios";
    }

    // Cargar listas para combos (fichas, usuarios, espacios)
    private void cargarListas(Model model) {
        model.addAttribute("listaFichas", fichaService.findAll());
        model.addAttribute("listaUsuarios", usuarioService.findAll());
        model.addAttribute("listaEspacios", espaciosService.findAll());
    }

    public ReservaEspaciosController(ReservaEspaciosService reservaEspaciosService, PdfGenerator pdfGenerator) {
        this.reservaEspaciosService = reservaEspaciosService;
        this.pdfGenerator = pdfGenerator;
    }

    @GetMapping("/reporte-reserva-espacios")
    public void generarReporteReservaElementos(
            @RequestParam(required = false) String motivo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response) throws Exception {
        var listaReservasEspacio = reservaEspaciosService.filtrarReservaEspacios(motivo, desde, hasta);
            
        // List<ReservaElementos> listaReservas = resElementosService.findAll();
        pdfGenerator.generarPdfReservaEspacios("reporte-reserva-espacios", listaReservasEspacio,motivo, desde, hasta, response);
    }
}