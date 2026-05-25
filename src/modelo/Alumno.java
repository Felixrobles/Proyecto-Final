package modelo;

public class Alumno {
    private long carnet;
    private String nombre;
    private String codigoCurso;
    private String seccion;
    private double nota;

    public Alumno(long carnet, String nombre, String codigoCurso, String seccion, double nota) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.codigoCurso = codigoCurso;
        this.seccion = seccion;
        this.nota = nota;
    }

    // Getters
    public long getCarnet() { return carnet; }
    public String getNombre() { return nombre; }
    public String getCodigoCurso() { return codigoCurso; }
    public String getSeccion() { return seccion; }
    public double getNota() { return nota; }

    // Setters
    public void setCarnet(long carnet) { this.carnet = carnet; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodigoCurso(String codigoCurso) { this.codigoCurso = codigoCurso; }
    public void setSeccion(String seccion) { this.seccion = seccion; }
    public void setNota(double nota) { this.nota = nota; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s | %.2f", carnet, nombre, codigoCurso, seccion, nota);
    }
    
    // Para obtener clave única por curso+sección
    public String getClaveCursoSeccion() {
        return codigoCurso + "_" + seccion;
    }
}