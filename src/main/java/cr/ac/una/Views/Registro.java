package cr.ac.una.Views;

import javax.swing.*;
import java.awt.*;

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

    public Registro() {
        UserTF.setPreferredSize(new Dimension(200, 30));
        PasswordTF.setPreferredSize(new Dimension(200, 30));
        RegistrarseBTN.setPreferredSize(new Dimension(100, 30));

        setContentPane(MainPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/LogIn.png"));
        setIconImage(icon.getImage());
        setTitle("Iniciar Sesion");
        setSize(700, 600);
        setLocation(550, 140);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        RegistrarseBTN.addActionListener(e -> onClickRegistrarseBTN());
    }

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

    public void onClickChangeLabel(){
        ChangeLabel.setBackground(Color.GREEN);
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }
}
