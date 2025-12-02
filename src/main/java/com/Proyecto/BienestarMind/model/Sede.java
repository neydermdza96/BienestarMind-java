package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sede")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Sede")
    private Integer idSede;

    @Column(name = "Nombre_sede", nullable = false, length = 100)
    private String nombreSede;

    @Column(name = "Telefono_sede", nullable = false, length = 20)
    private String telefonoSede;

    @Column(name = "Direccion_sede", nullable = false, length = 150)
    private String direccionSede;

    // Getters y setters
    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getTelefonoSede() {
        return telefonoSede;
    }

    public void setTelefonoSede(String telefonoSede) {
        this.telefonoSede = telefonoSede;
    }

    public String getDireccionSede() {
        return direccionSede;
    }

    public void setDireccionSede(String direccionSede) {
        this.direccionSede = direccionSede;
    }
}
