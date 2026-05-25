package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListaEnlazada {
    private Nodo cabeza;
    private int tamaño;
    private Function<Alumno, Comparable> criterioOrden;

    // Constructor para ordenar por un criterio específico
    public ListaEnlazada(Function<Alumno, Comparable> criterioOrden) {
        this.cabeza = null;
        this.tamaño = 0;
        this.criterioOrden = criterioOrden;
    }

    // Constructor sin orden específico
    public ListaEnlazada() {
        this.cabeza = null;
        this.tamaño = 0;
        this.criterioOrden = null;
    }

    // Agregar ordenado según criterio
    public void agregarOrdenado(Alumno nuevoAlumno) {
        Nodo nuevoNodo = new Nodo(nuevoAlumno);
        
        if (criterioOrden == null) {
            // Si no hay criterio, agregar al final
            agregarAlFinal(nuevoNodo);
            return;
        }

        Comparable valorNuevo = criterioOrden.apply(nuevoAlumno);

        // Caso: lista vacía
        if (cabeza == null) {
            cabeza = nuevoNodo;
            tamaño++;
            return;
        }

        // Caso: insertar al inicio
        Comparable valorCabeza = criterioOrden.apply(cabeza.getAlumno());
        if (valorNuevo.compareTo(valorCabeza) < 0) {
            nuevoNodo.setSiguiente(cabeza);
            cabeza = nuevoNodo;
            tamaño++;
            return;
        }

        // Caso: insertar en medio o al final
        Nodo actual = cabeza;
        while (actual.getSiguiente() != null) {
            Comparable valorSiguiente = criterioOrden.apply(actual.getSiguiente().getAlumno());
            if (valorNuevo.compareTo(valorSiguiente) < 0) {
                break;
            }
            actual = actual.getSiguiente();
        }
        
        nuevoNodo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevoNodo);
        tamaño++;
    }

    private void agregarAlFinal(Nodo nuevoNodo) {
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamaño++;
    }

    // Obtener todos los elementos como lista
    public List<Alumno> obtenerTodos() {
        List<Alumno> lista = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            lista.add(actual.getAlumno());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    // Obtener elementos filtrados por curso y sección
    public List<Alumno> obtenerPorCursoYSeccion(String codigoCurso, String seccion) {
        List<Alumno> lista = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            Alumno a = actual.getAlumno();
            if (a.getCodigoCurso().equals(codigoCurso) && a.getSeccion().equals(seccion)) {
                lista.add(a);
            }
            actual = actual.getSiguiente();
        }
        return lista;
    }

    // Obtener todos los cursos y secciones únicos
    public List<String> obtenerCursosYSecciones() {
        List<String> lista = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            String clave = actual.getAlumno().getClaveCursoSeccion();
            if (!lista.contains(clave)) {
                lista.add(clave);
            }
            actual = actual.getSiguiente();
        }
        return lista;
    }

    // Obtener las 5 notas más altas
    public List<Alumno> obtenerTop5Notas() {
        List<Alumno> todos = obtenerTodos();
        todos.sort((a1, a2) -> Double.compare(a2.getNota(), a1.getNota()));
        return todos.size() > 5 ? todos.subList(0, 5) : todos;
    }

    // Obtener las 5 notas más bajas
    public List<Alumno> obtenerBottom5Notas() {
        List<Alumno> todos = obtenerTodos();
        todos.sort((a1, a2) -> Double.compare(a1.getNota(), a2.getNota()));
        return todos.size() > 5 ? todos.subList(0, 5) : todos;
    }

    // Calcular promedio por curso y sección
    public double obtenerPromedio(String codigoCurso, String seccion) {
        List<Alumno> alumnos = obtenerPorCursoYSeccion(codigoCurso, seccion);
        if (alumnos.isEmpty()) return 0;
        
        double suma = 0;
        for (Alumno a : alumnos) {
            suma += a.getNota();
        }
        return suma / alumnos.size();
    }

    // Vaciar la lista
    public void vaciar() {
        cabeza = null;
        tamaño = 0;
    }

    public int getTamaño() { return tamaño; }
    
    public boolean estaVacia() { return cabeza == null; }

    // Método para simulación gráfica (dibujar en texto)
    public String dibujarLista() {
        StringBuilder sb = new StringBuilder();
        Nodo actual = cabeza;
        int contador = 0;
        
        sb.append("INICIO → ");
        while (actual != null) {
            Alumno a = actual.getAlumno();
            sb.append("[").append(a.getNombre()).append("|").append(a.getNota()).append("]");
            if (actual.getSiguiente() != null) {
                sb.append(" → ");
            }
            actual = actual.getSiguiente();
            contador++;
            if (contador % 4 == 0) {
                sb.append("\n         ");
            }
        }
        sb.append(" → NULL");
        return sb.toString();
    }
    
    // Obtener representación como tabla para simulación
    public String dibujarComoTabla() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌──────────────┬──────────────────┬────────┐\n");
        sb.append("│    Nombre     │      Carnet       │  Nota  │\n");
        sb.append("├──────────────┼──────────────────┼────────┤\n");
        
        Nodo actual = cabeza;
        while (actual != null) {
            Alumno a = actual.getAlumno();
            String nombre = a.getNombre().length() > 12 ? a.getNombre().substring(0, 12) : a.getNombre();
            sb.append(String.format("│ %-12s │ %-16d │ %6.2f │\n", nombre, a.getCarnet(), a.getNota()));
            actual = actual.getSiguiente();
        }
        sb.append("└──────────────┴──────────────────┴────────┘");
        return sb.toString();
    }
}