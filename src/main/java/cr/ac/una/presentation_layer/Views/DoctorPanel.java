package cr.ac.una.presentation_layer.Views;

import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.service_layer.RecetaService;
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
        IniciarComponentes();
        CargarVentana();
    }

    public DoctorPanel(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
        IniciarComponentes();
        CargarVentana();
    }

    private void IniciarComponentes() {
        RecetaService recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
        RecetaController recetaController = new RecetaController(recetaService);
        RecetaTableModel recetaTableModel = new RecetaTableModel();
        recetaService.addObserver(recetaTableModel);

        BannerView bannerView = new BannerView(this);
        HistoricoRecetas historicoRecetasView = new HistoricoRecetas(recetaService);
        GraficoPastel graficoPastelView = new GraficoPastel(recetaService);
        GraficoLineal graficoLinealView = new GraficoLineal(recetaService);
        Prescripcion prescripcionView = new Prescripcion(recetaController, recetaTableModel, nombreDoctor);

        PanelTabs = new JTabbedPane();

        // Cargar iconos
        ImageIcon bannerIcon = null, historicoIcon = null, graficoIcon = null, prescripcionIcon = null;
        bannerIcon = new ImageIcon(getClass().getResource("/Banner.png"));
        historicoIcon = new ImageIcon(getClass().getResource("/Historial.png"));
        graficoIcon = new ImageIcon(getClass().getResource("/Dashboard.png"));
        prescripcionIcon = new ImageIcon(getClass().getResource("/Prescripcion.png"));

        ImageIcon bannerResized = new ImageIcon(bannerIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon historicoResized = new ImageIcon(historicoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon graficoResized = new ImageIcon(graficoIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));
        ImageIcon prescripcionResized = new ImageIcon(prescripcionIcon.getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH));

        // Agregar pestañas
        PanelTabs.addTab("Inicio", bannerResized, bannerView.getPanel(), "Página de inicio del sistema");
        PanelTabs.addTab("Prescripción", prescripcionResized, prescripcionView.getMainPanel(), "Crear y gestionar recetas médicas");
        PanelTabs.addTab("Histórico Recetas", historicoResized, historicoRecetasView.getMainPanel(), "Histórico de recetas médicas");
        PanelTabs.addTab("Gráfico Pastel", graficoResized, graficoPastelView.getMainPanel(), "Gráfico de pastel de estadísticas");
        PanelTabs.addTab("Gráfico Lineal", graficoResized, graficoLinealView.getPanelPrincipal(), "Gráfico lineal de medicamentos por mes");

        // Configurar el panel base
        PanelBase = new JPanel();
        PanelBase.setLayout(new BorderLayout());
        PanelBase.add(PanelTabs, BorderLayout.CENTER);
    }

    private void CargarVentana() {
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
}
