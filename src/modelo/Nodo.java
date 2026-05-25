package modelo;

public class Nodo {
    private Alumno alumno;
    private Nodo siguiente;

    public Nodo(Alumno alumno) {
        this.alumno = alumno;
        this.siguiente = null;
    }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }
    public Nodo getSiguiente() { return siguiente; }
    public void setSiguiente(Nodo siguiente) { this.siguiente = siguiente; }
}