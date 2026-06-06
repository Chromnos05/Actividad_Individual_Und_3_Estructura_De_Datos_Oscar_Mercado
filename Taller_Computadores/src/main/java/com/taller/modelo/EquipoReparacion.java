package com.taller.modelo;

import java.util.Objects;

/**
 * Entidad principal del sistema. Representa un equipo de computo ingresado
 * al taller de reparacion con todos sus datos relevantes.
 */
public class EquipoReparacion implements Comparable<EquipoReparacion> {

    /** Identificador unico del servicio (clave primaria del sistema) */
    private String codigoServicio;
    /** Nombre del cliente que trajo el equipo */
    private String nombreCliente;
    /** Descripcion del problema reportado por el cliente */
    private String descripcionProblema;
    /** Tipo de equipo: LAPTOP, DESKTOP o TABLET */
    private String tipoEquipo;
    /** Estado actual: PENDIENTE, PROCESADO o CANCELADO */
    private String estado;
    /** Tecnico responsable de la reparacion */
    private String tecnicoAsignado;

    /**
     * Constructor completo que inicializa todos los atributos del equipo.
     *
     * @param codigoServicio   codigo unico de servicio
     * @param nombreCliente    nombre del cliente
     * @param descripcionProblema descripcion de la falla
     * @param tipoEquipo       tipo (LAPTOP / DESKTOP / TABLET)
     * @param estado           estado inicial (PENDIENTE / PROCESADO / CANCELADO)
     * @param tecnicoAsignado  tecnico a cargo
     */
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

    // -- Getters y Setters --

    public String getCodigoServicio() { return codigoServicio; }
    public void setCodigoServicio(String codigoServicio) { this.codigoServicio = codigoServicio; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getDescripcionProblema() { return descripcionProblema; }
    public void setDescripcionProblema(String descripcionProblema) { this.descripcionProblema = descripcionProblema; }

    public String getTipoEquipo() { return tipoEquipo; }
    public void setTipoEquipo(String tipoEquipo) { this.tipoEquipo = tipoEquipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTecnicoAsignado() { return tecnicoAsignado; }
    public void setTecnicoAsignado(String tecnicoAsignado) { this.tecnicoAsignado = tecnicoAsignado; }

    // -- Object overrides --

    /**
     * Representacion textual del equipo con todos sus datos.
     */
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

    /**
     * Dos equipos son iguales si tienen el mismo codigo de servicio.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipoReparacion that = (EquipoReparacion) o;
        return Objects.equals(codigoServicio, that.codigoServicio);
    }

    /**
     * Hash basado unicamente en el codigo de servicio.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(codigoServicio);
    }

    /**
     * Orden natural por codigo de servicio (orden alfabetico).
     */
    @Override
    public int compareTo(EquipoReparacion otro) {
        return this.codigoServicio.compareTo(otro.codigoServicio);
    }
}
