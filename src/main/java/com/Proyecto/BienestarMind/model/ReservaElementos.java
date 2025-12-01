package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "reservaelementos")
public class ReservaElementos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_ReservaElemento")
    private Integer idReservaElemento;

    @Column(name = "Fecha_Reserva", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaReserva;

    @Column(name = "Descripcion_Reserva", nullable = false, length = 255)
    private String descripcionReserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_ficha")
    private Ficha ficha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Elemento")
    private Elementos elemento;

    // ========================
    //      GETTERS / SETTERS
    // ========================

    public Integer getIdReservaElemento() {
        return idReservaElemento;
    }

    public void setIdReservaElemento(Integer idReservaElemento) {
        this.idReservaElemento = idReservaElemento;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getDescripcionReserva() {
        return descripcionReserva;
    }

    public void setDescripcionReserva(String descripcionReserva) {
        this.descripcionReserva = descripcionReserva;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Elementos getElemento() {
        return elemento;
    }

    public void setElemento(Elementos elemento) {
        this.elemento = elemento;
    }
}
