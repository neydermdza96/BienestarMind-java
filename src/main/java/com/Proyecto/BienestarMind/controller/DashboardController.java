package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.model.Ficha;
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.service.AsesoriaService;
import com.Proyecto.BienestarMind.service.FichaService;
import com.Proyecto.BienestarMind.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional; 
import java.util.stream.Collectors; 

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
    public String viewDashboard(Model model, Authentication authentication) { 
        
        // --- LÓGICA DE REDIRECCIÓN BASADA EN ROL ---
        // Chequea si el usuario tiene la autoridad ROLE_ADMINISTRADOR
        boolean isAdmin = authentication.getAuthorities().stream()
                                      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        
        if (isAdmin) {
            List<Asesoria> listaAsesorias = asesoriaService.findAll();
            model.addAttribute("listaAsesorias", listaAsesorias);
            model.addAttribute("totalAsesorias", listaAsesorias.size());
            return "dashboard"; // Retorna la vista del Admin
        } else {
            // Lógica de Aprendiz (puedes añadir aquí la lista de sus propias citas)
            return "aprendiz-dashboard"; // Retorna la vista del Aprendiz
        }
    }

    // 2. FORMULARIO NUEVA ASESORÍA
    @GetMapping("/asesorias/nueva")
    public String nuevaAsesoria(Model model) {
        model.addAttribute("asesoria", new Asesoria());
        cargarListasParaAsesoria(model);
        return "asesoria-form";
    }

    // 3. FORMULARIO EDITAR ASESORÍA
    @GetMapping("/asesorias/editar/{id}")
    public String editarAsesoria(@PathVariable Integer id, Model model) {
        // Busca la asesoría, si no existe lanza excepción
        Asesoria asesoria = asesoriaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asesoría inválida: " + id));
        
        model.addAttribute("asesoria", asesoria);
        cargarListasParaAsesoria(model); 
        return "asesoria-form";
    }

    // 4. GUARDAR (CREAR / ACTUALIZAR)
    @PostMapping("/asesorias/guardar")
    public String guardarAsesoria(
            @ModelAttribute("asesoria") Asesoria asesoria,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            // El servicio (AsesoriaService) realiza la validación de fechas
            asesoriaService.save(asesoria);
            
            // Si tiene éxito, se redirige con un mensaje flash
            String mensaje = (asesoria.getIdAsesoria() == null) ? "Asesoría agendada correctamente." : "Asesoría actualizada correctamente.";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
            
            return "redirect:/app/dashboard";

        } catch (IllegalArgumentException ex) {
            
            // CAPTURA LA EXCEPCIÓN: La validación de fecha falló.
            
            // 1. Añade el mensaje de error al modelo
            model.addAttribute("errorMessage", ex.getMessage());
            
            // 2. Vuelve a cargar las listas (combos)
            cargarListasParaAsesoria(model); 
            
            // 3. Retorna la vista del formulario
            return "asesoria-form"; 

        } catch (Exception e) {
            // Captura cualquier otro error de base de datos o inesperado
            model.addAttribute("errorMessage", "Error inesperado al guardar la asesoría: " + e.getMessage());
            cargarListasParaAsesoria(model);
            return "asesoria-form";
        }
    }

    // 5. ELIMINAR
    @GetMapping("/asesorias/eliminar/{id}")
    public String eliminarAsesoria(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            asesoriaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Asesoría eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se puede eliminar la asesoría. Podría tener registros asociados.");
        }
        return "redirect:/app/dashboard";
    }

    // MÉTODO AUXILIAR PARA CARGAR LISTAS
    private void cargarListasParaAsesoria(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("listaUsuarios", usuarios);
        model.addAttribute("listaFichas", fichaService.findAll());

        // ✅ CORRECCIÓN DEL ERROR DE COMPILACIÓN: 
        // Usamos getAuthority() que devuelve la cadena de rol (Ej: "ROLE_ADMINISTRADOR")
        List<Usuario> listaAsesores = usuarios.stream()
                .filter(u -> u.getRoles().stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_ADMINISTRADOR"))) // Corregido: Usar getAuthority()
                .collect(Collectors.toList());
        model.addAttribute("listaAsesores", listaAsesores);
    }
}