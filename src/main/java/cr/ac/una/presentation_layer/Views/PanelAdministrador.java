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

public class PanelAdministrador extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;

    public PanelAdministrador() {
        setResizable(false);

        IService<Doctor> doctorservice = new DoctorService(FileManagement.getDoctoresFileStore("doctores.xml"));
        DoctorController doctorcontroller = new DoctorController(doctorservice);
        DoctorTableModel doctormodel = new DoctorTableModel();
        DoctorView doctorview = new DoctorView(doctorcontroller, doctormodel, doctorcontroller.leerTodos(), false);
        doctorservice.addObserver(doctormodel);

        IService<Farmaceuta> farmaceutaservice = new FarmaceutaService(FileManagement.getFarmaceutasFileStore("farmaceutas.xml"));
        FarmaceutaController farmaceutacontroller = new FarmaceutaController(farmaceutaservice);
        FarmaceutaTableModel farmaceutamodel = new FarmaceutaTableModel();
        FarmaceutaView farmaceutaview = new FarmaceutaView(farmaceutacontroller, farmaceutamodel, farmaceutacontroller.leerTodos(), false);
        farmaceutaservice.addObserver(farmaceutamodel);

        IService<Paciente> pacienteservice = new PacienteService(FileManagement.getPacientesFileStore("pacientes.xml"));
        PacienteController pacientecontroller = new PacienteController(pacienteservice);
        PacienteTableModel pacientemodel = new PacienteTableModel();
        PacienteView pacienteview = new PacienteView(pacientecontroller, pacientemodel, pacientecontroller.leerTodos(), false);
        pacienteservice.addObserver(pacientemodel);

        MedicamentoService medicamentoservice = new MedicamentoService(FileManagement.getMedicamentoFileStore("medicamentos.xml"));
        MedicamentoController medicamentocontroller = new MedicamentoController(medicamentoservice);
        MedicamentoTableModel medicamentomodel = new MedicamentoTableModel();
        MedicamentoView medicamentoview = new MedicamentoView(medicamentocontroller, medicamentomodel, medicamentocontroller.leerTodos(), false);
        medicamentoservice.addObserver(medicamentomodel);

        // Crear la vista de Banner
        Banner bannerView = new Banner(this);
        
        // Crear la vista de Histórico de Recetas
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas();

        PanelTabs = new JTabbedPane();

        ImageIcon doctorIcon = new ImageIcon(getClass().getResource("/Doctores.png"));
        ImageIcon farmaceutaIcon = new ImageIcon(getClass().getResource("/Farmaceutas.png"));
        ImageIcon pacienteIcon = new ImageIcon(getClass().getResource("/Paciente.png"));
        ImageIcon medicamentoIcon = new ImageIcon(getClass().getResource("/medicamentos.png"));
        ImageIcon ClaveIcon = new ImageIcon(getClass().getResource("/CambiarClave.png"));
        ImageIcon bannerIcon = new ImageIcon(getClass().getResource("/Banner.png"));
        ImageIcon historicoIcon = new ImageIcon(getClass().getResource("/Historial.png"));

        ImageIcon doctorIconScaled = (doctorIcon != null && doctorIcon.getImage() != null)
                ? new ImageIcon(doctorIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon farmaceutaIconScaled = (farmaceutaIcon != null && farmaceutaIcon.getImage() != null)
                ? new ImageIcon(farmaceutaIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon pacienteIconScaled = (pacienteIcon != null && pacienteIcon.getImage() != null)
                ? new ImageIcon(pacienteIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon medicamentoIconScaled = (medicamentoIcon != null && medicamentoIcon.getImage() != null)
                ? new ImageIcon(medicamentoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon bannerIconScaled = (bannerIcon != null && bannerIcon.getImage() != null)
                ? new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon historicoIconScaled = (historicoIcon != null && historicoIcon.getImage() != null)
                ? new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;

        PanelTabs.addTab("Inicio", bannerIconScaled, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Doctores", doctorIconScaled, doctorview.getPanelBase(), "Doctores");
        PanelTabs.addTab("Farmacéuticos", farmaceutaIconScaled, farmaceutaview.getPanelBase(), "Farmacéuticos");
        PanelTabs.addTab("Pacientes", pacienteIconScaled, pacienteview.getPanelBase(), "Pacientes");
        PanelTabs.addTab("Medicamentos", medicamentoIconScaled, medicamentoview.getPanelBase(), "Medicamentos");
        PanelTabs.addTab("Histórico Recetas", historicoIconScaled, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");

        PanelBase = new JPanel();
        PanelBase.setLayout(new java.awt.BorderLayout());
        PanelBase.add(PanelTabs, java.awt.BorderLayout.CENTER);

        InitPrincipalPanel();
    }

    private void InitPrincipalPanel() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria (ADMIN)");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(PanelBase);
        setVisible(true);
    }
}
