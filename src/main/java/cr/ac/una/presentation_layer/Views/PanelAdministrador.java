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
        ImageIcon ClaveIcon = new ImageIcon(getClass().getResource("/CambiarClave.png"));

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

        try {
            // Crear dashboard (usa data/recetas.xml)
            DashboardView dashboardView = new DashboardView();
            RecetaFileStore recetaStore = new RecetaFileStore(new File("data/recetas.xml"));
            DashboardController dashboardController = new DashboardController(recetaStore, dashboardView);

            ImageIcon dashIcon = new ImageIcon(getClass().getResource("/Dashboard.png"));
            ImageIcon dashIconScaled = new ImageIcon(dashIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
            PanelTabs.addTab("Dashboard", dashIconScaled, dashboardView.getPanelBase(), "Dashboard");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // -----------------------------------

        //NUEVA PESTAÑA: HISTORIAL DE RECETAS
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

            // listeners mínimos: refrescar y ver detalle
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

            // busqueda rápida por texto (local, en memoria)
            histView.txtBuscar.addKeyListener(new KeyAdapter() {
                @Override public void keyReleased(KeyEvent e) {
                    String t = histView.txtBuscar.getText().trim().toLowerCase();
                    if (t.isEmpty()) {
                        recetaModel.setRows(recetaController.leerTodos());
                        return;
                    }
                    List<Receta> filtered = new java.util.ArrayList<>();
                    for (Receta r : recetaController.leerTodos()) {
                        if ((r.getId() != null && r.getId().toLowerCase().contains(t))
                                || (r.getFecha() != null && r.getFecha().toLowerCase().contains(t))
                                || (r.getEstado() != null && r.getEstado().toLowerCase().contains(t))) {
                            filtered.add(r);
                        }
                    }
                    recetaModel.setRows(filtered);
                }
            });

            // icono opcional
            ImageIcon recetasIcon = null;
            try { recetasIcon = new ImageIcon(getClass().getResource("/Recetas.png")); } catch (Exception ignore) { recetasIcon = null; }
            ImageIcon recetasIconScaled = null;
            if (recetasIcon != null && recetasIcon.getImage() != null) {
                recetasIconScaled = new ImageIcon(recetasIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
            } else {
                recetasIconScaled = medicamentoIconScaled;
            }

            PanelTabs.addTab("Historial Recetas", recetasIconScaled, histView.getPanelBase(), "Histórico de recetas");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // -----------------------------------

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
