package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "programas")
@Getter
@Setter
@NoArgsConstructor
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Programa")
    private Integer idPrograma;

    @Column(name = "Nombre_programa", nullable = false, length = 100)
    private String nombrePrograma;

    @Column(name = "Descripcion", length = 100)
    private String descripcion;
}