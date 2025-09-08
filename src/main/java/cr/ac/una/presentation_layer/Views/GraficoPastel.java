package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.service_layer.RecetaService;
import cr.ac.una.utilities.FileManagement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraficoPastel {
    private JTable TablaEstado;
    private JPanel MainPanel;
    private JPanel PanelMedicamentos;
    private JScrollPane Scroller;
    private JPanel GraficoPastel;
    
    private RecetaController recetaController;
    private RecetaService recetaService;
    private DefaultTableModel tableModel;
    private ChartPanel chartPanel;
    
    public GraficoPastel() {
        IniciarServicio();
        ConfigTabla();
        CrearGrafico();
        CargarData();
    }
    
    private void IniciarServicio() {
        try {
            recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
            recetaController = new RecetaController(recetaService);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al inicializar servicios: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ConfigTabla() {
        // Configurar el modelo de tabla
        String[] columnNames = {"Estado", "Cantidad", "Porcentaje"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla de solo lectura
            }
        };
        
        TablaEstado.setModel(tableModel);
        TablaEstado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TablaEstado.getTableHeader().setReorderingAllowed(false);

        // Ancho de tablas
        TablaEstado.getColumnModel().getColumn(0).setPreferredWidth(150);
        TablaEstado.getColumnModel().getColumn(1).setPreferredWidth(80);
        TablaEstado.getColumnModel().getColumn(2).setPreferredWidth(80);
    }
    
    private void CrearGrafico() {
        // Crear el gráfico de pastel inicial
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Sin datos", 1);
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Recetas por Estado",
            dataset,
            true, // legend
            true, // tooltips
            false // urls
        );
        
        // Personalizar el gráfico
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint((Comparable) "Sin datos", Color.LIGHT_GRAY);
        plot.setLabelFont(new Font("Bell MT", Font.PLAIN, 10));
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Limpiar el panel y agregar el grafico
        GraficoPastel.removeAll();
        GraficoPastel.setLayout(new BorderLayout());
        GraficoPastel.add(chartPanel, BorderLayout.CENTER);
        GraficoPastel.revalidate();
        GraficoPastel.repaint();
    }
    
    private void CargarData() {
        try {
            List<Receta> recetas = recetaController.leerTodos();
            Map<String, Integer> estadoCount = new HashMap<>();
            
            // Contar recetas por estado
            for (Receta receta : recetas) {
                String estado = receta.getEstado();
                if (estado == null || estado.trim().isEmpty()) {
                    estado = "Sin estado";
                }
                estadoCount.put(estado, estadoCount.getOrDefault(estado, 0) + 1);
            }
            
            // Limpiar la tabla
            tableModel.setRowCount(0);
            
            // Crear dataset para el grafico
            DefaultPieDataset dataset = new DefaultPieDataset();
            
            int totalRecetas = recetas.size();
            
            if (totalRecetas == 0) {
                tableModel.addRow(new Object[]{"Sin datos", 0, "0%"});
                dataset.setValue("Sin datos", 1);
            } else {
                // Agregar datos
                for (Map.Entry<String, Integer> entry : estadoCount.entrySet()) {
                    String estado = entry.getKey();
                    int cantidad = entry.getValue();
                    double porcentaje = (cantidad * 100.0) / totalRecetas;
                    
                    tableModel.addRow(new Object[]{
                        estado, 
                        cantidad, 
                        String.format("%.1f%%", porcentaje)
                    });
                    
                    dataset.setValue(estado, cantidad);
                }
            }
            
            ActualizarGrafico(dataset);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ActualizarGrafico(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Recetas por Estado",
            dataset,
            true,
            true,
            false
        );
        
        // Estilo del grafico
        PiePlot pie = (PiePlot) chart.getPlot();
        pie.setLabelFont(new Font("Bell MT", Font.PLAIN, 10));
        
        // Asignar colores específicos para cada estado
        Color[] colors = {
            new Color(79, 129, 189),   // Azul para Confeccionada
            new Color(192, 80, 77),    // Rojo para En proceso
            new Color(155, 187, 89),   // Verde para Lista
            new Color(128, 100, 162),  // Morado para Entregada
        };
        
        int iColor = 0;
        for (Object key : dataset.getKeys()) {
            if (iColor < colors.length) {
                pie.setSectionPaint((Comparable) key, colors[iColor]);
                iColor++;
            }
        }
        
        // Actualizar
        chartPanel.setChart(chart);
    }
    
    public JPanel getMainPanel() { return MainPanel; }
}
