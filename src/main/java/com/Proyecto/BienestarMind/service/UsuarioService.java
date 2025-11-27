package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por ID
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }
    
    // Obtener usuario por documento (usando el método personalizado del Repository)
    public Usuario findByDocumento(String documento) {
        return usuarioRepository.findByDocumento(documento);
    }

    // Guardar o actualizar un usuario
   // Guardar o actualizar un usuario (VERSIÓN SEGURA)
    public Usuario save(Usuario usuario) {
        
        // 1. Lógica de Seguridad para la Contraseña
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            
            // TRUCO DE SEGURIDAD:
            // Las contraseñas encriptadas con BCrypt siempre tienen 60 caracteres y empiezan con "$2a".
            // Si la contraseña que llega tiene menos de 60 caracteres, asumimos que es TEXTO PLANO (ej: "sena123")
            // y procedemos a encriptarla. Si ya es larga, la dejamos quieta para no dañar el login.
            
            if (usuario.getContrasena().length() < 60) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }

        // 2. Asignar Rol por defecto (Solo si es un usuario NUEVO)
        if (usuario.getIdUsuario() == null) {
            // Asegúrate de tener inyectado RolesRepository como vimos antes
            // Si te da error aquí, es porque te falta la lógica de roles que añadimos en pasos previos
             /* Roles rolAprendiz = roleRepository.findByNombreRol("ROLE_APRENDIZ")
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
             usuario.getRoles().add(rolAprendiz);
             */
        }
        
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario por ID
    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Verificar si un usuario existe
    public boolean existsById(Integer id) {
        return usuarioRepository.existsById(id);
    }
}