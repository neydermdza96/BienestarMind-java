package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Roles; // ✅ Importar
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.RolesRepository; // ✅ Importar
import com.Proyecto.BienestarMind.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException; 
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid; // ✅ Importar
import org.springframework.validation.BindingResult; // ✅ Importar

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

@Controller
@RequestMapping("/app/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolesRepository rolesRepository; // ✅ REQUERIDO

    // 1. LISTAR USUARIOS (Read)
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.findAll());
        return "usuario-list";
    }

    // 2. FORMULARIO CREAR (Create - Vista)
    @GetMapping("/nuevo")
    public String formularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        // ✅ CARGA LISTA DE ROLES
        model.addAttribute("listaRoles", rolesRepository.findAll()); 
        return "usuario-form";
    }

    // 3. FORMULARIO EDITAR (Update - Vista)
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario inválido: " + id));
        model.addAttribute("usuario", usuario);
        // ✅ CARGA LISTA DE ROLES
        model.addAttribute("listaRoles", rolesRepository.findAll()); 
        return "usuario-form";
    }

    // 4. GUARDAR (Create / Update - Acción)
    @PostMapping("/guardar")
    public String guardarUsuario(
            @Valid @ModelAttribute("usuario") Usuario usuarioForm, 
            BindingResult result, 
            RedirectAttributes redirectAttributes,
            // ✅ CAPTURA LOS ID's DEL DROPDOWN DE ROLES
            @RequestParam(value = "rolesIds", required = false) List<Integer> rolesIds,
            Model model) { 
        
        // 1. SI HAY ERRORES DE VALIDACIÓN, VUELVE AL FORMULARIO
        if (result.hasErrors()) {
            model.addAttribute("listaRoles", rolesRepository.findAll()); 
            // Esto asegura que el dropdown de roles no se pierda en caso de error
            return "usuario-form";
        }

        try {
            // 2. BUSCAR Y ASIGNAR ROLES
            Set<Roles> nuevosRoles = new HashSet<>();
            if (rolesIds != null && !rolesIds.isEmpty()) {
                // Buscamos todos los objetos Roles por sus IDs
                nuevosRoles.addAll(rolesRepository.findAllById(rolesIds));
            }
            
            // Lógica de EDICIÓN vs CREACIÓN
            if (usuarioForm.getIdUsuario() != null) {
                // --- ES UNA EDICIÓN ---
                Optional<Usuario> optionalExistente = usuarioService.findById(usuarioForm.getIdUsuario());
                if (optionalExistente.isEmpty()) {
                    throw new IllegalArgumentException("Usuario no encontrado para edición.");
                }
                Usuario usuarioExistente = optionalExistente.get();
                
                // Actualizar campos
                usuarioExistente.setNombres(usuarioForm.getNombres());
                usuarioExistente.setApellidos(usuarioForm.getApellidos());
                usuarioExistente.setDocumento(usuarioForm.getDocumento());
                usuarioExistente.setCorreo(usuarioForm.getCorreo());
                usuarioExistente.setTelefono(usuarioForm.getTelefono());
                usuarioExistente.setGenero(usuarioForm.getGenero());
                usuarioExistente.setFechaNacimiento(usuarioForm.getFechaNacimiento());
                
                // ✅ ASIGNAR ROLES EDITADOS
                usuarioExistente.setRoles(nuevosRoles); 
                
                // Contraseña: Solo actualizamos si se proporcionó una nueva
                if (usuarioForm.getContrasena() != null && !usuarioForm.getContrasena().isEmpty()) {
                    usuarioExistente.setContrasena(usuarioForm.getContrasena());
                }
                
                usuarioService.save(usuarioExistente);
                redirectAttributes.addFlashAttribute("mensajeExito", "Usuario " + usuarioExistente.getNombres() + " actualizado correctamente.");
                
            } else {
                // --- ES UNA CREACIÓN ---
                // Si es un usuario nuevo, asignamos los roles seleccionados por el administrador
                usuarioForm.setRoles(nuevosRoles);
                usuarioService.save(usuarioForm); 
                redirectAttributes.addFlashAttribute("mensajeExito", "Usuario " + usuarioForm.getNombres() + " creado y registrado correctamente.");
            }

        } catch (DataIntegrityViolationException e) {
             // ✅ MANEJO DE ERROR DE UNICIDAD (Documento o Correo ya existen)
             redirectAttributes.addFlashAttribute("mensajeError", "Error: El Correo o Documento ingresado ya se encuentra registrado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al guardar el usuario.");
        }

        return "redirect:/app/usuarios";
    }

    // 5. ELIMINAR (Delete)
    // ... (El método eliminarUsuario se mantiene, ya que estaba correcto) ...
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se puede eliminar este usuario porque tiene historial asociado (Reservas, Asesorías, etc.).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al intentar eliminar.");
        }
        return "redirect:/app/usuarios";
    }
}