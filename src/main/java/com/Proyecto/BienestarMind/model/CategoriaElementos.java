package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria_elementos")
public class CategoriaElementos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Categoria")
    private Integer idCategoria;

    @Column(name = "Descripcion", nullable = false, length = 50)
    private String descripcion;

    // Getters y Setters
    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
