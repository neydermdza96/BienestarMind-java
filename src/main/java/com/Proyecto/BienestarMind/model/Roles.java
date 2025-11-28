package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority; 
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// ✅ IMPLEMENTAR GrantedAuthority
public class Roles implements GrantedAuthority { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Rol")
    private Integer idRol;

    @Column(name = "Descripcion", nullable = false, length = 50) 
    private String nombreRol; 
    
    @Override
    public String toString() {
        return nombreRol;
    }

    // ✅ CORRECCIÓN CLAVE: Spring REQUIERE el prefijo ROLE_
    @Override
    public String getAuthority() {
        return "ROLE_" + nombreRol.toUpperCase(); 
        // Retorna "ROLE_ADMINISTRADOR", "ROLE_APRENDIZ", etc.
    }
}