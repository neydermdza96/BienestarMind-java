package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.ReservaEspacios;
import com.Proyecto.BienestarMind.service.FichaService;
import com.Proyecto.BienestarMind.service.UsuarioService;
import com.Proyecto.BienestarMind.service.EspaciosService;
import com.Proyecto.BienestarMind.service.ReservaEspaciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/reservaespacios")
public class ReservaEspaciosController {

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
    public String listarReservasEspacio(Model model) {
        model.addAttribute("listaReservasEspacio", reservaEspaciosService.findAll());
        // Vista: lista-reserva-espacio.html
        return "lista-reserva-espacio";
    }

    // NUEVA RESERVA
    @GetMapping("/nueva")
    public String nuevaReservaEspacio(Model model) {
        model.addAttribute("reservaEspacio", new ReservaEspacios());
        cargarListas(model);  // <- OJO: L mayúscula
        // Vista: reserva-espacio-form.html
        return "reserva-espacio-form";
    }

    // EDITAR RESERVA EXISTENTE
    @GetMapping("/editar/{id}")
    public String editarReservaEspacio(@PathVariable("id") Integer id, Model model) {
        ReservaEspacios reserva = reservaEspaciosService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id de reserva inválido: " + id));

        model.addAttribute("reservaEspacio", reserva);
        cargarListas(model);  // <- igual acá
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
            RedirectAttributes redirectAttributes
    ) {

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
            // Fecha inválida (domingo, festivo, después del 17/12/2025 en nuevas reservas, etc.)
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
                "Se eliminó la reserva #" + id + " correctamente."
        );

        return "redirect:/app/reservaespacios";
    }

    // Cargar listas para combos (fichas, usuarios, espacios)
    private void cargarListas(Model model) {
        model.addAttribute("listaFichas", fichaService.findAll());
        model.addAttribute("listaUsuarios", usuarioService.findAll());
        model.addAttribute("listaEspacios", espaciosService.findAll());
    }
}
