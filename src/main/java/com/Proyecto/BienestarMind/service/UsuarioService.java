package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Roles; // ✅ NECESARIO
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.RolesRepository; // ✅ NECESARIO
import com.Proyecto.BienestarMind.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


    // Guardar o actualizar un usuario (VERSIÓN SEGURA con Roles)
    public Usuario save(Usuario usuario) {
        
        // 1. Lógica de Seguridad para la Contraseña
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            if (usuario.getContrasena().length() < 60) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }

        // 2. Asignar Rol por defecto (Solo si es un usuario NUEVO)
        if (usuario.getIdUsuario() == null || usuario.getRoles().isEmpty()) {
             // 🔴 USAMOS 'APRENDIZ' de tu DB
             Roles rolAprendiz = rolesRepository.findByNombreRol("APRENDIZ") 
                                     .orElseThrow(() -> new RuntimeException("Error: Rol 'APRENDIZ' no encontrado."));
             
             usuario.setRoles(Collections.singleton(rolAprendiz));
        }
        
        return usuarioRepository.save(usuario);
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