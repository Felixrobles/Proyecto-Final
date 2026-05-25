package vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Alumno;
import modelo.ListaEnlazada;

public class DialogoNotas extends JDialog {
    private JTable tablaAltas;
    private JTable tablaBajas;
    
    public DialogoNotas(JFrame parent, ListaEnlazada lista) {
        super(parent, "Notas más Altas y más Bajas", true);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Panel de notas altas
        JPanel panelAltas = new JPanel(new BorderLayout());
        panelAltas.setBorder(BorderFactory.createTitledBorder("🏆 5 Notas más Altas"));
        
        DefaultTableModel modelAltas = new DefaultTableModel(new String[]{
            "Nota", "Nombre", "Carnet", "Curso", "Sección"
        }, 0);
        tablaAltas = new JTable(modelAltas);
        
        List<Alumno> top5 = lista.obtenerTop5Notas();
        for (Alumno a : top5) {
            modelAltas.addRow(new Object[]{
                a.getNota(), a.getNombre(), a.getCarnet(), a.getCodigoCurso(), a.getSeccion()
            });
        }
        
        panelAltas.add(new JScrollPane(tablaAltas), BorderLayout.CENTER);
        
        // Panel de notas bajas
        JPanel panelBajas = new JPanel(new BorderLayout());
        panelBajas.setBorder(BorderFactory.createTitledBorder("📉 5 Notas más Bajas"));
        
        DefaultTableModel modelBajas = new DefaultTableModel(new String[]{
            "Nota", "Nombre", "Carnet", "Curso", "Sección"
        }, 0);
        tablaBajas = new JTable(modelBajas);
        
        List<Alumno> bottom5 = lista.obtenerBottom5Notas();
        for (Alumno a : bottom5) {
            modelBajas.addRow(new Object[]{
                a.getNota(), a.getNombre(), a.getCarnet(), a.getCodigoCurso(), a.getSeccion()
            });
        }
        
        panelBajas.add(new JScrollPane(tablaBajas), BorderLayout.CENTER);
        
        panelPrincipal.add(panelAltas);
        panelPrincipal.add(panelBajas);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);
        
        setVisible(true);
    }
}