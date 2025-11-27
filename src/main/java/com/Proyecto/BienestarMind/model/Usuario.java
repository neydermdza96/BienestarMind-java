package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Usuario")
    private Integer idUsuario;
    
    @Column(name = "Nombres", nullable = false, length = 50)
    private String nombres;

    @Column(name = "Apellidos", nullable = false, length = 50)
    private String apellidos;
    
    @Column(name = "Contrasena", nullable = false, length = 100)
    private String contrasena;
    
    @Column(name = "Documento", nullable = false, length = 20, unique = true)
    private String documento;
    
    @Column(name = "Correo", nullable = false, length = 100, unique = true)
    private String correo;
    
    @Column(name = "Genero", nullable = false, length = 20)
    private String genero;
    
    @Column(name = "Telefono", length = 15)
    private String telefono;

    @Column(name = "Fecha_de_Nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    // =======================================================
    // ✅ ESTO ES LO QUE TE FALTA: RELACIÓN CON ROLES
    // =======================================================
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "Id_Usuario"),
        inverseJoinColumns = @JoinColumn(name = "Id_Rol")
    )
    private Set<Roles> roles = new HashSet<>(); 
}