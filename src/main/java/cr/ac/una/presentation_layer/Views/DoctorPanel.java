package cr.ac.una.presentation_layer.Views;

import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Controller.PrescripcionController;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.presentation_layer.Model.PrescripcionTableModel;
import cr.ac.una.service_layer.RecetaService;
import cr.ac.una.service_layer.PrescripcionService;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.awt.*;

public class DoctorPanel extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;
    private String nombreDoctor;
    private JScrollPane Scroller;
    private JPanel MainPanel;
    private JTable TablaDoctorPanel;

    public DoctorPanel(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;

        RecetaService recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
        RecetaController recetaController = new RecetaController(recetaService);
        RecetaTableModel recetaModel = new RecetaTableModel();
        recetaService.addObserver(recetaModel);

        PrescripcionService prescripcionService = new PrescripcionService(FileManagement.getPrescripcionFileStore("prescripciones.xml"));
        PrescripcionController prescripcionController = new PrescripcionController(prescripcionService);
        PrescripcionTableModel prescripcionModel = new PrescripcionTableModel();
        prescripcionService.addObserver(prescripcionModel);

        Prescripcion confeccionRecetasView = new Prescripcion(recetaController, recetaModel, nombreDoctor);
        Banner bannerView = new Banner(this);
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas();

        PanelTabs = new JTabbedPane();

        // Cargar iconos
        ImageIcon prescripcionIcon = new ImageIcon(getClass().getResource("/Prescripcion.png"));
        ImageIcon bannerIcon = new ImageIcon(getClass().getResource("/Banner.png"));
        ImageIcon historicoIcon = new ImageIcon(getClass().getResource("/Historial.png"));

        ImageIcon prescripcionResize = new ImageIcon(prescripcionIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon bannerResize = new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon historicoResize = new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));

        // Agregar pestañas
        PanelTabs.addTab("Inicio", bannerResize, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Prescripciones", prescripcionResize, confeccionRecetasView.getMainPanel(), "Confección de recetas médicas");
        PanelTabs.addTab("Histórico Recetas", historicoResize, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new BorderLayout());
        PanelBase.add(PanelTabs, BorderLayout.CENTER);

        setTitle("Sistema de Gestión Hospitalaria - " + nombreDoctor + " - (MED)");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon(getClass().getResource("/Doctor.png"));
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(PanelBase);
        setVisible(true);
        setResizable(false);
    }

    public JPanel getPanelBase() {
        return PanelBase;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }
}