package cr.ac.una.presentation_layer.Views;

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
        // Crear la vista de Banner
        Banner bannerView = new Banner(this);
        
        // Crear la vista de Histórico de Recetas
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas();

        // Configurar el TabbedPane con iconos
        PanelTabs = new JTabbedPane();

        // Cargar iconos
        ImageIcon bannerIcon = null, historicoIcon = null;
        try { bannerIcon = new ImageIcon(getClass().getResource("/Banner.png")); } catch (Exception ignore) {}
        try { historicoIcon = new ImageIcon(getClass().getResource("/Historial.png")); } catch (Exception ignore) {}

        ImageIcon bannerIconScaled = (bannerIcon != null && bannerIcon.getImage() != null)
                ? new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;
        ImageIcon historicoIconScaled = (historicoIcon != null && historicoIcon.getImage() != null)
                ? new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH))
                : null;

        // Agregar pestañas
        PanelTabs.addTab("Inicio", bannerIconScaled, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Histórico Recetas", historicoIconScaled, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new BorderLayout());
        PanelBase.add(PanelTabs, BorderLayout.CENTER);
    }

    private void setupUI() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria - Farmacéutico: " + nombreFarmaceuta);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
