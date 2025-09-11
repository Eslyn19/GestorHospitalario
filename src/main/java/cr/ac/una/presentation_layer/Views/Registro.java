package cr.ac.una.presentation_layer.Views;

import cr.ac.una.presentation_layer.Controller.AdminController;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;

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
    private JPasswordField PasswordTF;
    private JComboBox<String> userTypeCombo;

    private AdminController adminController;
    private DoctorController doctorController;
    private FarmaceutaController farmaceutaController;

    // Constructor
    public Registro() {
        setContentPane(MainPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/LogIn.png"));
        setIconImage(icon.getImage());
        setTitle("Iniciar Sesion");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        UserTF.setPreferredSize(new Dimension(200, 30));
        PasswordTF.setPreferredSize(new Dimension(200, 30));
        RegistrarseBTN.setPreferredSize(new Dimension(100, 30));
        RegistrarseBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        userTypeCombo.setSelectedIndex(0); // Seleccionar el primer elemento por defecto

        RegistrarseBTN.addActionListener(e -> onLogin());
        UserTF.addActionListener(e -> onLogin());
        PasswordTF.addActionListener(e -> onLogin());

        setVisible(true);
    }

    private void onLogin() {
        String idText = UserTF.getText().trim();
        String password = new String(PasswordTF.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        // Debug: mostrar valores
        System.out.println("ID: " + idText);
        System.out.println("Password: " + password);
        System.out.println("UserType: " + userType);

        if (idText.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar que los controladores estén inicializados
        if (adminController == null || doctorController == null || farmaceutaController == null) {
            JOptionPane.showMessageDialog(this,
                "Error: Los controladores no están inicializados. Reinicie la aplicación.",
                "Error del sistema",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            AutenticarUsuario(id, password, userType);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El ID debe ser un numero valido",
                "ID invalido",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void AutenticarUsuario(int id, String pass, String entidad) {
        switch (entidad) {
            case "Administrador":
                AuntenticarAdmin(id, pass);
                break;
            case "Doctor":
                AutenticarDoctor(id, pass);
                break;
            case "Farmacéutico":
                AutenticaFarmaceuta(id, pass);
                break;
            default:
                break;
        }
    }

    /*
        Metodos para el registro correcto con credenciales para
        administradores, farmaceutas y doctores
    */
    private void AuntenticarAdmin(int id, String password) {
        boolean autenticado = adminController.autenticar(id, password);
        if (autenticado) {
                PanelAdministrador panelAdmin = new PanelAdministrador();
                panelAdmin.setVisible(true);
                this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "ID o contraseña incorrectos para Administrador",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void AutenticarDoctor(int id, String password) {
        boolean autenticado = doctorController.autenticar(id, password);
        if (autenticado) {
            String nombreDoctor = doctorController.obtenerNombreDoctor(id);
            DoctorPanel panelDoctor = new DoctorPanel(nombreDoctor);
            panelDoctor.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "ID o contraseña incorrectos para Doctor",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void AutenticaFarmaceuta(int id, String password) {
        boolean autenticado = farmaceutaController.autenticar(id, password);
        if (autenticado) {
            String nombreFarmaceuta = farmaceutaController.obtenerNombreFarmaceuta(id);
            FarmaceutasPanel panelFarmaceutas = new FarmaceutasPanel(nombreFarmaceuta);
            panelFarmaceutas.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "ID o contraseña incorrectos para farmaceutico",
                "Error de autenticacion",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Controladores
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }
    public void setDoctorController(DoctorController doctorController) {
        this.doctorController = doctorController;
    }
    public void setFarmaceutaController(FarmaceutaController farmaceutaController) { this.farmaceutaController = farmaceutaController; }
    
    public JPanel getMainPanel() { return MainPanel; }
}
