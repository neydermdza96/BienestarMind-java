package com.Proyecto.BienestarMind.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Proyecto.BienestarMind.model.Roles;
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.RolesRepository;
import com.Proyecto.BienestarMind.service.UsuarioService;

import jakarta.validation.Valid;

@Controller // Usamos @Controller para servir vistas (Thymeleaf/HTML)
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolesRepository rolesRepository;

    // Este método mapea la URL /login a la vista login.html
    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("listaRoles", rolesRepository.findAll());
        return "registro";
    }

    // 4. GUARDAR (Create / Update - Acción)
    @PostMapping("/registro/guardar")
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
            return "index";
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
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Usuario " + usuarioExistente.getNombres() + " actualizado correctamente.");

            } else {
                // --- ES UNA CREACIÓN ---
                // Si es un usuario nuevo, asignamos los roles seleccionados por el
                // administrador
                usuarioForm.setRoles(nuevosRoles);
                usuarioService.save(usuarioForm);
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Usuario " + usuarioForm.getNombres() + " creado y registrado correctamente.");
            }

        } catch (DataIntegrityViolationException e) {
            // ✅ MANEJO DE ERROR DE UNICIDAD (Documento o Correo ya existen)
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error: El Correo o Documento ingresado ya se encuentra registrado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al guardar el usuario.");
        }

        return "redirect:/index";
    }

    // Controlador para /home (éxito después del login)

}