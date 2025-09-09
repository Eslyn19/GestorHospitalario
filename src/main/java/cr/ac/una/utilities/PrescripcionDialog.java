package cr.ac.una.utilities;

import cr.ac.una.domain_layer.Medicamento;
import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.presentation_layer.Model.MedicamentoTableModel;
import cr.ac.una.service_layer.MedicamentoService;
import cr.ac.una.data_access_layer.MedicamentoFileStore;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class PrescripcionDialog extends JDialog {
    private JTable medicamentosTable;
    private JSpinner cantidadSpinner;
    private JTextArea indicacionesTA;
    private JTextField duracionTF;
    private JButton agregarBTN;
    private JButton cancelarBTN;
    private PrescripcionMedicamento prescripcion;
    private boolean prescripcionAgregada = false;
    private MedicamentoTableModel medicamentoTableModel;
    private MedicamentoService medicamentoService;

    public PrescripcionDialog(JFrame parent) {
        super(parent, "Seleccionar Medicamento", true);
        initializeDialog();
    }

    private void initializeDialog() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Inicializar servicio de medicamentos
        try {
            medicamentoService = new MedicamentoService(new MedicamentoFileStore(new File("medicamentos.xml")));
            medicamentoTableModel = new MedicamentoTableModel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar medicamentos: " + e.getMessage());
            dispose();
            return;
        }

        // Panel superior: Tabla de medicamentos
        JPanel tablaPanel = new JPanel(new BorderLayout());
        tablaPanel.setBorder(BorderFactory.createTitledBorder("Seleccionar Medicamento"));
        
        medicamentosTable = new JTable(medicamentoTableModel);
        medicamentosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(medicamentosTable);
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        add(tablaPanel, BorderLayout.CENTER);

        // Panel inferior: Campos de prescripción
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBorder(BorderFactory.createTitledBorder("Detalles de la Prescripción"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        camposPanel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        camposPanel.add(cantidadSpinner, gbc);

        // Indicaciones
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Indicaciones:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        indicacionesTA = new JTextArea(3, 20);
        indicacionesTA.setLineWrap(true);
        indicacionesTA.setWrapStyleWord(true);
        camposPanel.add(new JScrollPane(indicacionesTA), gbc);

        // Duración
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
        camposPanel.add(new JLabel("Duración (días):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        duracionTF = new JTextField(20);
        camposPanel.add(duracionTF, gbc);

        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout());
        agregarBTN = new JButton("Agregar");
        cancelarBTN = new JButton("Cancelar");
        botonesPanel.add(agregarBTN);
        botonesPanel.add(cancelarBTN);

        // Panel contenedor para campos y botones
        JPanel inferiorPanel = new JPanel(new BorderLayout());
        inferiorPanel.add(camposPanel, BorderLayout.CENTER);
        inferiorPanel.add(botonesPanel, BorderLayout.SOUTH);
        add(inferiorPanel, BorderLayout.SOUTH);

        // Event listeners
        agregarBTN.addActionListener(e -> agregarPrescripcion());
        cancelarBTN.addActionListener(e -> dispose());

        // Doble clic en la tabla para seleccionar
        medicamentosTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    agregarPrescripcion();
                }
            }
        });

        // Cargar medicamentos
        cargarMedicamentos();
    }

    private void cargarMedicamentos() {
        try {
            List<Medicamento> medicamentos = medicamentoService.leerTodos();
            medicamentoTableModel.setRows(medicamentos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar medicamentos: " + e.getMessage());
        }
    }

    private void agregarPrescripcion() {
        int filaSeleccionada = medicamentosTable.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un medicamento de la tabla", 
                "Selección requerida", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Medicamento medicamentoSeleccionado = medicamentoTableModel.getAt(filaSeleccionada);
        if (medicamentoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Error al obtener el medicamento seleccionado", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = (Integer) cantidadSpinner.getValue();
        String indicaciones = indicacionesTA.getText().trim();
        String duracion = duracionTF.getText().trim();

        if (indicaciones.isEmpty() || duracion.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete las indicaciones y duración", 
                "Campos incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear prescripción con el medicamento seleccionado
        String nombreMedicamento = medicamentoSeleccionado.getNombreMedic() + " (" + medicamentoSeleccionado.getCodigo() + ")";
        prescripcion = new PrescripcionMedicamento(nombreMedicamento, cantidad, indicaciones, duracion);
        prescripcionAgregada = true;
        dispose();
    }

    public PrescripcionMedicamento getPrescripcion() {
        return prescripcion;
    }

    public boolean isPrescripcionAgregada() {
        return prescripcionAgregada;
    }
}
