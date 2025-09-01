package cr.ac.una.presentation_layer.Views;

import cr.ac.una.presentation_layer.Controller.AdminController;

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
    private JPasswordField PasswordTF;
    
    private JComboBox<String> userTypeCombo;

    private AdminController adminController;

    // Constructor
    public Registro() {
        initializeComponents();
        setupUI();
        addListeners();
    }
    
    private void initializeComponents() {
        // Inicializar el combo box solo con Administrador
        userTypeCombo = new JComboBox<>();
        userTypeCombo.addItem("Administrador");

        UserTF.setPreferredSize(new Dimension(200, 30));
        PasswordTF.setPreferredSize(new Dimension(200, 30));
        RegistrarseBTN.setPreferredSize(new Dimension(100, 30));
        RegistrarseBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private void setupUI() {
        setContentPane(MainPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/LogIn.png"));
        setIconImage(icon.getImage());
        setTitle("Iniciar Sesion");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Solo mostrar opción de Administrador
        userTypeCombo.removeAllItems();
        userTypeCombo.addItem("Administrador");
        userTypeCombo.setSelectedIndex(0);

        setVisible(true);
    }
    
    private void addListeners() {
        RegistrarseBTN.addActionListener(e -> onLogin());
        UserTF.addActionListener(e -> onLogin());
        PasswordTF.addActionListener(e -> onLogin());
    }
    
    private void onLogin() {
        String idText = UserTF.getText().trim();
        String password = new String(PasswordTF.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (idText.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            authenticateUser(id, password, userType);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El ID debe ser un número válido",
                "ID inválido",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void authenticateUser(int id, String password, String userType) {
        authenticateAdmin(id, password);
    }
    
    private void authenticateAdmin(int id, String password) {
        if (adminController != null) {
            boolean autenticado = adminController.autenticar(id, password);
            if (autenticado) {
                    PanelAdministrador panelAdmin = new PanelAdministrador();
                    panelAdmin.setVisible(true);
                    this.dispose(); // Cerrar ventana de login
            } else {
                JOptionPane.showMessageDialog(this,
                    "ID o contraseña incorrectos para Administrador",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Error: Controlador de administrador no disponible",
                "Error del sistema",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }
    
    public JPanel getMainPanel() { return MainPanel; }
}
