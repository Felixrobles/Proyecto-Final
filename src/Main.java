import javax.swing.SwingUtilities;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look and Feel del sistema
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new VentanaPrincipal().setVisible(true);
        });
    }
}