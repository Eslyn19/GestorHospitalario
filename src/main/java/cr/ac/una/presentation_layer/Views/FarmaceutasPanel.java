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
        Inicializar();
        CargarVentana();
    }

    public FarmaceutasPanel(String nombreFarmaceuta) {
        this.nombreFarmaceuta = nombreFarmaceuta;
        Inicializar();
        CargarVentana();
    }

    private void Inicializar() {
        // Crear servicios compartidos
        PacienteService pacienteService = new PacienteService(FileManagement.getPacientesFileStore("pacientes.xml"));
        RecetaService recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
        
        // Crear vistas con servicios compartidos
        BannerView bannerView = new BannerView(this);
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas(recetaService);
        GraficoPastel graficoPastelView = new GraficoPastel(recetaService);
        GraficoLineal graficoLinealView = new GraficoLineal(recetaService);
        
        DespachoTableModel despachoTableModel = new DespachoTableModel();
        recetaService.addObserver(despachoTableModel);
        Despacho despachoView = new Despacho();
        DespachoController despachoController = new DespachoController(despachoView, pacienteService, recetaService, despachoTableModel);

        PanelTabs = new JTabbedPane();

        // Cargar iconos
        ImageIcon bannerIcon = new ImageIcon(getClass().getResource("/Banner.png"));
        ImageIcon historicoIcon = new ImageIcon(getClass().getResource("/Historial.png"));
        ImageIcon despachoIcon = new ImageIcon(getClass().getResource("/Despacho.png"));
        ImageIcon graficoLinealIcon = new ImageIcon(getClass().getResource("/GraficoLineal.png"));
        ImageIcon graficoPastelIcon = new ImageIcon(getClass().getResource("/GraficoPastel.png"));

        ImageIcon BannerResized = new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon historicoResized = new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon despachoResized = new ImageIcon(despachoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon pastelResized = new ImageIcon(graficoPastelIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon graficoLinealResized = new ImageIcon(graficoLinealIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));

        // Agregar pestañas
        PanelTabs.addTab("Inicio", BannerResized, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Despacho", historicoResized, despachoView.getPanel(), "Despacho de recetas");
        PanelTabs.addTab("Histórico Recetas", despachoResized, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");
        PanelTabs.addTab("Gráfico Pastel", pastelResized, graficoPastelView.getMainPanel(), "Gráfico de pastel de estadísticas");
        PanelTabs.addTab("Gráfico Lineal", graficoLinealResized, graficoLinealView.getPanelPrincipal(), "Gráfico lineal de medicamentos por mes");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new BorderLayout());
        PanelBase.add(PanelTabs, BorderLayout.CENTER);
    }

    private void CargarVentana() {
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
}
