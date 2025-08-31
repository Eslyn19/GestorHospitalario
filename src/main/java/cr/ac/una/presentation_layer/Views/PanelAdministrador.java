package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.domain_layer.Farmaceuta;
import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;
import cr.ac.una.presentation_layer.Controller.MedicamentoController;
import cr.ac.una.presentation_layer.Controller.PacienteController;
import cr.ac.una.presentation_layer.Model.DoctorTableModel;
import cr.ac.una.presentation_layer.Model.FarmaceutaTableModel;
import cr.ac.una.presentation_layer.Model.MedicamentoTableModel;
import cr.ac.una.presentation_layer.Model.PacienteTableModel;
import cr.ac.una.service_layer.DoctorService;
import cr.ac.una.service_layer.FarmaceutaService;
import cr.ac.una.service_layer.MedicamentoService;
import cr.ac.una.service_layer.PacienteService;
import cr.ac.una.service_layer.IService;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class PanelAdministrador extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;

    public PanelAdministrador() {
        // Iniciar la clase de doctores
        IService<Doctor> doctorservice = new DoctorService(FileManagement.getDoctoresFileStore("doctores.xml"));
        DoctorController doctorcontroller = new DoctorController(doctorservice);
        DoctorTableModel doctormodel = new DoctorTableModel();
        DoctorView doctorview = new DoctorView(doctorcontroller, doctormodel, doctorcontroller.leerTodos(), false);
        doctorservice.addObserver(doctormodel);

        // Iniciar la clase de farmacéuticos
        IService<Farmaceuta> farmaceutaservice = new FarmaceutaService(FileManagement.getFarmaceutasFileStore("farmaceutas.xml"));
        FarmaceutaController farmaceutacontroller = new FarmaceutaController(farmaceutaservice);
        FarmaceutaTableModel farmaceutamodel = new FarmaceutaTableModel();
        FarmaceutaView farmaceutaview = new FarmaceutaView(farmaceutacontroller, farmaceutamodel, farmaceutacontroller.leerTodos(), false);
        farmaceutaservice.addObserver(farmaceutamodel);

        // Iniciar la clase de pacientes
        IService<Paciente> pacienteservice = new PacienteService(FileManagement.getPacientesFileStore("pacientes.xml"));
        PacienteController pacientecontroller = new PacienteController(pacienteservice);
        PacienteTableModel pacientemodel = new PacienteTableModel();
        PacienteView pacienteview = new PacienteView(pacientecontroller, pacientemodel, pacientecontroller.leerTodos(), false);
        pacienteservice.addObserver(pacientemodel);

        // Iniciar la clase de medicamentos
        MedicamentoService medicamentoservice = new MedicamentoService(FileManagement.getMedicamentoFileStore("medicamentos.xml"));
        MedicamentoController medicamentocontroller = new MedicamentoController(medicamentoservice);
        MedicamentoTableModel medicamentomodel = new MedicamentoTableModel();
        MedicamentoView medicamentoview = new MedicamentoView(medicamentocontroller, medicamentomodel, medicamentocontroller.leerTodos(), false);
        medicamentoservice.addObserver(medicamentomodel);

        // Crear tabs
        Dictionary<String, JPanel> tabs = new Hashtable<>();
        tabs.put("Doctores", doctorview.getPanelBase());
        tabs.put("Farmacéuticos", farmaceutaview.getPanelBase());
        tabs.put("Pacientes", pacienteview.getPanelBase());
        tabs.put("Medicamentos", medicamentoview.getPanelBase());

        // Configurar el TabbedPane con iconos
        PanelTabs = new JTabbedPane();
        
        // Cargar iconos
        ImageIcon doctorIcon = new ImageIcon(getClass().getResource("/Doctores.png"));
        ImageIcon farmaceutaIcon = new ImageIcon(getClass().getResource("/Farmaceutas.png"));
        ImageIcon pacienteIcon = new ImageIcon(getClass().getResource("/Paciente.png"));
        ImageIcon medicamentoIcon = new ImageIcon(getClass().getResource("/medicamentos.png"));
        
        // Redimensionar iconos para las pestañas
        ImageIcon doctorIconScaled = new ImageIcon(doctorIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon farmaceutaIconScaled = new ImageIcon(farmaceutaIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon pacienteIconScaled = new ImageIcon(pacienteIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon medicamentoIconScaled = new ImageIcon(medicamentoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        
        // Agregar pestañas con iconos
        PanelTabs.addTab("Doctores", doctorIconScaled, doctorview.getPanelBase(), "Doctores");
        PanelTabs.addTab("Farmacéuticos", farmaceutaIconScaled, farmaceutaview.getPanelBase(), "Farmacéuticos");
        PanelTabs.addTab("Pacientes", pacienteIconScaled, pacienteview.getPanelBase(), "Pacientes");
        PanelTabs.addTab("Medicamentos", medicamentoIconScaled, medicamentoview.getPanelBase(), "Medicamentos");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new java.awt.BorderLayout());
        PanelBase.add(PanelTabs, java.awt.BorderLayout.CENTER);

        // Inicializar la ventana principal
        initializePrincipalPanel();
    }

    private void initializePrincipalPanel() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(PanelBase);
        setVisible(true);
    }
}

