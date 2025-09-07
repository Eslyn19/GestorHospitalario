package cr.ac.una.presentation_layer.Views;

import cr.ac.una.presentation_layer.Controller.DespachoController;
import cr.ac.una.presentation_layer.Model.DespachoTableModel;
import cr.ac.una.service_layer.PacienteService;
import cr.ac.una.service_layer.RecetaService;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.awt.*;

public class FarmaceutasPanel extends JFrame {
    private JPanel MainPanel;
    private JTable TablaFarmaceutas;
    private JScrollPane ScrollerPane;
    private JTabbedPane PanelTabs;
    private JPanel PanelBase;
    private String nombreFarmaceuta;

    public FarmaceutasPanel() {
        this.nombreFarmaceuta = "Farmacéutico";
        initializeComponents();
        setupUI();
    }

    public FarmaceutasPanel(String nombreFarmaceuta) {
        this.nombreFarmaceuta = nombreFarmaceuta;
        initializeComponents();
        setupUI();
    }

    private void initializeComponents() {
        BannerView bannerView = new BannerView(this);
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas();
        PacienteService pacienteService = new PacienteService(FileManagement.getPacientesFileStore("pacientes.xml"));
        RecetaService recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
        
        DespachoTableModel despachoTableModel = new DespachoTableModel();
        recetaService.addObserver(despachoTableModel);
        Despacho despachoView = new Despacho();
        DespachoController despachoController = new DespachoController(despachoView, pacienteService, recetaService, despachoTableModel);

        PanelTabs = new JTabbedPane();

        // Cargar iconos
        ImageIcon bannerIcon = null, historicoIcon = null, despachoIcon = null;
        try { bannerIcon = new ImageIcon(getClass().getResource("/Banner.png")); } catch (Exception ignore) {}
        try { historicoIcon = new ImageIcon(getClass().getResource("/Historial.png")); } catch (Exception ignore) {}
        try { despachoIcon = new ImageIcon(getClass().getResource("/Despacho.png")); } catch (Exception ignore) {}

        ImageIcon bannerIconScaled = (bannerIcon != null && bannerIcon.getImage() != null)
                ? new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon historicoIconScaled = (historicoIcon != null && historicoIcon.getImage() != null)
                ? new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon despachoIconScaled = (despachoIcon != null && despachoIcon.getImage() != null)
                ? new ImageIcon(despachoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;

        // Agregar pestañas
        PanelTabs.addTab("Inicio", bannerIconScaled, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Despacho", despachoIconScaled, despachoView.getPanel(), "Despacho de recetas");
        PanelTabs.addTab("Histórico Recetas", historicoIconScaled, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new BorderLayout());
        PanelBase.add(PanelTabs, BorderLayout.CENTER);
    }

    private void setupUI() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria - (FARM): " + nombreFarmaceuta);
        setSize(1000, 710);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("/Farmaceuta.png"));
        setIconImage(icon.getImage());

        setContentPane(PanelBase);
        setVisible(true);
    }

    public JPanel getPanelBase() {
        return PanelBase;
    }

    public String getNombreFarmaceuta() {
        return nombreFarmaceuta;
    }
}
