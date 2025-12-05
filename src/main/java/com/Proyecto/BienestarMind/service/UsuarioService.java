package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Roles;
import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.RolesRepository;
import com.Proyecto.BienestarMind.repository.UsuarioRepository;
import com.Proyecto.BienestarMind.repository.AsesoriaRepository;
import com.Proyecto.BienestarMind.repository.ReservaElementosRepository;
import com.Proyecto.BienestarMind.repository.ReservaEspaciosRepository;
import com.Proyecto.BienestarMind.repository.FichaRepository; // ✅ NUEVA IMPORTACIÓN
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importación de Spring Transactional
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RolesRepository rolesRepository;

    // ✅ INYECCIONES PARA LA ELIMINACIÓN EN CASCADA MANUAL
    @Autowired
    private AsesoriaRepository asesoriaRepository;
    
    @Autowired
    private ReservaElementosRepository reservaElementosRepository;
    
    @Autowired
    private ReservaEspaciosRepository reservaEspaciosRepository;
    
    @Autowired
    private FichaRepository fichaRepository; // ✅ NUEVA INYECCIÓN CRÍTICA
    // -----------------------------------------------------------
    
    public List<Usuario> findAll() { return usuarioRepository.findAll(); }
    public Optional<Usuario> findById(Integer id) { return usuarioRepository.findById(id); }
    public Usuario findByDocumento(String documento) { return usuarioRepository.findByDocumento(documento); }
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null); 
    }

    public Usuario save(Usuario usuario) {
        
        validarEdadMinima(usuario.getFechaNacimiento());

        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            if (usuario.getContrasena().length() < 50) { 
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }

        if (usuario.getIdUsuario() == null || usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
             Roles rolAprendiz = rolesRepository.findByNombreRol("APRENDIZ") 
                                     .orElseThrow(() -> new RuntimeException("Error: Rol 'APRENDIZ' no encontrado."));
             
             usuario.setRoles(Collections.singleton(rolAprendiz));
        }
        
        return usuarioRepository.save(usuario);
    }
    
    private void validarEdadMinima(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if (edad < 14) {
            throw new IllegalArgumentException("Solo se permite el registro de usuarios mayores de 14 años. Su edad es: " + edad);
        }
    }
    
    public Usuario saveInitialAdmin(Usuario admin, String adminRoleNameInDB) {
        
        Roles rolAdmin = rolesRepository.findByNombreRol(adminRoleNameInDB)
                                 .orElseThrow(() -> new RuntimeException("Error: Rol '" + adminRoleNameInDB + "' no encontrado."));
        
        admin.setRoles(Set.of(rolAdmin));

        if (admin.getContrasena() != null) {
            admin.setContrasena(passwordEncoder.encode(admin.getContrasena()));
        }

        return usuarioRepository.save(admin);
    }
    
    // =======================================================
    // ✅ MÉTODO CORREGIDO FINAL: ELIMINACIÓN EN CASCADA MANUAL
    // =======================================================
    @Transactional
    public void deleteById(Integer id) {
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // 1. LIMPIEZA DE ASOCIACIONES MANY-TO-MANY (CAUSA DEL ERROR ANTERIOR)
        // Elimina las entradas en la tabla de unión 'usuario_ficha'.
        fichaRepository.deleteUserFichaAssociations(id); // <--- PASO CRÍTICO AÑADIDO
        
        // 2. ELIMINACIÓN DE RESERVAS Y ASESORÍAS (Lógica previa)

        // A. Eliminar Reservas de Elementos
        reservaElementosRepository.deleteByUsuario(usuario);
        
        // B. Eliminar Reservas de Espacios
        reservaEspaciosRepository.deleteByUsuario(usuario);

        // C. Eliminar Asesorías (Donde el usuario es receptor O asesor)
        asesoriaRepository.deleteByUsuarioRecibe(usuario);
        asesoriaRepository.deleteByUsuarioAsesor(usuario);
        
        // 3. Eliminar el usuario.
        usuarioRepository.delete(usuario);
    }
}