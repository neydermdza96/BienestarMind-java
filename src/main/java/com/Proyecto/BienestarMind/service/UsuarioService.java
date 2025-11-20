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
    public Usuario save(Usuario usuario) {
        // **Lógica de Seguridad:** ENCRIPTAR la contraseña antes de guardar.
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
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