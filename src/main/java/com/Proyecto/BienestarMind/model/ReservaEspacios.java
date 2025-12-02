package com.Proyecto.BienestarMind.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "reservaespacios")   // 👈 nombre exacto de la tabla en la BD
public class ReservaEspacios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_ReservaEspacio")   // 👈 columna exacta
    private Integer idReservaEspacio;

    @Column(name = "Fecha_Reserva", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaReserva;

    @Column(name = "Motivo_Reserva", nullable = false, length = 255)
    private String motivoReserva;

    @Column(name = "Duracion")
    private Integer duracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_ficha")
    private Ficha ficha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Espacio")
    private Espacios espacio;

    // ========================
    //      GETTERS / SETTERS
    // ========================

    public Integer getIdReservaEspacio() {
        return idReservaEspacio;
    }

    public void setIdReservaEspacio(Integer idReservaEspacio) {
        this.idReservaEspacio = idReservaEspacio;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getMotivoReserva() {
        return motivoReserva;
    }

    public void setMotivoReserva(String motivoReserva) {
        this.motivoReserva = motivoReserva;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
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

    public Espacios getEspacio() {
        return espacio;
    }

    public void setEspacio(Espacios espacio) {
        this.espacio = espacio;
    }
}
