package com.taller.modelo;

import java.util.Objects;

public class EquipoReparacion {

    private String codigoServicio;
    private String nombreCliente;
    private String descripcionProblema;
    private String tipoEquipo;
    private String estado;
    private String tecnicoAsignado;

    public EquipoReparacion(String codigoServicio, String nombreCliente,
                            String descripcionProblema, String tipoEquipo,
                            String estado, String tecnicoAsignado) {
        this.codigoServicio = codigoServicio;
        this.nombreCliente = nombreCliente;
        this.descripcionProblema = descripcionProblema;
        this.tipoEquipo = tipoEquipo;
        this.estado = estado;
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(String tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "codigo='" + codigoServicio + '\'' +
                ", cliente='" + nombreCliente + '\'' +
                ", problema='" + descripcionProblema + '\'' +
                ", tipo=" + tipoEquipo +
                ", estado=" + estado +
                ", tecnico='" + tecnicoAsignado + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipoReparacion that = (EquipoReparacion) o;
        return Objects.equals(codigoServicio, that.codigoServicio);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoServicio);
    }
}
