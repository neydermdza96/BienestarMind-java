package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asesoria")
@Getter
@Setter
@NoArgsConstructor
public class Asesoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Asesoria")
    private Integer idAsesoria;

    @Column(name = "Motivo_asesoria", nullable = false, length = 255)
    private String motivoAsesoria;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha; // Usamos LocalDate

    // RELACIÓN: Usuario que RECIBE la asesoría (FK: Id_Usuario_Recibe)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Usuario_Recibe", nullable = false)
    private Usuario usuarioRecibe;

    // RELACIÓN: Usuario que ASESORA (FK: Id_Usuario_Asesor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Usuario_Asesor", nullable = false)
    private Usuario usuarioAsesor;
    
    // RELACIÓN: Ficha a la que pertenece la asesoría (FK: ficha_Id_ficha)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "ficha_Id_ficha", // Nombre de la columna FK en la tabla 'asesoria'
    referencedColumnName = "Id_ficha" // Nombre de la columna PK en la tabla 'ficha'
)
private Ficha ficha;

    // Campos de auditoría (created_AT y update_AT)
    @Column(name = "created_AT", insertable = false, updatable = false)
    private LocalDateTime createdAT;

    @Column(name = "update_AT", insertable = false, updatable = false)
    private LocalDateTime updateAT;
}