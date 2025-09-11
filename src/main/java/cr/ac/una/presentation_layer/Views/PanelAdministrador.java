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
import cr.ac.una.service_layer.*;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.util.Objects;

public class PanelAdministrador extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;

    // Constructor
    public PanelAdministrador() {
        // Cargar datos de cada entidad
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

        // Crear servicio de recetas compartido
        RecetaService recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));

        BannerView bannerView = new BannerView(this);
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas(recetaService);
        GraficoPastel graficoPastelView = new GraficoPastel(recetaService);
        GraficoLineal graficoLinealView = new GraficoLineal(recetaService);

        // Cargar imagenes
        ImageIcon doctorIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Doctores.png")));
        ImageIcon farmaceutaIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Farmaceutas.png")));
        ImageIcon pacienteIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Paciente.png")));
        ImageIcon medicamentoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/medicamentos.png")));
        ImageIcon bannerIcon = new ImageIcon(getClass().getResource("/Banner.png"));
        ImageIcon historicoIcon = new ImageIcon(getClass().getResource("/Historial.png"));
        ImageIcon graficoIcon = new ImageIcon(getClass().getResource("/Dashboard.png"));
        ImageIcon graficoLinealIcon = new ImageIcon(getClass().getResource("/Dashboard.png")); // Usar el mismo icono por ahora

        // Configurar imagenes con nuevo resize
        ImageIcon doctorResize = new ImageIcon(doctorIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon farmaceutaResize = new ImageIcon(farmaceutaIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon pacienteResize = new ImageIcon(pacienteIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon medicamentoResize = new ImageIcon(medicamentoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon bannerResize = new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon historicoResize = new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon graficoResize = new ImageIcon(graficoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon graficoLinealResize = new ImageIcon(graficoLinealIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));

        PanelTabs = new JTabbedPane();

        // Agregar pestañas
        PanelTabs.addTab("Inicio", bannerResize, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Doctores", doctorResize, doctorview.getPanelBase(), "Doctores");
        PanelTabs.addTab("Farmacéuticos", farmaceutaResize, farmaceutaview.getPanelBase(), "Farmacéuticos");
        PanelTabs.addTab("Pacientes", pacienteResize, pacienteview.getPanelBase(), "Pacientes");
        PanelTabs.addTab("Medicamentos", medicamentoResize, medicamentoview.getPanelBase(), "Medicamentos");
        PanelTabs.addTab("Histórico Recetas", historicoResize, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");
        PanelTabs.addTab("Gráfico Pastel", graficoResize, graficoPastelView.getMainPanel(), "Gráfico de pastel de estadísticas");
        PanelTabs.addTab("Gráfico Lineal", graficoLinealResize, graficoLinealView.getPanelPrincipal(), "Gráfico lineal de medicamentos por mes");

        PanelBase = new JPanel();
        PanelBase.setLayout(new java.awt.BorderLayout());
        PanelBase.add(PanelTabs, java.awt.BorderLayout.CENTER);

        setTitle("Sistema de Gestión Hospitalaria (ADM)");
        setSize(1100, 710);
        ImageIcon icon = new ImageIcon(getClass().getResource("/Admin.png"));
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(PanelBase);
        setVisible(true);
        setResizable(false);
    }
}
