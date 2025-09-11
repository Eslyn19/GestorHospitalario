package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.service_layer.RecetaService;
import cr.ac.una.service_layer.IServiceObserver;
import cr.ac.una.utilities.ChangeType;
import cr.ac.una.data_access_layer.RecetaFileStore;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class HistoricoRecetas implements IServiceObserver<Receta> {
    private JPanel MainPanel;
    private JTable TablaHistorico;
    private JScrollPane MainScroller;

    private RecetaController recetaController;
    private RecetaTableModel recetaTableModel;
    private RecetaService recetaService;

    public HistoricoRecetas() {
        Inicializar();
        CargarDatos();
    }

    public HistoricoRecetas(RecetaService recetaService) {
        this.recetaService = recetaService;
        this.recetaController = new RecetaController(recetaService);
        this.recetaTableModel = new RecetaTableModel();

        recetaService.addObserver(this);
        recetaService.addObserver(recetaTableModel);

        // Configurar tabla
        TablaHistorico.setModel(recetaTableModel);
        TablaHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        MainScroller.setViewportView(TablaHistorico);

        CargarDatos();
    }

    private void Inicializar() {
        try {
            recetaService = new RecetaService(new RecetaFileStore(new File("recetas.xml")));
            recetaController = new RecetaController(recetaService);
            recetaTableModel = new RecetaTableModel();
            
            recetaService.addObserver(this);
            recetaService.addObserver(recetaTableModel);
            
            // Configurar tabla
            TablaHistorico.setModel(recetaTableModel);
            TablaHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            MainScroller.setViewportView(TablaHistorico);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void CargarDatos() {
        try {
            if (recetaController != null) {
                List<Receta> recetas = recetaController.leerTodos();
                recetaTableModel.setRows(recetas);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar las recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Implementación del Observer
    @Override
    public void onDataChanged(ChangeType type, Receta receta) {
        SwingUtilities.invokeLater(() -> {
            TablaHistorico.repaint();
        });
    }

    public JPanel getMainPanel() { return MainPanel; }

}
