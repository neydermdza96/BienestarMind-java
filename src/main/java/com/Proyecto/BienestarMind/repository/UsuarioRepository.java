package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // ¡Necesitas esta importación para Optional!

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // MÉTODO REQUERIDO: Spring Security busca el usuario por el "username" (tu Correo)
    Optional<Usuario> findByCorreo(String correo); 
    
    // El método que ya tenías
    Usuario findByDocumento(String documento);
}