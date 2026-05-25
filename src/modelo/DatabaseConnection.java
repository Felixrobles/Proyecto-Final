package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_listas";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Cambia por tu contraseña
    
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS alumnos (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY," +
                     "carnet BIGINT UNIQUE NOT NULL," +
                     "nombre VARCHAR(100) NOT NULL," +
                     "codigo_curso VARCHAR(10) NOT NULL," +
                     "seccion CHAR(1) NOT NULL," +
                     "nota DECIMAL(5,2) NOT NULL" +
                     ")";
        
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'alumnos' verificada/creada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }
    
    public static void guardarAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumnos (carnet, nombre, codigo_curso, seccion, nota) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, alumno.getCarnet());
            pstmt.setString(2, alumno.getNombre());
            pstmt.setString(3, alumno.getCodigoCurso());
            pstmt.setString(4, alumno.getSeccion());
            pstmt.setDouble(5, alumno.getNota());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar alumno: " + e.getMessage());
        }
    }
    
    public static List<Alumno> cargarTodosLosAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos ORDER BY id";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Alumno a = new Alumno(
                    rs.getLong("carnet"),
                    rs.getString("nombre"),
                    rs.getString("codigo_curso"),
                    rs.getString("seccion"),
                    rs.getDouble("nota")
                );
                alumnos.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar alumnos: " + e.getMessage());
        }
        return alumnos;
    }
    
    public static void limpiarTabla() {
        String sql = "TRUNCATE TABLE alumnos";
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al limpiar tabla: " + e.getMessage());
        }
    }
}