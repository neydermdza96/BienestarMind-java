package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.service.UsuarioService;
import com.Proyecto.BienestarMind.service.AsesoriaService;
import com.Proyecto.BienestarMind.service.FichaService; // AÑADIDO
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
    
    @Autowired // INYECCIÓN NECESARIA para el formulario
    private FichaService fichaService; 

    // -----------------------------------------------------------
    // 1. DASHBOARD - VISTA PRINCIPAL
    // -----------------------------------------------------------
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("listaUsuarios", usuarios);

        List<Asesoria> asesorias = asesoriaService.findAll();
        model.addAttribute("totalAsesorias", asesorias.size());
        model.addAttribute("listaAsesorias", asesorias);
        
        // Retorna el template dashboard.html
        return "dashboard"; 
    }
    
    // -----------------------------------------------------------
    // 2. CREAR ASESORÍA (C del CRUD Web)
    // -----------------------------------------------------------

    // 2.1 Muestra el formulario vacío
    @GetMapping("/asesorias/nueva")
    public String mostrarFormularioCreacion(Model model) {
        
        model.addAttribute("asesoria", new Asesoria());
        
        // Carga la data para los SELECT (Usuarios y Fichas)
        model.addAttribute("listaUsuarios", usuarioService.findAll());
        model.addAttribute("listaFichas", fichaService.findAll()); 
        
        return "asesoria-form"; // Retorna el template asesoria-form.html
    }

    // 2.2 Guarda la asesoría enviada por POST
    @PostMapping("/asesorias/guardar")
    public String guardarAsesoria(@ModelAttribute("asesoria") Asesoria asesoria) {
        
        asesoriaService.save(asesoria);
        
        return "redirect:/app/dashboard"; // Redirige al Dashboard
    }
    
    // -----------------------------------------------------------
    // 3. ELIMINAR ASESORÍA (D del CRUD Web)
    // -----------------------------------------------------------
    @GetMapping("/asesorias/eliminar/{id}")
    public String eliminarAsesoria(@PathVariable Integer id) {
        asesoriaService.deleteById(id);
        
        return "redirect:/app/dashboard"; 
    }
    
    // (Opcional) Puedes crear una vista específica para la gestión de usuarios
    @GetMapping("/usuarios")
    public String viewUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("listaUsuarios", usuarios);
        return "gestion-usuarios"; 
    }
}