package com.taller;

import com.taller.modelo.EquipoReparacion;
import com.taller.servicio.GestorTaller;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Clase principal con menu interactivo de consola.
 * Ofrece 15 opciones para gestionar el taller de computadores
 * usando las colecciones del SDK y Stream.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final GestorTaller gestor = new GestorTaller();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();
            ejecutarOpcion(opcion);
        } while (opcion != 15);
        System.out.println("Saliendo del sistema...");
    }

    /** Muestra las 15 opciones del menu principal. */
    private static void mostrarMenu() {
        System.out.println("\n========= TALLER DE COMPUTADORES =========");
        System.out.println("1.  Registrar equipo");
        System.out.println("2.  Ver todos los equipos registrados");
        System.out.println("3.  Ver equipos pendientes");
        System.out.println("4.  Procesar siguiente equipo");
        System.out.println("5.  Ver historial de equipos procesados");
        System.out.println("6.  Buscar equipo por codigo de servicio (Map)");
        System.out.println("7.  Buscar equipo por criterio alternativo (Stream)");
        System.out.println("8.  Filtrar equipos por estado (Stream)");
        System.out.println("9.  Ordenar equipos (Stream)");
        System.out.println("10. Ver estadisticas (Stream + Map)");
        System.out.println("11. Ver agrupamientos (Stream + Map)");
        System.out.println("12. Cancelar equipo pendiente");
        System.out.println("13. Deshacer ultimo procesamiento");
        System.out.println("14. Ver cantidad de equipos");
        System.out.println("15. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    /** Lee y parsea la opcion ingresada; retorna -1 si no es un numero. */
    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Enruta la opcion al metodo correspondiente con try-catch general. */
    private static void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1 -> registrarEquipo();
                case 2 -> verTodos();
                case 3 -> verPendientes();
                case 4 -> procesarEquipo();
                case 5 -> verHistorial();
                case 6 -> buscarPorCodigo();
                case 7 -> buscarPorCriterio();
                case 8 -> filtrarPorEstado();
                case 9 -> ordenarEquipos();
                case 10 -> verEstadisticas();
                case 11 -> verAgrupamientos();
                case 12 -> cancelarEquipo();
                case 13 -> deshacerProcesamiento();
                case 14 -> verCantidad();
                case 15 -> {}
                default -> System.out.println("Opcion no valida");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // -- Metodos de cada opcion del menu --

    /** Opcion 1: Solicita datos al usuario y registra un nuevo equipo. */
    private static void registrarEquipo() {
        System.out.print("Codigo de servicio: ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine().trim();
        System.out.print("Descripcion del problema: ");
        String problema = scanner.nextLine().trim();
        System.out.print("Tipo de equipo (LAPTOP/DESKTOP/TABLET): ");
        String tipo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Tecnico asignado: ");
        String tecnico = scanner.nextLine().trim();

        EquipoReparacion equipo = new EquipoReparacion(codigo, cliente, problema, tipo, "PENDIENTE", tecnico);
        gestor.registrar(equipo);
        System.out.println("Equipo registrado exitosamente");
    }

    /** Opcion 2: Muestra todos los equipos del registro general. */
    private static void verTodos() {
        List<EquipoReparacion> lista = gestor.obtenerTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay equipos registrados");
            return;
        }
        gestor.mostrarEquipos(lista);
    }

    /** Opcion 3: Muestra solo los equipos en cola de pendientes. */
    private static void verPendientes() {
        List<EquipoReparacion> lista = gestor.obtenerPendientes();
        if (lista.isEmpty()) {
            System.out.println("No hay equipos pendientes");
            return;
        }
        gestor.mostrarEquipos(lista);
    }

    /** Opcion 4: Procesa el primer equipo de la cola FIFO. */
    private static void procesarEquipo() {
        EquipoReparacion equipo = gestor.procesarSiguiente();
        System.out.println("Equipo procesado: " + equipo.getCodigoServicio()
                + " - " + equipo.getNombreCliente());
    }

    /** Opcion 5: Muestra el historial LIFO de equipos procesados. */
    private static void verHistorial() {
        List<EquipoReparacion> historial = gestor.obtenerHistorial();
        if (historial.isEmpty()) {
            System.out.println("No hay equipos en el historial");
            return;
        }
        gestor.mostrarEquipos(historial);
    }

    /** Opcion 6: Busqueda directa por codigo usando el HashMap. */
    private static void buscarPorCodigo() {
        System.out.print("Ingrese el codigo de servicio: ");
        String codigo = scanner.nextLine().trim();
        EquipoReparacion equipo = gestor.buscarPorCodigo(codigo);
        if (equipo == null) {
            System.out.println("No se encontro equipo con codigo " + codigo);
        } else {
            System.out.println(equipo);
        }
    }

    /** Opcion 7: Busqueda por nombre del cliente usando Stream filter+findFirst. */
    private static void buscarPorCriterio() {
        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scanner.nextLine().trim();
        EquipoReparacion equipo = gestor.buscarPorCriterioAlternativo(nombre);
        if (equipo == null) {
            System.out.println("No se encontro equipo para el cliente " + nombre);
        } else {
            System.out.println(equipo);
        }
    }

    /** Opcion 8: Filtra equipos por estado usando Stream filter+toList. */
    private static void filtrarPorEstado() {
        System.out.print("Ingrese el estado (PENDIENTE/PROCESADO/CANCELADO): ");
        String estado = scanner.nextLine().trim().toUpperCase();
        List<EquipoReparacion> resultado = gestor.filtrarPorEstado(estado);
        if (resultado.isEmpty()) {
            System.out.println("No hay equipos en estado " + estado);
        } else {
            gestor.mostrarEquipos(resultado);
        }
    }

    /** Opcion 9: Submenu para ordenar por codigo o por nombre del cliente. */
    private static void ordenarEquipos() {
        System.out.println("1. Ordenar por codigo");
        System.out.println("2. Ordenar por nombre del cliente");
        System.out.print("Seleccione criterio de orden: ");
        String entrada = scanner.nextLine().trim();
        List<EquipoReparacion> resultado;
        switch (entrada) {
            case "1" -> resultado = gestor.ordenarPorCodigo();
            case "2" -> resultado = gestor.ordenarPorNombreCliente();
            default -> {
                System.out.println("Opcion no valida");
                return;
            }
        }
        if (resultado.isEmpty()) {
            System.out.println("No hay equipos para ordenar");
        } else {
            gestor.mostrarEquipos(resultado);
        }
    }

    /** Opcion 10: Muestra estadisticas, conteos y banderas usando Stream. */
    private static void verEstadisticas() {
        System.out.println("--- Estadisticas por estado ---");
        Map<String, Long> stats = gestor.estadisticasPorEstado();
        stats.forEach((estado, cantidad) ->
                System.out.println(estado + ": " + cantidad));

        System.out.println("\n--- Conteos individuales ---");
        System.out.println("Pendientes: " + gestor.contarPorEstado("PENDIENTE"));
        System.out.println("Procesados: " + gestor.contarPorEstado("PROCESADO"));
        System.out.println("Cancelados: " + gestor.contarPorEstado("CANCELADO"));

        System.out.println("Hay pendientes: " + (gestor.existeAlgunPendiente() ? "Si" : "No"));
        System.out.println("Todos los equipos tienen codigo: "
                + (gestor.todosTienenCodigo() ? "Si" : "No"));
        System.out.println("Ninguno procesado: "
                + (gestor.ningunoProcesado() ? "Si" : "No"));
    }

    /** Opcion 11: Muestra agrupamientos por estado y por tipo (Stream). */
    private static void verAgrupamientos() {
        System.out.println("--- Agrupamiento por estado ---");
        Map<String, List<EquipoReparacion>> porEstado = gestor.agruparPorEstado();
        porEstado.forEach((estado, lista) -> {
            System.out.println("\n" + estado + " (" + lista.size() + "):");
            lista.forEach(e -> System.out.println("  - " + e.getCodigoServicio() + " | " + e.getNombreCliente()));
        });

        System.out.println("\n--- Agrupamiento por tipo de equipo ---");
        Map<String, List<EquipoReparacion>> porTipo = gestor.agruparPorTipo();
        porTipo.forEach((tipo, lista) -> {
            System.out.println("\n" + tipo + " (" + lista.size() + "):");
            lista.forEach(e -> System.out.println("  - " + e.getCodigoServicio() + " | " + e.getNombreCliente()));
        });
    }

    /** Opcion 12: Cancela un equipo pendiente por su codigo. */
    private static void cancelarEquipo() {
        System.out.print("Ingrese el codigo del equipo a cancelar: ");
        String codigo = scanner.nextLine().trim();
        gestor.cancelar(codigo);
        System.out.println("Equipo " + codigo + " cancelado exitosamente");
    }

    /** Opcion 13: Deshace el ultimo procesamiento (LIFO). */
    private static void deshacerProcesamiento() {
        EquipoReparacion equipo = gestor.deshacer();
        System.out.println("Procesamiento deshecho. Equipo " + equipo.getCodigoServicio()
                + " vuelve a estado PENDIENTE");
    }

    /** Opcion 14: Muestra la cantidad total de equipos registrados. */
    private static void verCantidad() {
        System.out.println("Total de equipos registrados: " + gestor.cantidadTotal());
    }
}
