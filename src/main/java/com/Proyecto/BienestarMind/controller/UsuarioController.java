package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.service.UsuarioService; // Usamos el Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    // Inyectamos el Servicio, siguiendo la mejor práctica
    @Autowired
    private UsuarioService usuarioService;

    // GET /api/usuarios -> Obtener todos los usuarios (Read - All)
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    // GET /api/usuarios/{id} -> Obtener usuario por ID (Read - One)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/usuarios -> Crear un nuevo usuario (Create)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Código 201
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        // La lógica de encriptación de contraseña se maneja en UsuarioService
        return usuarioService.save(usuario);
    }
    
    // PUT /api/usuarios/{id} -> Actualizar un usuario (Update)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioDetails) {
        return usuarioService.findById(id)
                .map(existingUsuario -> {
                    // Actualizamos todos los campos modificables
                    existingUsuario.setNombres(usuarioDetails.getNombres());
                    existingUsuario.setApellidos(usuarioDetails.getApellidos());
                    existingUsuario.setDocumento(usuarioDetails.getDocumento());
                    existingUsuario.setCorreo(usuarioDetails.getCorreo());
                    existingUsuario.setGenero(usuarioDetails.getGenero());
                    existingUsuario.setTelefono(usuarioDetails.getTelefono());
                    existingUsuario.setFechaNacimiento(usuarioDetails.getFechaNacimiento());
                    
                    // IMPORTANTE: Actualizar la contraseña solo si se proporciona una nueva
                    // (el servicio se encargará de encriptarla)
                    if (usuarioDetails.getContrasena() != null && !usuarioDetails.getContrasena().isEmpty()) {
                        existingUsuario.setContrasena(usuarioDetails.getContrasena());
                    }

                    Usuario updatedUsuario = usuarioService.save(existingUsuario);
                    return ResponseEntity.ok(updatedUsuario);
                })
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found si el ID no existe
    }

    // DELETE /api/usuarios/{id} -> Eliminar un usuario (Delete)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Código 204
    public void deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteById(id);
    }
}