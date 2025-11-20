package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.UsuarioRepository; // En un proyecto real, se inyecta el Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository usuarioRepository; // Idealmente inyectarías el UsuarioService

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    // ... otros métodos (PUT, DELETE)
}