package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Usuario;
import com.Proyecto.BienestarMind.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;             // ✅ Importar
import org.springframework.security.core.authority.SimpleGrantedAuthority; // ✅ Importar
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;                                          // ✅ Importar

import java.util.stream.Collectors;                                   // ✅ Importar

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

        // 2. Convierte el Set de Roles del Usuario a una Collection de GrantedAuthority
        Collection<? extends GrantedAuthority> authorities = 
                usuario.getRoles().stream()
                       // Mapea el rol usando getAuthority() del modelo Roles (ej: ROLE_ADMINISTRADOR)
                       .map(rol -> new SimpleGrantedAuthority(rol.getAuthority())) 
                       .collect(Collectors.toList());
                       
        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),        // Correo como Nombre de Usuario
                usuario.getContrasena(),    // Contraseña ENCRIPTADA
                authorities                 // ✅ CORRECCIÓN CLAVE: Usamos las autoridades reales
        );
    }
}