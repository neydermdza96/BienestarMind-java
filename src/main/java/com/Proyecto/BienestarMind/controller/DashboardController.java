package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.model.Ficha;
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.service.AsesoriaService;
import com.Proyecto.BienestarMind.service.FichaService;
import com.Proyecto.BienestarMind.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // ✅ Importar
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
    private FichaService fichaService; 

    // 1. VISTA PRINCIPAL (DASHBOARD)
    @GetMapping("/dashboard")
    // ✅ Recibir el objeto Authentication para verificar el rol
    public String viewDashboard(Model model, Authentication authentication) { 
        
        // --- LÓGICA DE REDIRECCIÓN BASADA EN ROL ---
        // Chequea si el usuario tiene la autoridad ROLE_ADMINISTRADOR
        boolean isAdmin = authentication.getAuthorities().stream()
                                      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        
        if (!isAdmin) {
             // Redirige a la vista simplificada para Aprendices
             return "redirect:/app/aprendiz-dashboard";
        }
        // ------------------------------------------

        // Lógica de Dashboard de Administrador (si es admin, continúa aquí)
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("totalUsuarios", usuarios.size());
        
        // Carga la lista de asesorías para la tabla
        List<Asesoria> asesorias = asesoriaService.findAll();
        model.addAttribute("totalAsesorias", asesorias.size());
        model.addAttribute("listaAsesorias", asesorias);
        
        return "dashboard"; // Retorna el dashboard de ADMIN
    }

    // 2. FORMULARIO DE CREACIÓN (Nueva Asesoría)
    @GetMapping("/asesorias/nueva")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("asesoria", new Asesoria());
        cargarListas(model); 
        return "asesoria-form";
    }

    // 3. FORMULARIO DE EDICIÓN
    @GetMapping("/asesorias/editar/{id}")
    public String editarAsesoria(@PathVariable Integer id, Model model) {
        Asesoria asesoria = asesoriaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asesoría inválida: " + id));
        
        model.addAttribute("asesoria", asesoria);
        cargarListas(model); 
        return "asesoria-form";
    }

    // 4. GUARDAR
    @PostMapping("/asesorias/guardar")
    public String guardarAsesoria(@ModelAttribute("asesoria") Asesoria asesoria) {
        asesoriaService.save(asesoria);
        return "redirect:/app/dashboard";
    }

    // 5. ELIMINAR
    @GetMapping("/asesorias/eliminar/{id}")
    public String eliminarAsesoria(@PathVariable Integer id) {
        asesoriaService.deleteById(id);
        return "redirect:/app/dashboard";
    }

    // ✅ NUEVO: VISTA DASHBOARD APRENDIZ
    @GetMapping("/aprendiz-dashboard")
    public String viewAprendizDashboard(Model model) {
        // Lógica específica para Aprendiz (ej. cargar solo sus citas)
        List<Asesoria> asesorias = asesoriaService.findAll(); // Simulación
        model.addAttribute("totalAsesorias", asesorias.size());
        
        return "aprendiz-dashboard"; // Debe existir este template
    }


    // Método auxiliar para no repetir código
    private void cargarListas(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Ficha> fichas = fichaService.findAll();
        model.addAttribute("listaUsuarios", usuarios);
        model.addAttribute("listaFichas", fichas);
    }
}