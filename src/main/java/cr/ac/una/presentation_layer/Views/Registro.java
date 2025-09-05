package cr.ac.una.presentation_layer.Views;

import cr.ac.una.presentation_layer.Controller.AdminController;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;
import cr.ac.una.service_layer.AdminService;
import cr.ac.una.service_layer.DoctorService;
import cr.ac.una.service_layer.FarmaceutaService;
import cr.ac.una.utilities.FileManagement;

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
    private DoctorController doctorController;
    private FarmaceutaController farmaceutaController;

    // Constructor
    public Registro() {
        initializeComponents();
        setupUI();
        addListeners();
    }
    
    private void initializeComponents() {
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

        // El ComboBox ya está configurado en el .form

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
        if ("Administrador".equals(userType)) {
            authenticateAdmin(id, password);
        } else if ("Doctor".equals(userType)) {
            authenticateDoctor(id, password);
        } else if ("Farmacéutico".equals(userType)) {
            authenticateFarmaceuta(id, password);
        }
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
    
    private void authenticateDoctor(int id, String password) {
        if (doctorController != null) {
            boolean autenticado = doctorController.autenticar(id, password);
            if (autenticado) {
                // Obtener el nombre del doctor
                String nombreDoctor = doctorController.obtenerNombreDoctor(id);
                PanelDoctor panelDoctor = new PanelDoctor(nombreDoctor);
                panelDoctor.setVisible(true);
                this.dispose(); // Cerrar ventana de login
            } else {
                JOptionPane.showMessageDialog(this,
                    "ID o contraseña incorrectos para Doctor",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Error: Controlador de doctor no disponible",
                "Error del sistema",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void authenticateFarmaceuta(int id, String password) {
        if (farmaceutaController != null) {
            boolean autenticado = farmaceutaController.autenticar(id, password);
            if (autenticado) {
                // Obtener el nombre del farmacéutico
                String nombreFarmaceuta = farmaceutaController.obtenerNombreFarmaceuta(id);
                // Por ahora, los farmacéuticos también van al PanelDoctor
                // TODO: Crear PanelFarmaceutico específico si es necesario
                PanelDoctor panelDoctor = new PanelDoctor(nombreFarmaceuta);
                panelDoctor.setVisible(true);
                this.dispose(); // Cerrar ventana de login
            } else {
                JOptionPane.showMessageDialog(this,
                    "ID o contraseña incorrectos para Farmacéutico",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Error: Controlador de farmacéutico no disponible",
                "Error del sistema",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }
    
    public void setDoctorController(DoctorController doctorController) {
        this.doctorController = doctorController;
    }
    
    public void setFarmaceutaController(FarmaceutaController farmaceutaController) {
        this.farmaceutaController = farmaceutaController;
    }
    
    public JPanel getMainPanel() { return MainPanel; }
}
