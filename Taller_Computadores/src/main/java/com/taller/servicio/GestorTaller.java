package com.taller.servicio;

import com.taller.modelo.EquipoReparacion;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GestorTaller {

    private final List<EquipoReparacion> registroGeneral;
    private final Queue<EquipoReparacion> pendientes;
    private final Deque<EquipoReparacion> historial;
    private final Map<String, EquipoReparacion> mapaEquipos;

    public GestorTaller() {
        this.registroGeneral = new ArrayList<>();
        this.pendientes = new LinkedList<>();
        this.historial = new ArrayDeque<>();
        this.mapaEquipos = new HashMap<>();
    }

    public void registrar(EquipoReparacion equipo) {
        if (mapaEquipos.containsKey(equipo.getCodigoServicio())) {
            throw new IllegalArgumentException(
                    "Ya existe un equipo con el codigo " + equipo.getCodigoServicio());
        }
        equipo.setEstado("PENDIENTE");
        registroGeneral.add(equipo);
        pendientes.offer(equipo);
        mapaEquipos.put(equipo.getCodigoServicio(), equipo);
    }

    public EquipoReparacion procesarSiguiente() {
        EquipoReparacion equipo = pendientes.poll();
        if (equipo == null) {
            throw new IllegalStateException("No hay equipos pendientes por procesar");
        }
        equipo.setEstado("PROCESADO");
        historial.push(equipo);
        return equipo;
    }

    public void cancelar(String codigoServicio) {
        EquipoReparacion equipo = mapaEquipos.get(codigoServicio);
        if (equipo == null) {
            throw new IllegalArgumentException(
                    "No se encontro equipo con codigo " + codigoServicio);
        }
        if (!"PENDIENTE".equals(equipo.getEstado())) {
            throw new IllegalStateException(
                    "Solo se pueden cancelar equipos en estado PENDIENTE");
        }
        equipo.setEstado("CANCELADO");
        pendientes.removeIf(e -> e.getCodigoServicio().equals(codigoServicio));
    }

    public EquipoReparacion deshacer() {
        if (historial.isEmpty()) {
            throw new IllegalStateException("No hay operaciones para deshacer");
        }
        EquipoReparacion equipo = historial.pop();
        equipo.setEstado("PENDIENTE");
        pendientes.offer(equipo);
        return equipo;
    }

    public List<EquipoReparacion> obtenerTodos() {
        return new ArrayList<>(registroGeneral);
    }

    public List<EquipoReparacion> obtenerPendientes() {
        return new ArrayList<>(pendientes);
    }

    public List<EquipoReparacion> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    public EquipoReparacion buscarPorCodigo(String codigoServicio) {
        return mapaEquipos.get(codigoServicio);
    }

    public int cantidadTotal() {
        return registroGeneral.size();
    }
}
