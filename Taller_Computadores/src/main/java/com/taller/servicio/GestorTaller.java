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

/**
 * Servicio central del taller. Mantiene las cuatro colecciones del SDK y
 * expone toda la logica de negocio: registro, procesamiento, cancelacion,
 * consultas con Stream y agrupamientos.
 */
public class GestorTaller {

    // -- Colecciones del SDK de Java --

    /** Registro historico completo de todos los equipos (ArrayList) */
    private final List<EquipoReparacion> registroGeneral;
    /** Equipos pendientes por atender en orden FIFO (LinkedList como Queue) */
    private final Queue<EquipoReparacion> pendientes;
    /** Historial de equipos ya procesados en orden LIFO (ArrayDeque como Deque) */
    private final Deque<EquipoReparacion> historial;
    /** Mapa para busqueda rapida por codigo de servicio (HashMap) */
    private final Map<String, EquipoReparacion> mapaEquipos;

    /** Inicializa las cuatro colecciones vacias. */
    public GestorTaller() {
        this.registroGeneral = new ArrayList<>();
        this.pendientes = new LinkedList<>();
        this.historial = new ArrayDeque<>();
        this.mapaEquipos = new HashMap<>();
    }

    // -- Metodos de negocio --

    /**
     * Registra un nuevo equipo. Valida que el codigo no exista en el Map,
     * asigna estado PENDIENTE y lo agrega a las tres colecciones.
     *
     * @param equipo equipo a registrar
     * @throws IllegalArgumentException si el codigo ya existe
     */
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

    /**
     * Procesa el siguiente equipo en la cola FIFO de pendientes.
     * Cambia su estado a PROCESADO y lo apila en el historial.
     *
     * @return el equipo procesado
     * @throws IllegalStateException si no hay pendientes
     */
    public EquipoReparacion procesarSiguiente() {
        EquipoReparacion equipo = pendientes.poll();
        if (equipo == null) {
            throw new IllegalStateException("No hay equipos pendientes por procesar");
        }
        equipo.setEstado("PROCESADO");
        historial.push(equipo);
        return equipo;
    }

    /**
     * Cancela un equipo pendiente identificado por su codigo.
     * Cambia su estado a CANCELADO y lo elimina de la cola con removeIf().
     * Permanece en el registro general y en el Map como evidencia.
     *
     * @param codigoServicio codigo del equipo a cancelar
     * @throws IllegalArgumentException si el codigo no existe
     * @throws IllegalStateException    si el equipo no esta PENDIENTE
     */
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

    /**
     * Deshace el ultimo procesamiento: saca el ultimo equipo del Deque (LIFO),
     * lo vuelve a PENDIENTE y lo reingresa a la cola FIFO.
     *
     * @return el equipo restaurado a pendiente
     * @throws IllegalStateException si el historial esta vacio
     */
    public EquipoReparacion deshacer() {
        if (historial.isEmpty()) {
            throw new IllegalStateException("No hay operaciones para deshacer");
        }
        EquipoReparacion equipo = historial.pop();
        equipo.setEstado("PENDIENTE");
        pendientes.offer(equipo);
        return equipo;
    }

    // -- Metodos de consulta basicos --

    /** Retorna copia de la lista general de todos los equipos. */
    public List<EquipoReparacion> obtenerTodos() {
        return new ArrayList<>(registroGeneral);
    }

    /** Retorna copia de la cola de pendientes. */
    public List<EquipoReparacion> obtenerPendientes() {
        return new ArrayList<>(pendientes);
    }

    /** Retorna copia del historial de procesados. */
    public List<EquipoReparacion> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    /** Busqueda directa por clave en el HashMap. */
    public EquipoReparacion buscarPorCodigo(String codigoServicio) {
        return mapaEquipos.get(codigoServicio);
    }

    /** Cantidad total de equipos registrados. */
    public int cantidadTotal() {
        return registroGeneral.size();
    }

    // -- Metodos con Stream (programacion funcional) --

    /**
     * Busqueda por nombre del cliente usando filter() + findFirst().
     * Retorna el primer equipo cuyo cliente coincida (ignora mayusculas).
     */
    public EquipoReparacion buscarPorCriterioAlternativo(String criterio) {
        return registroGeneral.stream()
                .filter(e -> e.getNombreCliente().equalsIgnoreCase(criterio))
                .findFirst()
                .orElse(null);
    }

    /** Filtra equipos por estado usando filter() + toList(). */
    public List<EquipoReparacion> filtrarPorEstado(String estado) {
        return registroGeneral.stream()
                .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    /** Filtra equipos por tipo usando filter() + toList(). */
    public List<EquipoReparacion> filtrarPorTipo(String tipo) {
        return registroGeneral.stream()
                .filter(e -> e.getTipoEquipo().equalsIgnoreCase(tipo))
                .toList();
    }

    /** Orden ascendente por codigo de servicio usando sorted(). */
    public List<EquipoReparacion> ordenarPorCodigo() {
        return registroGeneral.stream()
                .sorted()
                .toList();
    }

    /** Orden alfabetico por nombre del cliente usando sorted(Comparator). */
    public List<EquipoReparacion> ordenarPorNombreCliente() {
        return registroGeneral.stream()
                .sorted(Comparator.comparing(EquipoReparacion::getNombreCliente))
                .toList();
    }

    /** Cuenta cuantos equipos estan en un estado dado: filter() + count(). */
    public long contarPorEstado(String estado) {
        return registroGeneral.stream()
                .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    /** Extrae solo los codigos de servicio con map() + toList(). */
    public List<String> obtenerCodigosRegistrados() {
        return registroGeneral.stream()
                .map(EquipoReparacion::getCodigoServicio)
                .toList();
    }

    /** anyMatch(): true si al menos un equipo esta PENDIENTE. */
    public boolean existeAlgunPendiente() {
        return registroGeneral.stream()
                .anyMatch(e -> "PENDIENTE".equals(e.getEstado()));
    }

    /** allMatch(): true si todos los equipos tienen codigo no nulo ni vacio. */
    public boolean todosTienenCodigo() {
        return registroGeneral.stream()
                .allMatch(e -> e.getCodigoServicio() != null && !e.getCodigoServicio().isEmpty());
    }

    /** noneMatch(): true si ningun equipo esta PROCESADO. */
    public boolean ningunoProcesado() {
        return registroGeneral.stream()
                .noneMatch(e -> "PROCESADO".equals(e.getEstado()));
    }

    /** Agrupa equipos por estado: Collectors.groupingBy(). */
    public Map<String, List<EquipoReparacion>> agruparPorEstado() {
        return registroGeneral.stream()
                .collect(Collectors.groupingBy(EquipoReparacion::getEstado));
    }

    /** Agrupa equipos por tipo: Collectors.groupingBy(). */
    public Map<String, List<EquipoReparacion>> agruparPorTipo() {
        return registroGeneral.stream()
                .collect(Collectors.groupingBy(EquipoReparacion::getTipoEquipo));
    }

    /** Estadisticas: groupingBy con counting() para contar por estado. */
    public Map<String, Long> estadisticasPorEstado() {
        return registroGeneral.stream()
                .collect(Collectors.groupingBy(EquipoReparacion::getEstado, Collectors.counting()));
    }

    /** Reconstruye el Map desde la List usando toMap() con manejo de duplicados. */
    public Map<String, EquipoReparacion> construirMapaDesdeLista() {
        return registroGeneral.stream()
                .collect(Collectors.toMap(
                        EquipoReparacion::getCodigoServicio,
                        e -> e,
                        (existente, nuevo) -> existente));
    }

    /** Recorre una lista con forEach() e imprime cada equipo. */
    public void mostrarEquipos(List<EquipoReparacion> equipos) {
        equipos.forEach(System.out::println);
    }
}
