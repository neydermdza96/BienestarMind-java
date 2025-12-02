package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.ReservaElementos;
import com.Proyecto.BienestarMind.service.FichaService;
import com.Proyecto.BienestarMind.service.UsuarioService;
import com.Proyecto.BienestarMind.service.ElementosService;
import com.Proyecto.BienestarMind.service.ReservaElementosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/reservaelementos")
public class ReservaElementosController {

    @Autowired
    private ReservaElementosService reservaElementosService;

    @Autowired
    private FichaService fichaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ElementosService elementosService;

    // LISTAR TODAS LAS RESERVAS DE ELEMENTOS
    @GetMapping
    public String listarReservasElementos(Model model) {
        model.addAttribute("listaReservas", reservaElementosService.findAll());
        // Vista: lista-reserva-elementos.html
        return "lista-reserva-elementos";
    }

    // NUEVA RESERVA
    @GetMapping("/nueva")
    public String nuevaReservaElemento(Model model) {
        model.addAttribute("reservaElemento", new ReservaElementos());
        cargarListas(model);
        // Vista: reserva-elementos-form.html
        return "reserva-elementos-form";
    }

    // EDITAR RESERVA EXISTENTE
    @GetMapping("/editar/{id}")
    public String editarReservaElemento(@PathVariable("id") Integer id, Model model) {
        ReservaElementos reserva = reservaElementosService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id de reserva inválido: " + id));

        model.addAttribute("reservaElemento", reserva);
        cargarListas(model);
        return "reserva-elementos-form";
    }

    // GUARDAR (CREAR / ACTUALIZAR)
    @PostMapping("/guardar")
    public String guardarReservaElemento(
            @ModelAttribute("reservaElemento") ReservaElementos reservaElemento,
            @RequestParam(required = false) String idFicha,
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) Integer idElemento,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        try {
            // Saber si es nueva o edición ANTES de guardar
            boolean esNueva = (reservaElemento.getIdReservaElemento() == null);

            // ----- Ficha (opcional) -----
            if (idFicha != null && !idFicha.isBlank()) {
                fichaService.findById(idFicha).ifPresent(reservaElemento::setFicha);
            } else {
                reservaElemento.setFicha(null);
            }

            // ----- Usuario (opcional) -----
            if (idUsuario != null) {
                usuarioService.findById(idUsuario).ifPresent(reservaElemento::setUsuario);
            } else {
                reservaElemento.setUsuario(null);
            }

            // ----- Elemento (obligatorio en la UI) -----
            if (idElemento != null) {
                elementosService.findById(idElemento).ifPresent(reservaElemento::setElemento);
            } else {
                reservaElemento.setElemento(null);
            }

            // Guardar (valida fechas en el service)
            ReservaElementos guardada = reservaElementosService.save(reservaElemento);

            String mensaje;
            if (esNueva) {
                mensaje = "Se creó la reserva de elemento #" + guardada.getIdReservaElemento() + " correctamente.";
            } else {
                mensaje = "Se actualizó la reserva de elemento #" + guardada.getIdReservaElemento() + " correctamente.";
            }

            redirectAttributes.addFlashAttribute("successMessage", mensaje);

            return "redirect:/app/reservaelementos";

        } catch (IllegalArgumentException ex) {
            // Errores de fecha y reglas de negocio
            model.addAttribute("reservaElemento", reservaElemento);
            model.addAttribute("errorMessage", ex.getMessage());
            cargarListas(model);
            return "reserva-elementos-form";
        }
    }

    // ELIMINAR RESERVA
    @GetMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable("id") Integer id,
                                  RedirectAttributes redirectAttributes) {

        reservaElementosService.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Se eliminó la reserva de elemento #" + id + " correctamente."
        );

        return "redirect:/app/reservaelementos";
    }

    // CARGA COMBOS
    private void cargarListas(Model model) {
        model.addAttribute("listaFichas", fichaService.findAll());
        model.addAttribute("listaUsuarios", usuarioService.findAll());
        model.addAttribute("listaElementos", elementosService.findAll());
    }
}
