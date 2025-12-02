package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Roles; // ✅ NECESARIO
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.RolesRepository; // ✅ NECESARIO
import com.Proyecto.BienestarMind.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate; // Importación necesaria
import java.time.Period;   // Importación necesaria
import java.util.Date;     // Importación necesaria (si la entidad usa Date)
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set; // ✅ NECESARIO

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RolesRepository rolesRepository; // ✅ INYECCIÓN CRÍTICA

    // ... (Métodos find, findAll, findByDocumento) ...
    public List<Usuario> findAll() { return usuarioRepository.findAll(); }
    public Optional<Usuario> findById(Integer id) { return usuarioRepository.findById(id); }
    public Usuario findByDocumento(String documento) { return usuarioRepository.findByDocumento(documento); }
    public Usuario findByCorreo(String correo) {
    // Usamos el método findByCorreo que definiste en UsuarioRepository
    // Si no lo encuentra, devuelve null
    return usuarioRepository.findByCorreo(correo).orElse(null); 
}


    // Guardar o actualizar un usuario (VERSIÓN SEGURA con Roles y VALIDACIÓN DE EDAD)
    public Usuario save(Usuario usuario) {
        
        // 1. ✅ NUEVA VALIDACIÓN: Mayor de 14 años (Usando LocalDate del modelo)
        validarEdadMinima(usuario.getFechaNacimiento());

        // 2. Lógica de Seguridad para la Contraseña
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            // ✅ CORRECCIÓN: Evitamos re-encriptar contraseñas que ya tienen el hash completo (longitud ~60)
            if (usuario.getContrasena().length() < 50) { 
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }

        // 3. Asignar Rol por defecto (Solo si es un usuario NUEVO o sin roles)
        if (usuario.getIdUsuario() == null || usuario.getRoles().isEmpty()) {
             // Usamos 'APRENDIZ' de tu DB
             Roles rolAprendiz = rolesRepository.findByNombreRol("APRENDIZ") 
                                     .orElseThrow(() -> new RuntimeException("Error: Rol 'APRENDIZ' no encontrado."));
             
             usuario.setRoles(Collections.singleton(rolAprendiz));
        }
        
        return usuarioRepository.save(usuario);
    }
    
    // ✅ NUEVO MÉTODO DE VALIDACIÓN DE EDAD
    private void validarEdadMinima(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        
        // Calcular la edad en años
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if (edad < 14) {
            throw new IllegalArgumentException("Solo se permite el registro de usuarios mayores de 14 años. Su edad es: " + edad);
        }
    }
    
    // =======================================================
    // ✅ MÉTODO ESPECIAL PARA CREAR EL PRIMER ADMINISTRADOR
    // =======================================================
    /**
     * Este método es llamado por el Seeder (CommandLineRunner)
     */
    public Usuario saveInitialAdmin(Usuario admin, String adminRoleNameInDB) {
        
        // 1. Buscar el rol de Administrador
        Roles rolAdmin = rolesRepository.findByNombreRol(adminRoleNameInDB)
                                 .orElseThrow(() -> new RuntimeException("Error: Rol '" + adminRoleNameInDB + "' no encontrado."));
        
        // 2. Asignar el rol
        admin.setRoles(Set.of(rolAdmin));

        // 3. Encriptar la contraseña (asegura que el seeder funcione)
        if (admin.getContrasena() != null) {
            admin.setContrasena(passwordEncoder.encode(admin.getContrasena()));
        }

        return usuarioRepository.save(admin);
    }

    public void deleteById(Integer id) { usuarioRepository.deleteById(id); }
}