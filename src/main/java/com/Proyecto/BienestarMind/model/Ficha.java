package com.Proyecto.BienestarMind.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ficha")
@Getter
@Setter
@NoArgsConstructor
public class Ficha {

    @Id
    @Column(name = "Id_ficha", length = 20)
    private String idFicha;

    @Column(name = "Descripcion", length = 100)
    private String descripcion;

    @Column(name = "Jornada_ficha", length = 20)
    private String jornadaFicha;

    // ✅ CAMPO NUEVO Y OBLIGATORIO (Según tu SQL)
    @Column(name = "Id_Programa", nullable = false)
    private Integer idPrograma;

    // ========================
    // GETTERS / SETTERS
    // ========================

    public String getIdFicha() {
        return idFicha;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
