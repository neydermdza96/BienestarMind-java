package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email; 
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull; 
import jakarta.validation.constraints.Size; 
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
    
    // ✅ VALIDACIONES: Nombres
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "Nombres", nullable = false, length = 50)
    private String nombres;

    // ✅ VALIDACIONES: Apellidos
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @Column(name = "Apellidos", nullable = false, length = 50)
    private String apellidos;
    
    // La encriptación se maneja en el servicio.
    @Column(name = "Contrasena", nullable = false, length = 100)
    private String contrasena;
    
    // ✅ VALIDACIONES: Documento
    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    @Column(name = "Documento", nullable = false, length = 20, unique = true)
    private String documento;
    
    // ✅ VALIDACIONES: Correo
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un formato de correo válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    @Column(name = "Correo", nullable = false, length = 100, unique = true)
    private String correo;
    
    // ✅ VALIDACIONES: Género
    @NotBlank(message = "El género es obligatorio")
    @Column(name = "Genero", nullable = false, length = 20)
    private String genero;
    
    // ✅ VALIDACIONES: Teléfono
    @NotBlank(message = "El teléfono es obligatorio")
    @Column(name = "Telefono", nullable = false, length = 15) 
    private String telefono;

    // ✅ VALIDACIONES: Fecha de Nacimiento
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(name = "Fecha_de_Nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    // ✅ RELACIÓN CON ROLES
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "Id_Usuario"),
        inverseJoinColumns = @JoinColumn(name = "Id_Rol")
    )
    private Set<Roles> roles = new HashSet<>();
}