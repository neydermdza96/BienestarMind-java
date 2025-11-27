package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
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
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Rol")
    private Integer idRol;

    // 🔴 CORRECCIÓN AQUÍ:
    // Cambiamos "Nombre_Rol" por "Descripcion" para que coincida con tu base de datos SQL.
    @Column(name = "Descripcion", nullable = false, length = 50) 
    private String nombreRol;
    
    @Override
    public String toString() {
        return nombreRol;
    }

    public String getAuthority() {
        return nombreRol;
    }
}