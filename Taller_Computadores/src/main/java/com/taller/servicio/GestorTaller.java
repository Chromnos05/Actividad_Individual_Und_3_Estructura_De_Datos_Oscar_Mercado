package com.taller.servicio;

import com.taller.modelo.EquipoReparacion;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

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

    public EquipoReparacion buscarPorCriterioAlternativo(String criterio) {
        return registroGeneral.stream()
                .filter(e -> e.getNombreCliente().equalsIgnoreCase(criterio))
                .findFirst()
                .orElse(null);
    }

    public List<EquipoReparacion> filtrarPorEstado(String estado) {
        return registroGeneral.stream()
                .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    public List<EquipoReparacion> filtrarPorTipo(String tipo) {
        return registroGeneral.stream()
                .filter(e -> e.getTipoEquipo().equalsIgnoreCase(tipo))
                .toList();
    }

    public List<EquipoReparacion> ordenarPorCodigo() {
        return registroGeneral.stream()
                .sorted()
                .toList();
    }

    public List<EquipoReparacion> ordenarPorNombreCliente() {
        return registroGeneral.stream()
                .sorted(Comparator.comparing(EquipoReparacion::getNombreCliente))
                .toList();
    }

    public long contarPorEstado(String estado) {
        return registroGeneral.stream()
                .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    public List<String> obtenerCodigosRegistrados() {
        return registroGeneral.stream()
                .map(EquipoReparacion::getCodigoServicio)
                .toList();
    }

    public boolean existeAlgunPendiente() {
        return registroGeneral.stream()
                .anyMatch(e -> "PENDIENTE".equals(e.getEstado()));
    }

    public boolean todosTienenCodigo() {
        return registroGeneral.stream()
                .allMatch(e -> e.getCodigoServicio() != null && !e.getCodigoServicio().isEmpty());
    }

    public boolean ningunoProcesado() {
        return registroGeneral.stream()
                .noneMatch(e -> "PROCESADO".equals(e.getEstado()));
    }

    public Map<String, List<EquipoReparacion>> agruparPorEstado() {
        return registroGeneral.stream()
                .collect(Collectors.groupingBy(EquipoReparacion::getEstado));
    }

    public Map<String, List<EquipoReparacion>> agruparPorTipo() {
        return registroGeneral.stream()
                .collect(Collectors.groupingBy(EquipoReparacion::getTipoEquipo));
    }

    public Map<String, Long> estadisticasPorEstado() {
        return registroGeneral.stream()
                .collect(Collectors.groupingBy(EquipoReparacion::getEstado, Collectors.counting()));
    }

    public Map<String, EquipoReparacion> construirMapaDesdeLista() {
        return registroGeneral.stream()
                .collect(Collectors.toMap(
                        EquipoReparacion::getCodigoServicio,
                        e -> e,
                        (existente, nuevo) -> existente));
    }

    public void mostrarEquipos(List<EquipoReparacion> equipos) {
        equipos.forEach(System.out::println);
    }
}
