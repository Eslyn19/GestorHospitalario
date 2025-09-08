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
    private JPanel MainPanel;
    private JTable TablaDoctorPanel;
    private JScrollPane Scroller;
    private JTabbedPane PanelTabs;
    private JPanel PanelBase;
    private String nombreDoctor;

    public DoctorPanel() {
        this.nombreDoctor = "Doctor";
        initializeComponents();
        setupUI();
    }

    public DoctorPanel(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
        initializeComponents();
        setupUI();
    }

    private void initializeComponents() {
        BannerView bannerView = new BannerView(this);
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas();
        GraficoPastel graficoPastelView = new GraficoPastel();
        
        // Inicializar servicios para prescripción
        RecetaService recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
        RecetaController recetaController = new RecetaController(recetaService);
        RecetaTableModel recetaTableModel = new RecetaTableModel();
        recetaService.addObserver(recetaTableModel);
        
        // Crear vista de prescripción
        Prescripcion prescripcionView = new Prescripcion(recetaController, recetaTableModel, nombreDoctor);

        PanelTabs = new JTabbedPane();

        // Cargar iconos
        ImageIcon bannerIcon = null, historicoIcon = null, graficoIcon = null, prescripcionIcon = null;
        try { bannerIcon = new ImageIcon(getClass().getResource("/Banner.png")); } catch (Exception ignore) {}
        try { historicoIcon = new ImageIcon(getClass().getResource("/Historial.png")); } catch (Exception ignore) {}
        try { graficoIcon = new ImageIcon(getClass().getResource("/Dashboard.png")); } catch (Exception ignore) {}
        try { prescripcionIcon = new ImageIcon(getClass().getResource("/Prescripcion.png")); } catch (Exception ignore) {}

        ImageIcon bannerIconScaled = (bannerIcon != null && bannerIcon.getImage() != null)
                ? new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon historicoIconScaled = (historicoIcon != null && historicoIcon.getImage() != null)
                ? new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon graficoIconScaled = (graficoIcon != null && graficoIcon.getImage() != null)
                ? new ImageIcon(graficoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon prescripcionIconScaled = (prescripcionIcon != null && prescripcionIcon.getImage() != null)
                ? new ImageIcon(prescripcionIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;

        // Agregar pestañas
        PanelTabs.addTab("Inicio", bannerIconScaled, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Prescripción", prescripcionIconScaled, prescripcionView.getMainPanel(), "Crear y gestionar recetas médicas");
        PanelTabs.addTab("Histórico Recetas", historicoIconScaled, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");
        PanelTabs.addTab("Gráficos", graficoIconScaled, graficoPastelView.getMainPanel(), "Gráficos de estadísticas de recetas");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new BorderLayout());
        PanelBase.add(PanelTabs, BorderLayout.CENTER);
    }

    private void setupUI() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria - (DOC): " + nombreDoctor);
        setSize(1000, 710);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("/Doctor.png"));
        setIconImage(icon.getImage());

        setContentPane(PanelBase);
        setVisible(true);
    }

    public JPanel getPanelBase() {
        return PanelBase;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }
}
