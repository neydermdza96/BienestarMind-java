package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "elementos")  // nombre exacto de la tabla
public class Elementos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Elemento")   // 👈 columna exacta
    private Integer idElemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Categoria")   // FK a categoria_elementos
    private CategoriaElementos categoria;

    @Column(name = "Nombre_Elemento", nullable = false, length = 100)
    private String nombreElemento;

    @Column(name = "Estado_Elemento", nullable = false, length = 100)
    private String estadoElemento;

    // Si quieres, puedes mapear también created_AT y update_AT,
    // pero NO es obligatorio para que funcione el CRUD.
    // private Timestamp createdAt; etc.

    // ===== Getters & Setters =====

    public Integer getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(Integer idElemento) {
        this.idElemento = idElemento;
    }

    public CategoriaElementos getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaElementos categoria) {
        this.categoria = categoria;
    }

    public String getNombreElemento() {
        return nombreElemento;
    }

    public void setNombreElemento(String nombreElemento) {
        this.nombreElemento = nombreElemento;
    }

    public String getEstadoElemento() {
        return estadoElemento;
    }

    public void setEstadoElemento(String estadoElemento) {
        this.estadoElemento = estadoElemento;
    }
}
