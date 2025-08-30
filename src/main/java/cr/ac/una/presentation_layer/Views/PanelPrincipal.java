package cr.ac.una.presentation_layer.Views;

import cr.ac.una.data_access_layer.DoctorFileStore;
import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Model.DoctorTableModel;
import cr.ac.una.service_layer.DoctorService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

// Panel para incluir las demas ventanas

public class PanelPrincipal extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;

    public PanelPrincipal() {
        initializePrincipalPanel();
        setupDoctorTab();
    }
    
    private void initializePrincipalPanel() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Hospital.webp"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Could not load hospital icon");
        }
        
        setContentPane(PanelBase);
        setVisible(true);
    }
    
    private void setupDoctorTab() {
        try {
            // Initialize doctor services
            File xmlFile = new File("data/doctores.xml");
            DoctorFileStore fileStore = new DoctorFileStore(xmlFile);
            DoctorService doctorService = new DoctorService(fileStore);
            DoctorController doctorController = new DoctorController(doctorService);
            
            // Create table model and set up observer
            DoctorTableModel tableModel = new DoctorTableModel();
            doctorService.addObserver(tableModel);
            
            // Load existing doctors
            List<Doctor> doctors = doctorController.leerTodos();
            
            // Create doctor view panel (not as standalone window)
            JPanel doctorPanel = DoctorView.createDoctorPanel(doctorController, tableModel, doctors);
            
            // Add doctor tab to tabbed pane
            PanelTabs.addTab("Doctores", new ImageIcon(getClass().getResource("/Doctores.png")), 
                           doctorPanel, "Gestión de Doctores");
            
            // Add other tabs for future modules
            JPanel pacientesPanel = new JPanel();
            pacientesPanel.add(new JLabel("Módulo de Pacientes - Próximamente", SwingConstants.CENTER));
            PanelTabs.addTab("Pacientes", new ImageIcon(getClass().getResource("/Paciente.png")), 
                           pacientesPanel, "Gestión de Pacientes");
            
            JPanel medicamentosPanel = new JPanel();
            medicamentosPanel.add(new JLabel("Módulo de Medicamentos - Próximamente", SwingConstants.CENTER));
            PanelTabs.addTab("Medicamentos", new ImageIcon(getClass().getResource("/medicamentos.png")), 
                           medicamentosPanel, "Gestión de Medicamentos");
            
            JPanel farmaceutasPanel = new JPanel();
            farmaceutasPanel.add(new JLabel("Módulo de Farmaceutas - Próximamente", SwingConstants.CENTER));
            PanelTabs.addTab("Farmaceutas", new ImageIcon(getClass().getResource("/Farmaceutas.png")), 
                           farmaceutasPanel, "Gestión de Farmaceutas");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al inicializar el panel de doctores: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    


    public JPanel getPanelBase() { return PanelBase; }
    
    public JTabbedPane getPanelTabs() { return PanelTabs; }
    
    public void selectDoctorTab() {
        PanelTabs.setSelectedIndex(0); // Select first tab (Doctores)
    }
}
