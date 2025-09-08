package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Medicamento;
import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.presentation_layer.Controller.MedicamentoController;
import cr.ac.una.presentation_layer.Model.MedicamentoTableModel;
import cr.ac.una.service_layer.MedicamentoService;
import cr.ac.una.data_access_layer.MedicamentoFileStore;
import cr.ac.una.utilities.AgregarMedicamentoDialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class AgregarMedicamento extends JFrame {
    private JTable TablaMedicamentos;
    private JPanel MainPanel;
    private JSpinner CantidadSpinner;
    private JTextField IndicacionesTF;
    private JPanel PanelInfo;
    private JPanel PanelCantidad;
    private JLabel CantidadLabel;
    private JPanel DiasPanel;
    private JLabel DiasLabel;
    private JSpinner DiasSpinner;
    private JPanel IndicacionesPanel;
    private JLabel IndicacionesLabel;
    private JButton AceptarBTN;
    private JScrollPane Scroller;
    private JPanel PanelTablaMedicamentos;
    private JButton VolverBTN;
    private JPanel PanelBotones;

    private MedicamentoController medicamentoController;
    private MedicamentoTableModel medicamentoTableModel;
    private PrescripcionMedicamento prescripcionMedicamento;
    private AgregarMedicamentoDialog dialogCallback;
    private JDialog parentDialog;

    public AgregarMedicamento(JDialog parentDialog) {
        this.parentDialog = parentDialog;
        setContentPane(MainPanel);  // ya está inicializado por $$$setupUI$$$
        setTitle("Agregar Medicamento");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        setupEventListeners();
        cargarMedicamentos();

        CantidadSpinner.setSize(new Dimension(200, 30));
        IndicacionesTF.setSize(new Dimension(400, 50));
        DiasPanel.setPreferredSize(new Dimension(200, 30));
        Color color = new Color(255, 255, 255);
        configurarPanel(IndicacionesPanel, "Descripcion medicamentos", color, new Font("Arial", Font.BOLD, 13), Color.WHITE);
    }


    public void setDialogCallback(AgregarMedicamentoDialog dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    private void setupUI() {
        setTitle("Agregar Medicamento");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setContentPane(MainPanel);
        
        TablaMedicamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CantidadSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        DiasSpinner.setModel(new SpinnerNumberModel(1, 1, 365, 1));
        IndicacionesTF.setColumns(20);

        try {
            medicamentoController = new MedicamentoController(new MedicamentoService(new MedicamentoFileStore(new File("medicamentos.xml"))));
            medicamentoTableModel = new MedicamentoTableModel();
            TablaMedicamentos.setModel(medicamentoTableModel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        AceptarBTN.addActionListener(e -> agregarMedicamento());
        VolverBTN.addActionListener(e -> dispose());

        // Doble clic en la tabla para agregar medicamento
        TablaMedicamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    agregarMedicamento();
                }
            }
        });
    }
    
    private void cargarMedicamentos() {
        List<Medicamento> medicamentos = medicamentoController.leerTodos();
        medicamentoTableModel.setRows(medicamentos);
    }
    
    private void agregarMedicamento() {
        // Validar que se haya seleccionado un medicamento
        int fila = TablaMedicamentos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un medicamento de la tabla",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar que el medicamento seleccionado sea válido
        Medicamento medicamentoSeleccionado = medicamentoTableModel.getAt(fila);
        if (medicamentoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                "Error al obtener el medicamento seleccionado",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar indicaciones
        String indicaciones = IndicacionesTF.getText().trim();
        if (indicaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese las indicaciones del medicamento",
                "Campo requerido",
                JOptionPane.WARNING_MESSAGE);
            IndicacionesTF.requestFocus();
            return;
        }
        
        // Validar cantidad
        int cantidad = (Integer) CantidadSpinner.getValue();
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this,
                "La cantidad debe ser mayor a 0",
                "Cantidad inválida",
                JOptionPane.WARNING_MESSAGE);
            CantidadSpinner.requestFocus();
            return;
        }
        
        // Validar días
        int dias = (Integer) DiasSpinner.getValue();
        if (dias <= 0) {
            JOptionPane.showMessageDialog(this,
                "Los días de duración deben ser mayor a 0",
                "Duración inválida",
                JOptionPane.WARNING_MESSAGE);
            DiasSpinner.requestFocus();
            return;
        }
        
        // Crear prescripción
        String duracion = dias + " días";
        String nombreMedicamento = medicamentoSeleccionado.getNombreMedic() + " (" + medicamentoSeleccionado.getCodigo() + ")";
        
        prescripcionMedicamento = new PrescripcionMedicamento(nombreMedicamento, cantidad, indicaciones, duracion);
        
        // Notificar al dialog callback
        if (dialogCallback != null) {
            dialogCallback.setPrescripcionMedicamento(prescripcionMedicamento);
        }
        
        // Cerrar ventana
        if (parentDialog != null) {
            parentDialog.dispose();
        } else {
            dispose();
        }
    }

    public PrescripcionMedicamento getPrescripcionMedicamento() {
        return prescripcionMedicamento;
    }

    private void configurarPanel(JPanel panel, String titulo, Color bordeColor, Font fuente, Color tituloColor) {
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(bordeColor, 1),
                        titulo,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        fuente,
                        tituloColor
                )
        );
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
