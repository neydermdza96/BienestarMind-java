package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Necesario para JPA

@Entity
@Table(name = "usuario")
@Getter // Genera todos los Getters
@Setter // Genera todos los Setters
@NoArgsConstructor // Genera el constructor sin argumentos requerido por JPA
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
    
    @Column(name = "Fecha_de_Nacimiento")
    private LocalDate fechaNacimiento; // java.time.LocalDate
}