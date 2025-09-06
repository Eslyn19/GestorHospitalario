package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.service_layer.RecetaService;
import cr.ac.una.data_access_layer.RecetaFileStore;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class HistoricoRecetas {
    private JPanel MainPanel;
    private JTable TablaHistorico;
    private JScrollPane MainScroller;
    
    private RecetaController recetaController;
    private RecetaTableModel recetaTableModel;

    public HistoricoRecetas() {
        initializeComponents();
        loadData();
    }

    private void initializeComponents() {
        try {
            // Inicializar controlador y modelo
            recetaController = new RecetaController(
                new RecetaService(
                    new RecetaFileStore(new File("recetas.xml"))
                )
            );
            recetaTableModel = new RecetaTableModel();
            
            // Configurar tabla
            TablaHistorico.setModel(recetaTableModel);
            TablaHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            // Configurar scroll pane
            MainScroller.setViewportView(TablaHistorico);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar el histórico de recetas: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
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

    public JPanel getMainPanel() { 
        return MainPanel; 
    }
    
    // Método para refrescar los datos
    public void refreshData() {
        loadData();
    }
}
