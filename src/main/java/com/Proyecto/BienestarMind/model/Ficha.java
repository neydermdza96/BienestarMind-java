package com.Proyecto.BienestarMind.model;

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
    @Column(name = "Id_ficha", length = 20) // <-- ¡Añade length = 20 aquí!
    private String idFicha; 
    
    @Column(name = "Descripcion", length = 100)
    private String descripcion;
    
    @Column(name = "Jornada_ficha", length = 20)
    private String jornadaFicha;
}