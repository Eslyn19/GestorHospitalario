package cr.ac.una.presentation_layer.Views.AcercaDe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AcercaDeView {

    private final JPanel PanelBase;
    private final JPanel content;

    public AcercaDeView() {
        PanelBase = new JPanel(new BorderLayout());
        PanelBase.setBorder(new EmptyBorder(12, 12, 12, 12));
        PanelBase.setBackground(Color.WHITE);

        // titulo
        JLabel title = new JLabel("Prescripción y Despacho de Recetas", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(20, 60, 160));
        title.setBorder(new EmptyBorder(6, 6, 6, 6));
        PanelBase.add(title, BorderLayout.NORTH);

        // texto informativo alineado y con espacio
        content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(8, 24, 8, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Línea con subtítulo
        JLabel subtitle = new JLabel("Ultima modificacion: 2 sep 2025 7:00 pm", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(new Color(60, 60, 60));
        content.add(subtitle, gbc);
        PanelBase.add(content, BorderLayout.CENTER);

    }

    public JPanel getPanelBase() {
        return PanelBase;
    }
}
