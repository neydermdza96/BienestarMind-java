package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections; // Usaremos esta importación

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // 1. Busca el usuario en la base de datos por el correo (Username)
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        // 2. Crea el objeto UserDetails que Spring Security usará para la verificación
        // NOTA: Temporalmente se usa Collections.emptyList() para los Roles. 
        // En un proyecto final, aquí se deben cargar los Roles (Authorities) del usuario.
        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),        // Correo como Nombre de Usuario
                usuario.getContrasena(),    // Contraseña ENCRIPTADA (ver Paso 4)
                Collections.emptyList()     // Roles/Autoridades
        );
    }
}