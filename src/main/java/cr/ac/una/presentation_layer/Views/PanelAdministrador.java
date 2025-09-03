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

import cr.ac.una.presentation_layer.Views.DashboardView.DashboardView;
import cr.ac.una.presentation_layer.Controller.DashboardController;
import cr.ac.una.data_access_layer.RecetaFileStore;
import cr.ac.una.presentation_layer.Views.HistorialRecetas.HistorialRecetasView;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Views.HistorialRecetas.RecetaDetalleDialog;
import cr.ac.una.presentation_layer.Views.AcercaDe.AcercaDeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class PanelAdministrador extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;

    public PanelAdministrador() {
        setResizable(false);
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

        // Configurar el TabbedPane con iconos
        PanelTabs = new JTabbedPane();

        // Cargar iconos (con fallback silencioso)
        ImageIcon doctorIcon = null, farmaceutaIcon = null, pacienteIcon = null, medicamentoIcon = null, ClaveIcon = null;
        try { doctorIcon = new ImageIcon(getClass().getResource("/Doctores.png")); } catch (Exception ignore) {}
        try { farmaceutaIcon = new ImageIcon(getClass().getResource("/Farmaceutas.png")); } catch (Exception ignore) {}
        try { pacienteIcon = new ImageIcon(getClass().getResource("/Paciente.png")); } catch (Exception ignore) {}
        try { medicamentoIcon = new ImageIcon(getClass().getResource("/medicamentos.png")); } catch (Exception ignore) {}
        try { ClaveIcon = new ImageIcon(getClass().getResource("/CambiarClave.png")); } catch (Exception ignore) {}

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

        // Agregar pestañas con iconos (si hay icono, si no, pasa null)
        PanelTabs.addTab("Doctores", doctorIconScaled, doctorview.getPanelBase(), "Doctores");
        PanelTabs.addTab("Farmacéuticos", farmaceutaIconScaled, farmaceutaview.getPanelBase(), "Farmacéuticos");
        PanelTabs.addTab("Pacientes", pacienteIconScaled, pacienteview.getPanelBase(), "Pacientes");
        PanelTabs.addTab("Medicamentos", medicamentoIconScaled, medicamentoview.getPanelBase(), "Medicamentos");

        try {
            DashboardView dashboardView = new DashboardView();
            RecetaFileStore recetaStore = new RecetaFileStore(new File("data/recetas.xml"));
            DashboardController dashboardController = new DashboardController(recetaStore, dashboardView);

            ImageIcon dashIcon = null;
            try { dashIcon = new ImageIcon(getClass().getResource("/Dashboard.png")); } catch (Exception ignore) {}
            ImageIcon dashIconScaled = (dashIcon != null && dashIcon.getImage() != null)
                    ? new ImageIcon(dashIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                    : null;
            PanelTabs.addTab("Dashboard", dashIconScaled, dashboardView.getPanelBase(), "Dashboard");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            // Crea la vista
            HistorialRecetasView histView = new HistorialRecetasView();

            // crea controller y model
            RecetaController recetaController = new RecetaController(new File("data/recetas.xml"));
            RecetaTableModel recetaModel = new RecetaTableModel();
            // carga datos iniciales
            List<Receta> all = recetaController.leerTodos();
            recetaModel.setRows(all);

            // conecta modelo a la vista
            histView.setTableModel(recetaModel);

            // refrescar y ver detalle
            histView.btnRefrescar.addActionListener(e -> {
                List<Receta> nuevas = recetaController.leerTodos();
                recetaModel.setRows(nuevas);
            });

            histView.btnVerDetalle.addActionListener(e -> {
                int row = histView.tableRecetas.getSelectedRow();
                if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione una receta.", "Info", JOptionPane.INFORMATION_MESSAGE); return; }
                Receta r = recetaModel.getAt(row);
                Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
                RecetaDetalleDialog dlg = new RecetaDetalleDialog(owner, r);
                dlg.setVisible(true);
            });

            // busqueda rápida por ID
            histView.txtBuscar.addKeyListener(new KeyAdapter() {
                @Override public void keyReleased(KeyEvent e) {
                    String t = histView.txtBuscar.getText().trim().toLowerCase();
                    if (t.isEmpty()) {
                        recetaModel.setRows(recetaController.leerTodos());
                        return;
                    }
                    List<Receta> filtered = new java.util.ArrayList<>();
                    for (Receta r : recetaController.leerTodos()) {
                        if (r.getId() != null && r.getId().toLowerCase().contains(t)) {
                            filtered.add(r);
                        }
                    }
                    recetaModel.setRows(filtered);
                }
            });

            // icono opcional para la pestaña de recetas
            ImageIcon recetasIcon = null;
            try { recetasIcon = new ImageIcon(getClass().getResource("/Recetas.png")); } catch (Exception ignore) { recetasIcon = null; }
            ImageIcon recetasIconScaled = (recetasIcon != null && recetasIcon.getImage() != null)
                    ? new ImageIcon(recetasIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                    : medicamentoIconScaled;

            PanelTabs.addTab("Historial Recetas", recetasIconScaled, histView.getPanelBase(), "Histórico de recetas");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ACERCA DE (nueva pestaña integrada)
        try {
            AcercaDeView acerca = new AcercaDeView();
            ImageIcon aboutIcon = null;
            try { aboutIcon = new ImageIcon(getClass().getResource("/AcercaDe.png")); } catch (Exception ignore) { aboutIcon = null; }
            ImageIcon aboutIconScaled = (aboutIcon != null && aboutIcon.getImage() != null)
                    ? new ImageIcon(aboutIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                    : null;
            PanelTabs.addTab("Acerca de", aboutIconScaled, acerca.getPanelBase(), "Acerca de");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new java.awt.BorderLayout());
        PanelBase.add(PanelTabs, java.awt.BorderLayout.CENTER);

        // Inicializar la ventana principal
        initializePrincipalPanel();
    }

    private void initializePrincipalPanel() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria (ADMIN)");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(PanelBase);
        setVisible(true);
    }
}
