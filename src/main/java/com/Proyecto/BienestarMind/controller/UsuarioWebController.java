package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException; 
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. LISTAR USUARIOS (Read)
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.findAll());
        return "usuario-list"; // Vista de la lista
    }

    // 2. FORMULARIO CREAR (Create - Vista)
    @GetMapping("/nuevo")
    public String formularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario-form"; // Vista del formulario
    }

    // 3. FORMULARIO EDITAR (Update - Vista)
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario inválido: " + id));
        model.addAttribute("usuario", usuario);
        return "usuario-form"; // Reutilizamos el mismo formulario
    }

    // 4. GUARDAR (Create / Update - Acción)
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuarioForm) {
        
        // Lógica para manejar EDICIÓN vs CREACIÓN
        if (usuarioForm.getIdUsuario() != null) {
            // --- ES UNA EDICIÓN ---
            Usuario usuarioExistente = usuarioService.findById(usuarioForm.getIdUsuario()).get();
            
            // Actualizamos datos básicos
            usuarioExistente.setNombres(usuarioForm.getNombres());
            usuarioExistente.setApellidos(usuarioForm.getApellidos());
            usuarioExistente.setDocumento(usuarioForm.getDocumento());
            usuarioExistente.setCorreo(usuarioForm.getCorreo());
            usuarioExistente.setTelefono(usuarioForm.getTelefono());
            usuarioExistente.setGenero(usuarioForm.getGenero());

            usuarioExistente.setFechaNacimiento(usuarioForm.getFechaNacimiento());
            
            // CONTRASEÑA: Solo la actualizamos si el usuario escribió una nueva
            if (usuarioForm.getContrasena() != null && !usuarioForm.getContrasena().isEmpty()) {
                usuarioExistente.setContrasena(usuarioForm.getContrasena());
                // El servicio se encargará de encriptarla de nuevo
            }
            
            // Guardamos el objeto existente actualizado (Mantiene sus roles antiguos)
            usuarioService.save(usuarioExistente);
            
        } else {
            // --- ES UNA CREACIÓN ---
            // El servicio se encarga de encriptar pass y asignar rol por defecto
            usuarioService.save(usuarioForm);
        }

        return "redirect:/app/usuarios";
    }

    // 5. ELIMINAR (Delete)
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            // Aquí atrapamos el error de llave foránea (reservas, asesorías, etc.)
            redirectAttributes.addFlashAttribute("mensajeError", "No se puede eliminar este usuario porque tiene historial asociado (Reservas, Asesorías, etc.).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al intentar eliminar.");
        }
        return "redirect:/app/usuarios";
    }
}