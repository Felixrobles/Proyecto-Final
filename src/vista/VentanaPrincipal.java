package vista;

import controlador.ControladorArchivo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.*;

public class VentanaPrincipal extends JFrame {
    // Listas enlazadas para diferentes órdenes
    private ListaEnlazada listaPorNombre;
    private ListaEnlazada listaPorCarnet;
    private ListaEnlazada listaPorNotaDesc;
    private ListaEnlazada listaOriginal;
    
    // Componentes UI
    private JTable tablaDatos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaSimulacion;
    private JLabel lblEstado;
    private String nombreArchivoActual;
    
    public VentanaPrincipal() {
        setTitle("Proyecto 3 - Simulación de Listas Enlazadas Ordenadas");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar listas
        listaPorNombre = new ListaEnlazada(alumno -> alumno.getNombre().toLowerCase());
        listaPorCarnet = new ListaEnlazada(alumno -> alumno.getCarnet());
        listaPorNotaDesc = new ListaEnlazada(alumno -> -alumno.getNota());
        listaOriginal = new ListaEnlazada();
        
        // Inicializar componentes
        inicializarComponentes();
        
        // Verificar conexión a BD
        DatabaseConnection.crearTablaSiNoExiste();
        
        lblEstado.setText("✅ Listo. Cargue un archivo para comenzar.");
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] opciones = {
            "1. Cargar Archivo", "2. Cargar a Listas", "3. Ver por Nombre",
            "4. Ver por Carnet", "5. Ver por Nota ↓", "6. Notas Altas/Bajas",
            "7. Promedios", "8. Salir"
        };
        
        for (String opcion : opciones) {
            JButton btn = new JButton(opcion);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.addActionListener(new ManejadorOpciones(opcion.charAt(0) + ""));
            panelBotones.add(btn);
        }
        
        add(panelBotones, BorderLayout.NORTH);
        
        // Tabla de datos
        modeloTabla = new DefaultTableModel(new String[]{
            "Carnet", "Nombre", "Código Curso", "Sección", "Nota"
        }, 0);
        tablaDatos = new JTable(modeloTabla);
        tablaDatos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tablaDatos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollTabla = new JScrollPane(tablaDatos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Datos de Alumnos"));
        add(scrollTabla, BorderLayout.CENTER);
        
        // Área de simulación
        areaSimulacion = new JTextArea(8, 80);
        areaSimulacion.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaSimulacion.setEditable(false);
        areaSimulacion.setBackground(new Color(240, 240, 255));
        JScrollPane scrollSimulacion = new JScrollPane(areaSimulacion);
        scrollSimulacion.setBorder(BorderFactory.createTitledBorder("Simulación de Lista Enlazada"));
        add(scrollSimulacion, BorderLayout.SOUTH);
        
        // Barra de estado
        lblEstado = new JLabel("Esperando acción...");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblEstado.setFont(new Font("Arial", Font.ITALIC, 11));
        add(lblEstado, BorderLayout.SOUTH);
    }
    
    private class ManejadorOpciones implements ActionListener {
        private String opcion;
        
        public ManejadorOpciones(String opcion) {
            this.opcion = opcion;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (opcion) {
                case "1":
                    cargarArchivo();
                    break;
                case "2":
                    cargarDatosAListas();
                    break;
                case "3":
                    mostrarPorNombre();
                    break;
                case "4":
                    mostrarPorCarnet();
                    break;
                case "5":
                    mostrarPorNotaDescendente();
                    break;
                case "6":
                    mostrarNotasAltasYBajas();
                    break;
                case "7":
                    mostrarPromedios();
                    break;
                case "8":
                    salir();
                    break;
            }
        }
    }
    
    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Seleccionar archivo de datos");
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            nombreArchivoActual = fileChooser.getSelectedFile().getAbsolutePath();
            
            try {
                List<Alumno> alumnos = ControladorArchivo.leerArchivo(nombreArchivoActual);
                mostrarEnTabla(alumnos);
                
                // Guardar en lista original
                listaOriginal.vaciar();
                for (Alumno a : alumnos) {
                    listaOriginal.agregarOrdenado(a);
                }
                
                lblEstado.setText("✅ Archivo cargado: " + nombreArchivoActual + " (" + alumnos.size() + " registros)");
                JOptionPane.showMessageDialog(this, 
                    "Archivo cargado exitosamente.\nTotal de registros: " + alumnos.size(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException ex) {
                lblEstado.setText("❌ Error al leer el archivo");
                JOptionPane.showMessageDialog(this, 
                    "Error al leer el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarDatosAListas() {
        if (listaOriginal.estaVacia()) {
            JOptionPane.showMessageDialog(this, 
                "Primero debe cargar un archivo (Opción 1)", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Vaciar listas
        listaPorNombre.vaciar();
        listaPorCarnet.vaciar();
        listaPorNotaDesc.vaciar();
        
        // Cargar datos ordenadamente
        List<Alumno> alumnos = listaOriginal.obtenerTodos();
        for (Alumno a : alumnos) {
            listaPorNombre.agregarOrdenado(a);
            listaPorCarnet.agregarOrdenado(a);
            listaPorNotaDesc.agregarOrdenado(a);
        }
        
        // Mostrar simulación
        areaSimulacion.setText(
            "=== SIMULACIÓN DE LISTAS ENLAZADAS ===\n\n" +
            "📋 Lista ordenada por NOMBRE:\n" + listaPorNombre.dibujarComoTabla() + "\n\n" +
            "🎯 Tamaño de la lista: " + listaPorNombre.getTamaño() + " nodos\n" +
            "🔗 Estructura enlazada: " + listaPorNombre.dibujarLista()
        );
        
        lblEstado.setText("✅ Datos cargados a las listas enlazadas ordenadas");
        JOptionPane.showMessageDialog(this, 
            "Datos cargados exitosamente a las listas enlazadas.\n" +
            "Total de nodos creados: " + listaPorNombre.getTamaño(),
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarPorNombre() {
        if (listaPorNombre.estaVacia()) {
            JOptionPane.showMessageDialog(this, 
                "Primero debe cargar datos a las listas (Opción 2)", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Alumno> alumnos = listaPorNombre.obtenerTodos();
        mostrarEnTabla(alumnos);
        
        areaSimulacion.setText(
            "📖 MOSTRANDO: Ordenado por NOMBRE (A-Z)\n" +
            "=====================================\n" +
            listaPorNombre.dibujarComoTabla()
        );
        
        lblEstado.setText("📖 Mostrando datos ordenados por Nombre");
    }
    
    private void mostrarPorCarnet() {
        if (listaPorCarnet.estaVacia()) {
            JOptionPane.showMessageDialog(this, 
                "Primero debe cargar datos a las listas (Opción 2)", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Alumno> alumnos = listaPorCarnet.obtenerTodos();
        mostrarEnTabla(alumnos);
        
        areaSimulacion.setText(
            "🆔 MOSTRANDO: Ordenado por CARNET (Ascendente)\n" +
            "============================================\n" +
            "┌──────────────┬──────────────────┬────────┐\n" +
            "│    Carnet     │      Nombre       │  Nota  │\n" +
            "├──────────────┼──────────────────┼────────┤\n"
        );
        
        for (Alumno a : alumnos) {
            areaSimulacion.append(String.format("│ %-12d │ %-16s │ %6.2f │\n", 
                a.getCarnet(), 
                a.getNombre().length() > 16 ? a.getNombre().substring(0, 16) : a.getNombre(),
                a.getNota()));
        }
        areaSimulacion.append("└──────────────┴──────────────────┴────────┘");
        
        lblEstado.setText("🆔 Mostrando datos ordenados por Carnet");
    }
    
    private void mostrarPorNotaDescendente() {
        if (listaPorNotaDesc.estaVacia()) {
            JOptionPane.showMessageDialog(this, 
                "Primero debe cargar datos a las listas (Opción 2)", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Alumno> alumnos = listaPorNotaDesc.obtenerTodos();
        mostrarEnTabla(alumnos);
        
        areaSimulacion.setText(
            "⭐ MOSTRANDO: Ordenado por NOTA (Descendente - mayor a menor)\n" +
            "==========================================================\n" +
            "┌──────────┬──────────────────┬──────────────┐\n" +
            "│   Nota   │      Nombre       │    Carnet    │\n" +
            "├──────────┼──────────────────┼──────────────┤\n"
        );
        
        for (Alumno a : alumnos) {
            areaSimulacion.append(String.format("│ %6.2f   │ %-16s │ %-12d │\n", 
                a.getNota(),
                a.getNombre().length() > 16 ? a.getNombre().substring(0, 16) : a.getNombre(),
                a.getCarnet()));
        }
        areaSimulacion.append("└──────────┴──────────────────┴──────────────┘\n\n");
        areaSimulacion.append("🔗 Estructura enlazada (por nota):\n");
        areaSimulacion.append(listaPorNotaDesc.dibujarLista());
        
        lblEstado.setText("⭐ Mostrando datos ordenados por Nota (Mayor a Menor)");
    }
    
    private void mostrarNotasAltasYBajas() {
        if (listaOriginal.estaVacia()) {
            JOptionPane.showMessageDialog(this, 
                "Primero debe cargar un archivo (Opción 1)", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        new DialogoNotas(this, listaOriginal);
    }
    
    private void mostrarPromedios() {
        if (listaOriginal.estaVacia()) {
            JOptionPane.showMessageDialog(this, 
                "Primero debe cargar un archivo (Opción 1)", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<String> cursosSecciones = listaOriginal.obtenerCursosYSecciones();
        
        StringBuilder sb = new StringBuilder();
        sb.append("📊 PROMEDIOS POR CURSO Y SECCIÓN\n");
        sb.append("================================\n\n");
        
        for (String clave : cursosSecciones) {
            String[] partes = clave.split("_");
            String curso = partes[0];
            String seccion = partes[1];
            double promedio = listaOriginal.obtenerPromedio(curso, seccion);
            sb.append(String.format("📚 Curso: %s | Sección: %s → Promedio: %.2f\n", 
                curso, seccion, promedio));
        }
        
        areaSimulacion.setText(sb.toString());
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Promedios", JOptionPane.INFORMATION_MESSAGE);
        lblEstado.setText("📊 Mostrando promedios por curso y sección");
    }
    
    private void mostrarEnTabla(List<Alumno> alumnos) {
        modeloTabla.setRowCount(0);
        for (Alumno a : alumnos) {
            modeloTabla.addRow(new Object[]{
                a.getCarnet(), a.getNombre(), a.getCodigoCurso(), a.getSeccion(), a.getNota()
            });
        }
    }
    
    private void salir() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea salir?", 
            "Confirmar salida", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnection.cerrarConexion();
            System.exit(0);
        }
    }
}