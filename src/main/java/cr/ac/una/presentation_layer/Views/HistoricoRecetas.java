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
        Inicializar();
        CargarDatos();
    }

    private void Inicializar() {
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

    public JPanel getMainPanel() { return MainPanel; }

}
