package cr.ac.una.presentation_layer.Views;

import cr.ac.una.data_access_layer.DoctorFileStore;
import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Model.DoctorTableModel;
import cr.ac.una.service_layer.DoctorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
        setLocationRelativeTo(null);
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

    // Método para iniciar y validar sesión
    public void onClickRegistrarseBTN() {
        String user = UserTF.getText().trim();
        String pass = new String(PasswordTF.getPassword()).trim();

        try {
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            // Validar credenciales de administrador
            if (!user.equals("admin")) {
                JOptionPane.showMessageDialog(this, "Usuario incorrecto. Solo se permite el usuario 'admin'.");
                return;
            }
            
            if (!pass.equals("root")) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta. La contraseña debe ser 'root'.");
                return;
            }

            // Autenticación exitosa
            JOptionPane.showMessageDialog(this, 
                "¡Inicio de sesión exitoso!\nBienvenido Administrador", 
                "Acceso Autorizado", JOptionPane.INFORMATION_MESSAGE);

            // Close login window and open main panel
            dispose();
            openMainPanel();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openMainPanel() {
        try {
            // Open main panel with tabbed interface
            PanelPrincipal mainPanel = new PanelPrincipal();
            mainPanel.selectDoctorTab(); // Automatically select doctors tab
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al abrir el panel principal: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
