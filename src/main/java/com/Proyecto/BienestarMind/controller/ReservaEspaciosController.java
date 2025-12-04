package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.ReservaElementos;
import com.Proyecto.BienestarMind.model.ReservaEspacios;
import com.Proyecto.BienestarMind.service.FichaService;
import com.Proyecto.BienestarMind.service.ReservaElementosService;
import com.Proyecto.BienestarMind.service.UsuarioService;
import com.Proyecto.BienestarMind.service.EspaciosService;
import com.Proyecto.BienestarMind.service.ReservaEspaciosService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    // LISTAR TODAS LAS RESERVAS
    @GetMapping
    public String listarReservasEspacio(
        @RequestParam(required = false) String motivo,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
        Model model) {
        List<ReservaEspacios> reservaEspacios = reservaEspaciosService.filtrarReservaEspacios(motivo, desde, hasta);    
        model.addAttribute("listaReservasEspacio", reservaEspacios);
        model.addAttribute("motivo", motivo);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        // Vista: lista-reserva-espacio.html
        return "lista-reserva-espacio";
    }

    // NUEVA RESERVA
    @GetMapping("/nueva")
    public String nuevaReservaEspacio(Model model) {
        model.addAttribute("reservaEspacio", new ReservaEspacios());
        cargarListas(model); // <- OJO: L mayúscula
        // Vista: reserva-espacio-form.html
        return "reserva-espacio-form";
    }

    // EDITAR RESERVA EXISTENTE
    @GetMapping("/editar/{id}")
    public String editarReservaEspacio(@PathVariable("id") Integer id, Model model) {
        ReservaEspacios reserva = reservaEspaciosService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id de reserva inválido: " + id));

        model.addAttribute("reservaEspacio", reserva);
        cargarListas(model); // <- igual acá
        return "reserva-espacio-form";
    }

    // GUARDAR (CREAR / ACTUALIZAR)
    @PostMapping("/guardar")
    public String guardarReservaEspacio(
            @ModelAttribute("reservaEspacio") ReservaEspacios reservaEspacio,
            @RequestParam(required = false) String idFicha,
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) Integer idEspacio,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // Saber si es nueva o edición ANTES de guardar
            boolean esNueva = (reservaEspacio.getIdReservaEspacio() == null);

            // ----- Ficha (opcional) -----
            if (idFicha != null && !idFicha.isBlank()) {
                fichaService.findById(idFicha).ifPresent(reservaEspacio::setFicha);
            } else {
                reservaEspacio.setFicha(null);
            }

            // ----- Usuario (opcional) -----
            if (idUsuario != null) {
                usuarioService.findById(idUsuario).ifPresent(reservaEspacio::setUsuario);
            } else {
                reservaEspacio.setUsuario(null);
            }

            // ----- Espacio (obligatorio en UI) -----
            if (idEspacio != null) {
                espaciosService.findById(idEspacio).ifPresent(reservaEspacio::setEspacio);
            } else {
                reservaEspacio.setEspacio(null);
            }

            // Guardar (aquí también se valida la fecha en el service)
            ReservaEspacios guardada = reservaEspaciosService.save(reservaEspacio);

            // Mensaje distinto según si se creó o se editó
            String mensaje;
            if (esNueva) {
                mensaje = "Se creó la reserva #" + guardada.getIdReservaEspacio() + " correctamente.";
            } else {
                mensaje = "Se actualizó la reserva #" + guardada.getIdReservaEspacio() + " correctamente.";
            }

            redirectAttributes.addFlashAttribute("successMessage", mensaje);

            return "redirect:/app/reservaespacios";

        } catch (IllegalArgumentException ex) {
            // Fecha inválida (domingo, festivo, después del 17/12/2025 en nuevas reservas,
            // etc.)
            model.addAttribute("reservaEspacio", reservaEspacio);
            model.addAttribute("errorMessage", ex.getMessage());
            cargarListas(model);
            return "reserva-espacio-form";
        }
    }

    // ELIMINAR RESERVA
    @GetMapping("/eliminar/{id}")
    public String eliminarReservaEspacio(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {

        reservaEspaciosService.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Se eliminó la reserva #" + id + " correctamente.");

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
