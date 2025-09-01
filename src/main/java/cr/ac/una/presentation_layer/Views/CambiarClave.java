package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.domain_layer.Farmaceuta;
import cr.ac.una.domain_layer.Persona;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;

import javax.swing.*;

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

    // Campos para manejar la actualización
    private Persona persona; // Doctor o Farmaceuta
    private DoctorController doctorController;
    private FarmaceutaController farmaceutaController;

    // Constructor por defecto (mantiene compatibilidad)
    public CambiarClave(){
        this(null, null, null);
    }
    
    // Constructor para doctores
    public CambiarClave(Doctor doctor, DoctorController doctorController) {
        this(doctor, doctorController, null);
    }
    
    // Constructor para farmacéutas
    public CambiarClave(Farmaceuta farmaceuta, FarmaceutaController farmaceutaController) {
        this(farmaceuta, null, farmaceutaController);
    }
    
    // Constructor principal
    private CambiarClave(Persona persona, DoctorController doctorController, FarmaceutaController farmaceutaController) {
        this.persona = persona;
        this.doctorController = doctorController;
        this.farmaceutaController = farmaceutaController;
        
        setTitle("Cambiar Clave" + (persona != null ? " - " + persona.getNombre() + " " + persona.getApellido() : ""));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(MainPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/PasswordIcon.png"));
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Agregar listeners a los botones
        addListeners();
        
        setVisible(true);
    }
    
    private void addListeners() {
        // Botón Rechazar - cierra la ventana
        RechazarBTN.addActionListener(e -> onRechazar());
        
        // Botón Aceptar - procesa el cambio de clave
        aceptarBTN.addActionListener(e -> onAceptar());
    }
    
    private void onRechazar() {
        dispose(); // Cierra la ventana
    }
    
    private void onAceptar() {
        String contraseñaActual = new String(ContActualPF.getPassword()).trim();
        String contraseñaNueva = new String(ContNuevaPF.getPassword()).trim();
        
        // Validar que los campos no estén vacíos
        if (contraseñaActual.isEmpty() || contraseñaNueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Campos incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Si no hay persona asociada, es cambio de clave genérico
        if (persona == null) {
            JOptionPane.showMessageDialog(this, 
                "¡Contraseña cambiada exitosamente!", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposYCerrar();
            return;
        }
        
        // Validar contraseña actual
        if (!contraseñaActual.equals(persona.getClave())) {
            JOptionPane.showMessageDialog(this, 
                "La contraseña actual es incorrecta", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Intentar actualizar la contraseña
        try {
            if (persona instanceof Doctor) {
                actualizarClaveDoctor((Doctor) persona, contraseñaNueva);
            } else if (persona instanceof Farmaceuta) {
                actualizarClaveFarmaceuta((Farmaceuta) persona, contraseñaNueva);
            }
            
            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this, 
                "¡Contraseña cambiada exitosamente!\nLa información se ha actualizado en el sistema.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            limpiarCamposYCerrar();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al actualizar la contraseña: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarClaveDoctor(Doctor doctor, String nuevaClave) {
        if (doctorController != null) {
            // Actualizar usando el nuevo método que incluye la clave
            doctorController.ActualizarConClave(
                doctor.getID(),
                doctor.getNombre(),
                doctor.getApellido(),
                nuevaClave,
                doctor.getEspecialidad()
            );
            
            // Actualizar la clave localmente en el objeto
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
            
            // Actualizar la clave localmente en el objeto
            farmaceuta.setClave(nuevaClave);
        }
    }
    
    private void limpiarCamposYCerrar() {
        ContActualPF.setText("");
        ContNuevaPF.setText("");
        dispose();
    }

    public JPanel getContentPane() { return MainPanel; }

}
