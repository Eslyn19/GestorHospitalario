package cr.ac.una.presentation_layer.Views;

import com.github.lgooddatepicker.components.DatePicker;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.service_layer.RecetaService;
import cr.ac.una.service_layer.IServiceObserver;
import cr.ac.una.utilities.ChangeType;
import cr.ac.una.utilities.FileManagement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class GraficoLineal implements IServiceObserver<Receta> {
    private JPanel PanelPrincipal;
    private JPanel PanelIzquierdo;
    private JPanel PanelDerecho;
    private JPanel ContenedorDatos;
    private JPanel DesdePanel;
    private JPanel BotonesPanel;
    private JPanel HastaPanel;
    private JLabel DesdeLabel;
    private JLabel HastaLabel;
    private DatePicker HastaDatePicker;
    private DatePicker DesdeDataPicker;
    private JLabel MedicamentosLabel;
    private JComboBox<String> MedicamentosCB;
    private JButton AgregarBTN;
    private JTable TablaMedicamentos;
    private JPanel PanelTabla;
    private JPanel Botones;
    private JButton BorrarUnoBTN;
    private JButton BorrarTodosBTN;
    private JButton CrearGraficoBTN;
    
    // Servicios y controladores
    private RecetaService recetaService;
    private RecetaController recetaController;
    private DefaultTableModel tableModel;
    private ChartPanel chartPanel;
    private JFreeChart chart;
    
    // Datos para el gráfico
    private Map<String, Map<String, Double>> medicamentosData;
    private List<String> mesesEnRango;
    
    public GraficoLineal() {
        InicializarServicios();
        ConfigurarComponentes();
        CargarMedicamentos();
        CrearGraficoInicial();
    }
    
    public GraficoLineal(RecetaService recetaService) {
        this.recetaService = recetaService;
        this.recetaController = new RecetaController(recetaService);
        recetaService.addObserver(this);
        ConfigurarComponentes();
        CargarMedicamentos();
        CrearGraficoInicial();
    }
    
    private void InicializarServicios() {
        try {
            recetaService = new RecetaService(FileManagement.getRecetaFileStore("recetas.xml"));
            recetaController = new RecetaController(recetaService);
            recetaService.addObserver(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al inicializar servicios: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ConfigurarComponentes() {
        // Configurar tabla
        String[] columnNames = {"Medicamento"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        TablaMedicamentos.setModel(tableModel);
        TablaMedicamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar date pickers con fechas por defecto
        LocalDate hoy = LocalDate.now();
        HastaDatePicker.setDate(hoy.minusMonths(2));  // El HastaDatePicker está en la sección "Desde"
        DesdeDataPicker.setDate(hoy);                 // El DesdeDataPicker está en la sección "Hasta"
        
        // Configurar listeners
        AgregarBTN.addActionListener(e -> agregarMedicamento());
        BorrarUnoBTN.addActionListener(e -> borrarMedicamentoSeleccionado());
        BorrarTodosBTN.addActionListener(e -> borrarTodosMedicamentos());
        CrearGraficoBTN.addActionListener(e -> crearGrafico());
        
        // Inicializar datos
        medicamentosData = new HashMap<>();
        mesesEnRango = new ArrayList<>();
    }
    
    private void CargarMedicamentos() {
        try {
            List<Receta> recetas = recetaController.leerTodos();
            Set<String> medicamentosUnicos = new HashSet<>();
            
            for (Receta receta : recetas) {
                for (var prescripcion : receta.getPrescripciones()) {
                    String nombreMedicamento = prescripcion.getMedicamento();
                    // Extraer solo el nombre del medicamento (sin código)
                    if (nombreMedicamento.contains("(")) {
                        nombreMedicamento = nombreMedicamento.substring(0, nombreMedicamento.indexOf("(")).trim();
                    }
                    medicamentosUnicos.add(nombreMedicamento);
                }
            }
            
            MedicamentosCB.removeAllItems();
            for (String medicamento : medicamentosUnicos) {
                MedicamentosCB.addItem(medicamento);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar medicamentos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarMedicamento() {
        String medicamentoSeleccionado = (String) MedicamentosCB.getSelectedItem();
        if (medicamentoSeleccionado == null || medicamentoSeleccionado.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un medicamento", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar si ya existe en la tabla
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (medicamentoSeleccionado.equals(tableModel.getValueAt(i, 0))) {
                JOptionPane.showMessageDialog(null, "El medicamento ya está en la tabla", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Agregar a la tabla
        tableModel.addRow(new Object[]{medicamentoSeleccionado});
    }
    
    private void borrarMedicamentoSeleccionado() {
        int filaSeleccionada = TablaMedicamentos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            tableModel.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un medicamento para borrar", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void borrarTodosMedicamentos() {
        int confirmacion = JOptionPane.showConfirmDialog(null, 
            "¿Está seguro de que desea borrar todos los medicamentos?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
        }
    }
    
    private void crearGrafico() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Agregue al menos un medicamento a la tabla", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LocalDate fechaDesde = HastaDatePicker.getDate();  // El HastaDatePicker está en la sección "Desde"
        LocalDate fechaHasta = DesdeDataPicker.getDate();  // El DesdeDataPicker está en la sección "Hasta"
        
        if (fechaDesde == null || fechaHasta == null) {
            JOptionPane.showMessageDialog(null, "Seleccione las fechas de inicio y fin", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (fechaDesde.isAfter(fechaHasta)) {
            JOptionPane.showMessageDialog(null, "La fecha de inicio debe ser anterior a la fecha de fin", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generar meses en el rango
        mesesEnRango = generarMesesEnRango(fechaDesde, fechaHasta);
        
        // Actualizar columnas de la tabla
        actualizarColumnasTabla();
        
        // Calcular estadísticas
        calcularEstadisticas(fechaDesde, fechaHasta);
        
        // Crear gráfico
        crearGraficoLineal();
    }
    
    private List<String> generarMesesEnRango(LocalDate fechaDesde, LocalDate fechaHasta) {
        List<String> meses = new ArrayList<>();
        LocalDate fechaActual = fechaDesde.withDayOfMonth(1);
        
        while (!fechaActual.isAfter(fechaHasta.withDayOfMonth(1))) {
            meses.add(fechaActual.format(DateTimeFormatter.ofPattern("yyyy-M")));
            fechaActual = fechaActual.plusMonths(1);
        }
        
        return meses;
    }
    
    private void actualizarColumnasTabla() {
        // Crear nuevas columnas
        String[] columnNames = new String[mesesEnRango.size() + 1];
        columnNames[0] = "Medicamento";
        for (int i = 0; i < mesesEnRango.size(); i++) {
            columnNames[i + 1] = mesesEnRango.get(i);
        }
        
        // Guardar datos actuales
        List<Object[]> datosActuales = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object[] fila = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                fila[j] = tableModel.getValueAt(i, j);
            }
            datosActuales.add(fila);
        }
        
        // Crear nuevo modelo con las columnas actualizadas
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Restaurar datos
        for (Object[] fila : datosActuales) {
            Object[] nuevaFila = new Object[columnNames.length];
            nuevaFila[0] = fila[0]; // Medicamento
            // Inicializar cantidades en 0
            for (int i = 1; i < columnNames.length; i++) {
                nuevaFila[i] = 0.0;
            }
            tableModel.addRow(nuevaFila);
        }
        
        TablaMedicamentos.setModel(tableModel);
    }
    
    private void calcularEstadisticas(LocalDate fechaDesde, LocalDate fechaHasta) {
        try {
            List<Receta> recetas = recetaController.leerTodos();
            medicamentosData.clear();
            
            // Inicializar datos para cada medicamento en la tabla
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String medicamento = (String) tableModel.getValueAt(i, 0);
                medicamentosData.put(medicamento, new HashMap<>());
                for (String mes : mesesEnRango) {
                    medicamentosData.get(medicamento).put(mes, 0.0);
                }
            }
            
            // Procesar recetas
            for (Receta receta : recetas) {
                LocalDate fechaConfeccion = receta.getFechaConfeccion();
                if (fechaConfeccion != null && 
                    !fechaConfeccion.isBefore(fechaDesde) && 
                    !fechaConfeccion.isAfter(fechaHasta)) {
                    
                    String mesReceta = fechaConfeccion.format(DateTimeFormatter.ofPattern("yyyy-M"));
                    
                    for (var prescripcion : receta.getPrescripciones()) {
                        String nombreMedicamento = prescripcion.getMedicamento();
                        if (nombreMedicamento.contains("(")) {
                            nombreMedicamento = nombreMedicamento.substring(0, nombreMedicamento.indexOf("(")).trim();
                        }
                        
                        if (medicamentosData.containsKey(nombreMedicamento)) {
                            double cantidadActual = medicamentosData.get(nombreMedicamento).getOrDefault(mesReceta, 0.0);
                            medicamentosData.get(nombreMedicamento).put(mesReceta, cantidadActual + prescripcion.getCantidad());
                        }
                    }
                }
            }
            
            // Actualizar tabla con las cantidades calculadas
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String medicamento = (String) tableModel.getValueAt(i, 0);
                Map<String, Double> datosMedicamento = medicamentosData.get(medicamento);
                
                for (int j = 0; j < mesesEnRango.size(); j++) {
                    String mes = mesesEnRango.get(j);
                    double cantidad = datosMedicamento.getOrDefault(mes, 0.0);
                    tableModel.setValueAt(cantidad, i, j + 1);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al calcular estadísticas: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void crearGraficoLineal() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Agregar datos al dataset
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String medicamento = (String) tableModel.getValueAt(i, 0);
            
            for (int j = 0; j < mesesEnRango.size(); j++) {
                String mes = mesesEnRango.get(j);
                Double cantidad = (Double) tableModel.getValueAt(i, j + 1);
                dataset.addValue(cantidad, medicamento, mes);
            }
        }
        
        // Crear gráfico
        chart = ChartFactory.createLineChart(
            "Estadísticas de Medicamentos por Mes",
            "Mes",
            "Cantidad",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Configurar colores y estilos
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 12));
        
        // Actualizar panel del gráfico
        if (chartPanel != null) {
            PanelDerecho.remove(chartPanel);
        }
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        
        // Cambiar el layout del panel derecho a BorderLayout temporalmente
        PanelDerecho.setLayout(new BorderLayout());
        PanelDerecho.add(chartPanel, BorderLayout.CENTER);
        PanelDerecho.revalidate();
        PanelDerecho.repaint();
    }
    
    private void CrearGraficoInicial() {
        // Crear gráfico vacío inicial
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createLineChart(
            "Estadísticas de Medicamentos por Mes",
            "Mes",
            "Cantidad",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        
        // Cambiar el layout del panel derecho a BorderLayout temporalmente
        PanelDerecho.setLayout(new BorderLayout());
        PanelDerecho.add(chartPanel, BorderLayout.CENTER);
    }
    
    // Implementación del Observer
    @Override
    public void onDataChanged(ChangeType type, Receta receta) {
        SwingUtilities.invokeLater(() -> {
            CargarMedicamentos();
            // Si hay datos en la tabla, recalcular estadísticas
            if (tableModel.getRowCount() > 0 && !mesesEnRango.isEmpty()) {
                LocalDate fechaDesde = HastaDatePicker.getDate();  // El HastaDatePicker está en la sección "Desde"
                LocalDate fechaHasta = DesdeDataPicker.getDate();  // El DesdeDataPicker está en la sección "Hasta"
                if (fechaDesde != null && fechaHasta != null) {
                    calcularEstadisticas(fechaDesde, fechaHasta);
                    if (chart != null) {
                        crearGraficoLineal();
                    }
                }
            }
        });
    }
    
    public JPanel getPanelPrincipal() {
        return PanelPrincipal;
    }
}
