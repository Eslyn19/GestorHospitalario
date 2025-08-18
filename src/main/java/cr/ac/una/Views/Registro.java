package cr.ac.una.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Registro extends JFrame {
    private JPanel MainPanel;
    private JPanel LogInPanel;
    private JPanel WelcomePanel;
    private JPanel SpacesPanel;
    private JPanel UtilPanel;
    private JLabel BienvenidoLabel;
    private JTextField UserTF;
    private JButton RegistrarseBTN;
    private JLabel ChangeLabel;
    private JPasswordField PasswordTF;

    // Constructor
    public Registro() {
        UserTF.setPreferredSize(new Dimension(200, 30));
        PasswordTF.setPreferredSize(new Dimension(200, 30));
        RegistrarseBTN.setPreferredSize(new Dimension(100, 30));
        RegistrarseBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ChangeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setContentPane(MainPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/LogIn.png"));
        setIconImage(icon.getImage());
        setTitle("Iniciar Sesion");
        setSize(700, 600);
        setLocation(550, 140);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        RegistrarseBTN.addActionListener(e -> onClickRegistrarseBTN());
        ChangeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Opcional
                dispose();
                CambiarCtr CC = new CambiarCtr();
                CC.setVisible(true);
            }
        });
    }

    public JPanel getMainPanel() { return MainPanel; }

    // Metodó para iniciar y validar sección
    public void onClickRegistrarseBTN() {
        String user = UserTF.getText().trim();
        String pass = PasswordTF.getText().trim();

        try {
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
