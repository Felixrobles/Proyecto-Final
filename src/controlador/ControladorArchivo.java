package controlador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Alumno;

public class ControladorArchivo {
    
    public static List<Alumno> leerArchivo(String nombreArchivo) throws IOException {
        List<Alumno> alumnos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                
                // Separar por coma
                String[] partes = linea.split(",");
                if (partes.length >= 5) {
                    try {
                        long carnet = Long.parseLong(partes[0].trim());
                        String nombre = partes[1].trim();
                        String codigoCurso = partes[2].trim();
                        String seccion = partes[3].trim();
                        double nota = Double.parseDouble(partes[4].trim());
                        
                        Alumno alumno = new Alumno(carnet, nombre, codigoCurso, seccion, nota);
                        alumnos.add(alumno);
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en línea: " + linea);
                    }
                }
            }
        }
        
        return alumnos;
    }
    
    public static boolean validarArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        return archivo.exists() && archivo.isFile();
    }
    
    public static void mostrarContenidoArchivo(String nombreArchivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        }
    }
}