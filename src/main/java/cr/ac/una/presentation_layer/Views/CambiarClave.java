package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.domain_layer.Farmaceuta;
import cr.ac.una.domain_layer.Persona;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;

import javax.swing.*;
import java.awt.*;

public class CambiarClave extends JFrame {
    private JPanel MainPanel;
    private JPanel UpperPanel;
    private JPanel LowerPanel;
    private JPanel AntiguoPanel;
    private JPanel NuevoPanel;
    private JPanel AceptarPanel;
    private JPanel RechazarPanel;
    private JButton aceptarBTN;
    private JButton RechazarBTN;
    private JPanel NuevaLabelPanel;
    private JPanel NuevoCampoPanel;
    private JPasswordField ContActualPF;
    private JPanel NuevoIDPanel;
    private JPanel NuevoCampoIDPanel;
    private JPasswordField ContNuevaPF;

    private Persona persona; // Doctor o Farmaceuta
    private DoctorController doctorController;
    private FarmaceutaController farmaceutaController;

    public CambiarClave(){
        this(null, null, null);
    }

    // Constructor para doctores
    public CambiarClave(Doctor doctor, DoctorController doctorController) {
        this(doctor, doctorController, null);
    }
    
    // Constructor para farmaceutas
    public CambiarClave(Farmaceuta farmaceuta, FarmaceutaController farmaceutaController) {
        this(farmaceuta, null, farmaceutaController);
    }
    
    // Constructor principal
    private CambiarClave(Persona persona, DoctorController doctorController, FarmaceutaController farmaceutaController) {
        this.persona = persona;
        this.doctorController = doctorController;
        this.farmaceutaController = farmaceutaController;
        
        setTitle("Cambiar Clave");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(MainPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/PasswordIcon.png"));
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ContActualPF.setPreferredSize(new Dimension(20, 25));
        ContNuevaPF.setPreferredSize(new Dimension(20, 25));

        addListeners();
        setVisible(true);
    }
    
    private void addListeners() {
        RechazarBTN.addActionListener(e -> onRechazar());
        aceptarBTN.addActionListener(e -> onAceptar());
    }
    
    private void onRechazar() {
        dispose();
    }
    
    private void onAceptar() {
        String contraseñaActual = new String(ContActualPF.getPassword()).trim();
        String contraseñaNueva = new String(ContNuevaPF.getPassword()).trim();
        
        if (contraseñaActual.isEmpty() || contraseñaNueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Campos incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (persona == null) {
            JOptionPane.showMessageDialog(this, 
                "¡Contraseña cambiada exitosamente!", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            onClean();
            return;
        }
        
        if (!contraseñaActual.equals(persona.getClave())) {
            JOptionPane.showMessageDialog(this, 
                "La contraseña actual es incorrecta", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (persona instanceof Doctor) {
                actualizarClaveDoctor((Doctor) persona, contraseñaNueva);
            } else if (persona instanceof Farmaceuta) {
                actualizarClaveFarmaceuta((Farmaceuta) persona, contraseñaNueva);
            }
            
            JOptionPane.showMessageDialog(this,
                "¡Contraseña cambiada exitosamente!",
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            onClean();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al actualizar la contraseña: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarClaveDoctor(Doctor doctor, String nuevaClave) {
        if (doctorController != null) {
            doctorController.ActualizarConClave(
                doctor.getID(),
                doctor.getNombre(),
                doctor.getApellido(),
                nuevaClave,
                doctor.getEspecialidad()
            );
            
            doctor.setClave(nuevaClave);
        }
    }
    
    private void actualizarClaveFarmaceuta(Farmaceuta farmaceuta, String nuevaClave) {
        if (farmaceutaController != null) {
            farmaceutaController.ActualizarConClave(
                farmaceuta.getID(),
                farmaceuta.getNombre(),
                farmaceuta.getApellido(),
                nuevaClave
            );
            
            farmaceuta.setClave(nuevaClave);
        }
    }
    
    private void onClean() {
        ContActualPF.setText("");
        ContNuevaPF.setText("");
        dispose();
    }

    public JPanel getContentPane() { return MainPanel; }
}
