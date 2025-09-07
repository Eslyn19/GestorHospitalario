package cr.ac.una.presentation_layer.Views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Despacho extends JPanel {
    private JPanel MainPanel;
    private JTextField BuscarPacienteTF;
    private JComboBox RecetasCB;
    private JButton LimpiarBTN;
    private JButton actualizarRecetaBTN;
    private JPanel PanelComponentes;
    private JPanel PanelBotones;
    private JLabel PacienteIDLabel;
    private JLabel RecetasLabel;
    private JLabel EstadoLabel;
    private JComboBox EstadosCB;
    private JButton BuscarPacienteBTN;
    private JPanel PanelLimpiar;
    private JPanel PanelActualizar;
    private JPanel PanelBuscar;
    private JPanel PanelBuscarPaciente;
    private JPanel PanelBuscarReceta;
    private JPanel PanelEstadoReceta;
    private JPanel CenterPanel;
    private JPanel PacienteMainPanel;
    private JPanel DoublePacienteLabel;
    private JPanel RecetasMainPanel;
    private JPanel RecetasPanel;
    private JPanel EstadoPanel;
    private JPanel MainBotonPanel;
    private JPanel ActualizarPanel;
    private JPanel LimpiarPanel;

    public Despacho() {
        Color white = new Color(0, 0, 40);
        configurarPanel(PanelBotones, "Indicadores", white, new Font("Arial", Font.BOLD, 13), Color.BLACK);
        configurarPanel(PacienteMainPanel, "Buscar Paciente", white, new Font("Arial", Font.BOLD, 13), Color.BLACK);
        configurarPanel(RecetasMainPanel, "Gestor estado", white, new Font("Arial", Font.BOLD, 13), Color.BLACK);
        BuscarPacienteTF.setPreferredSize(new Dimension(200, 30));
        BuscarPacienteBTN.setPreferredSize(new Dimension(20, 20));
    }
    
    private void configurarPanel(JPanel panel, String titulo, Color bordeColor, Font fuente, Color tituloColor) {
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(bordeColor, 1),
                        titulo,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        fuente,
                        tituloColor
                )
        );
    }

    public JPanel getPanel() { return MainPanel; }
    
    // Getters & setters
    public JTextField getBuscarPacienteTF() { return BuscarPacienteTF; }
    public JComboBox getRecetasCB() { return RecetasCB; }
    public JComboBox getEstadosCB() { return EstadosCB; }
    public JButton getBuscarPacienteBTN() { return BuscarPacienteBTN; }
    public JButton getActualizarRecetaBTN() { return actualizarRecetaBTN; }
    public JButton getLimpiarBTN() { return LimpiarBTN; }
}
