package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Controller.PrescripcionController;
import cr.ac.una.presentation_layer.Model.DoctorTableModel;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.presentation_layer.Model.PrescripcionTableModel;
import cr.ac.una.service_layer.DoctorService;
import cr.ac.una.service_layer.RecetaService;
import cr.ac.una.service_layer.PrescripcionService;
import cr.ac.una.service_layer.IService;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.awt.*;

public class PanelDoctor extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;
    private String nombreDoctor;

    public PanelDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
        setResizable(false);
        initializeComponents();
        setupUI();
    }

    private void initializeComponents() {
        // Iniciar la clase de recetas para confección
        RecetaService recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
        RecetaController recetaController = new RecetaController(recetaService);
        RecetaTableModel recetaModel = new RecetaTableModel();
        recetaService.addObserver(recetaModel);

        // Iniciar la clase de prescripciones individuales
        PrescripcionService prescripcionService = new PrescripcionService(FileManagement.getPrescripcionFileStore("prescripciones.xml"));
        PrescripcionController prescripcionController = new PrescripcionController(prescripcionService);
        PrescripcionTableModel prescripcionModel = new PrescripcionTableModel();
        prescripcionService.addObserver(prescripcionModel);

        // Crear la vista de prescripciones (confección de recetas)
        Prescripcion confeccionRecetasView = new Prescripcion(recetaController, recetaModel, nombreDoctor);

        // Crear la vista de Banner
        Banner bannerView = new Banner(this);

        // Configurar el TabbedPane con iconos
        PanelTabs = new JTabbedPane();

        // Cargar iconos
        ImageIcon doctorIcon = null, prescripcionIcon = null, confeccionIcon = null, bannerIcon = null;
        try { prescripcionIcon = new ImageIcon(getClass().getResource("/Prescripcion.png")); } catch (Exception ignore) {}
        try { bannerIcon = new ImageIcon(getClass().getResource("/Banner.png")); } catch (Exception ignore) {}

        ImageIcon prescripcionIconScaled = (prescripcionIcon != null && prescripcionIcon.getImage() != null)
                ? new ImageIcon(prescripcionIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon bannerIconScaled = (bannerIcon != null && bannerIcon.getImage() != null)
                ? new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;

        // Agregar pestañas
        PanelTabs.addTab("Inicio", bannerIconScaled, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Prescripciones", prescripcionIconScaled, confeccionRecetasView.getMainPanel(), "Confección de recetas médicas");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new java.awt.BorderLayout());
        PanelBase.add(PanelTabs, java.awt.BorderLayout.CENTER);
    }

    private void setupUI() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria - Doctor: " + nombreDoctor);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(PanelBase);
        setVisible(true);
    }
}
