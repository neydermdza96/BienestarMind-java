package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "espacios")
public class Espacios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Espacio")
    private Integer idEspacio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Sede", nullable = false)
    private Sede sede;

    @Column(name = "Nombre_del_espacio", nullable = false, length = 100)
    private String nombreDelEspacio;

    // Getters y setters
    public Integer getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(Integer idEspacio) {
        this.idEspacio = idEspacio;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public String getNombreDelEspacio() {
        return nombreDelEspacio;
    }

    public void setNombreDelEspacio(String nombreDelEspacio) {
        this.nombreDelEspacio = nombreDelEspacio;
    }
}
