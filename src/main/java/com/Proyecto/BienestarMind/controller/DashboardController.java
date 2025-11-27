package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.model.Ficha;
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.service.AsesoriaService;
import com.Proyecto.BienestarMind.service.FichaService;
import com.Proyecto.BienestarMind.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/app")
public class DashboardController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AsesoriaService asesoriaService;

    @Autowired
    private FichaService fichaService; // ✅ Necesario para cargar las fichas

    // 1. VISTA PRINCIPAL (DASHBOARD)
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("totalUsuarios", usuarios.size());
        
        // Carga la lista de asesorías para la tabla
        List<Asesoria> asesorias = asesoriaService.findAll();
        model.addAttribute("totalAsesorias", asesorias.size());
        model.addAttribute("listaAsesorias", asesorias);
        
        return "dashboard";
    }

    // 2. FORMULARIO DE CREACIÓN (Nueva Asesoría)
    @GetMapping("/asesorias/nueva")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("asesoria", new Asesoria());
        cargarListas(model); // Carga usuarios y fichas
        return "asesoria-form";
    }

    // 3. ✅ NUEVO: FORMULARIO DE EDICIÓN (Cargar Asesoría Existente)
    @GetMapping("/asesorias/editar/{id}")
    public String editarAsesoria(@PathVariable Integer id, Model model) {
        // Busca la asesoría por ID
        Asesoria asesoria = asesoriaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asesoría inválida: " + id));
        
        model.addAttribute("asesoria", asesoria);
        cargarListas(model); // Carga usuarios y fichas para los select
        return "asesoria-form";
    }

    // 4. GUARDAR (Sirve para CREAR y EDITAR)
    @PostMapping("/asesorias/guardar")
    public String guardarAsesoria(@ModelAttribute("asesoria") Asesoria asesoria) {
        // El servicio guarda (si tiene ID actualiza, si no crea)
        asesoriaService.save(asesoria);
        return "redirect:/app/dashboard";
    }

    // 5. ELIMINAR
    @GetMapping("/asesorias/eliminar/{id}")
    public String eliminarAsesoria(@PathVariable Integer id) {
        asesoriaService.deleteById(id);
        return "redirect:/app/dashboard";
    }

    // Método auxiliar para no repetir código
    private void cargarListas(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Ficha> fichas = fichaService.findAll();
        model.addAttribute("listaUsuarios", usuarios);
        model.addAttribute("listaFichas", fichas);
    }
}