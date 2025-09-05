package cr.ac.una.presentation_layer.Views;

import com.github.lgooddatepicker.components.DatePicker;
import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Prescripcion extends JFrame {
    private JPanel MainPanel;
    private JPanel ControlPanel;
    private JButton BuscarPacBTN;
    private JButton AgregarMedBTN;
    private JLabel retiroLabel;
    private JLabel PacienteLabel;
    private JTable table1;
    private JButton GuardarBTN;
    private JButton LimpiarBTN;
    private JButton DescartarMedBTN;
    private JPanel ContenedorPanel;
    private JPanel FechaPanel;
    private DatePicker RetiroDPicker;
    private JPanel PacienteNamePanel;
    private JPanel TablaPanel;
    private JScrollPane JScrollpane;
    private JPanel BotonesPanel;

    // Campos adicionales para la funcionalidad
    private JTextField pacienteTF;
    private JTextField medicoTF;
    private List<PrescripcionMedicamento> prescripcionesTemporales;
    private RecetaController recetaController;
    private RecetaTableModel recetaTableModel;
    private String nombreDoctor;

    public JPanel getMainPanel() { return MainPanel; }

    public Prescripcion() {
        this.prescripcionesTemporales = new ArrayList<>();
        initializeComponents();
        setupUI();
        setupEventListeners();
    }

    public Prescripcion(RecetaController recetaController, RecetaTableModel recetaTableModel, String nombreDoctor) {
        this();
        this.recetaController = recetaController;
        this.recetaTableModel = recetaTableModel;
        this.nombreDoctor = nombreDoctor;
        medicoTF.setText(nombreDoctor);
        loadData();
    }

    private void initializeComponents() {
        // Crear campos adicionales que no están en el .form
        pacienteTF = new JTextField(20);
        medicoTF = new JTextField(20);
        
        // Configurar tabla
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar fecha de retiro por defecto (7 días después)
        LocalDate fechaRetiro = LocalDate.now().plusDays(7);
        RetiroDPicker.setDate(fechaRetiro);
    }

    private void setupUI() {
        setContentPane(MainPanel);
        setTitle("Confección de Recetas Médicas");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Configurar bordes
        ControlPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Controles de Receta", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
        
        ContenedorPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Datos de la Receta", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
    }

    private void setupEventListeners() {
        // Botón Buscar Paciente
        BuscarPacBTN.addActionListener(e -> buscarPaciente());

        // Botón Agregar Medicamento
        AgregarMedBTN.addActionListener(e -> abrirDialogoPrescripcion());
        
        // Botón Guardar (Confeccionar Receta)
        GuardarBTN.addActionListener(e -> confeccionarReceta());
        
        // Botón Limpiar
        LimpiarBTN.addActionListener(e -> limpiarCampos());
        
        // Botón Descartar Medicamento
        DescartarMedBTN.addActionListener(e -> eliminarPrescripcionSeleccionada());
        
        // Selección en tabla
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Aquí se puede cargar una receta seleccionada si es necesario
            }
        });
    }

    private void loadData() {
        if (recetaTableModel != null) {
            List<Receta> recetas = recetaController.leerTodos();
            recetaTableModel.setRows(recetas);
        }
    }

    private void buscarPaciente() {
        // TODO: Implementar búsqueda de paciente
        JOptionPane.showMessageDialog(this, 
            "Funcionalidad de búsqueda de paciente pendiente de implementar", 
            "En desarrollo", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirDialogoPrescripcion() {
        PrescripcionDialog dialog = new PrescripcionDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isPrescripcionAgregada()) {
            PrescripcionMedicamento prescripcion = dialog.getPrescripcion();
            prescripcionesTemporales.add(prescripcion);
            actualizarTablaPrescripciones();
        }
    }

    private void eliminarPrescripcionSeleccionada() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < prescripcionesTemporales.size()) {
            prescripcionesTemporales.remove(filaSeleccionada);
            actualizarTablaPrescripciones();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione una prescripción para eliminar", 
                "Selección requerida", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void confeccionarReceta() {
        try {
            String paciente = pacienteTF.getText().trim();
            String medico = medicoTF.getText().trim();
            LocalDate fechaRetiro = RetiroDPicker.getDate();

            if (paciente.isEmpty() || medico.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos obligatorios", 
                    "Campos incompletos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (fechaRetiro == null) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor seleccione una fecha de retiro", 
                    "Fecha requerida", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (fechaRetiro.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, 
                    "La fecha de retiro no puede ser anterior a hoy", 
                    "Fecha inválida", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (prescripcionesTemporales.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe agregar al menos una prescripción", 
                    "Prescripciones requeridas", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear la receta
            String idReceta = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Receta receta = new Receta(
                idReceta,
                paciente,
                medico,
                new ArrayList<>(prescripcionesTemporales),
                LocalDate.now(),
                fechaRetiro,
                "Confeccionada"
            );

            recetaController.confeccionarReceta(receta);
            JOptionPane.showMessageDialog(this, 
                "Receta confeccionada exitosamente con ID: " + idReceta, 
                "Receta creada", 
                JOptionPane.INFORMATION_MESSAGE);
            
            limpiarCampos();
            loadData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al confeccionar la receta: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        pacienteTF.setText("");
        medicoTF.setText(nombreDoctor != null ? nombreDoctor : "");
        PacienteLabel.setText("Paciente");
        LocalDate fechaRetiro = LocalDate.now().plusDays(7);
        RetiroDPicker.setDate(fechaRetiro);
        prescripcionesTemporales.clear();
        actualizarTablaPrescripciones();
        table1.clearSelection();
    }

    private void actualizarTablaPrescripciones() {
        // Crear un modelo de tabla temporal para mostrar las prescripciones
        String[] columnas = {"Medicamento", "Cantidad", "Indicaciones", "Duración"};
        Object[][] datos = new Object[prescripcionesTemporales.size()][4];
        
        for (int i = 0; i < prescripcionesTemporales.size(); i++) {
            PrescripcionMedicamento p = prescripcionesTemporales.get(i);
            datos[i][0] = p.getMedicamento();
            datos[i][1] = p.getCantidad();
            datos[i][2] = p.getIndicaciones();
            datos[i][3] = p.getDuracion();
        }
        
        table1.setModel(new javax.swing.table.DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    // Clase interna para el diálogo de prescripción
    private class PrescripcionDialog extends JDialog {
        private JTextField medicamentoTF;
        private JSpinner cantidadSpinner;
        private JTextArea indicacionesTA;
        private JTextField duracionTF;
        private JButton agregarBTN;
        private JButton cancelarBTN;
        private PrescripcionMedicamento prescripcion;
        private boolean prescripcionAgregada = false;

        public PrescripcionDialog(JFrame parent) {
            super(parent, "Agregar Prescripción", true);
            initializeDialog();
        }

        private void initializeDialog() {
            setSize(400, 300);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout());

            // Panel de campos
            JPanel camposPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            // Medicamento
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            camposPanel.add(new JLabel("Medicamento:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            medicamentoTF = new JTextField(20);
            camposPanel.add(medicamentoTF, gbc);

            // Cantidad
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            camposPanel.add(new JLabel("Cantidad:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
            camposPanel.add(cantidadSpinner, gbc);

            // Indicaciones
            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            camposPanel.add(new JLabel("Indicaciones:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
            indicacionesTA = new JTextArea(3, 20);
            indicacionesTA.setLineWrap(true);
            indicacionesTA.setWrapStyleWord(true);
            camposPanel.add(new JScrollPane(indicacionesTA), gbc);

            // Duración
            gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
            camposPanel.add(new JLabel("Duración (días):"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            duracionTF = new JTextField(20);
            camposPanel.add(duracionTF, gbc);

            add(camposPanel, BorderLayout.CENTER);

            // Panel de botones
            JPanel botonesPanel = new JPanel(new FlowLayout());
            agregarBTN = new JButton("Agregar");
            cancelarBTN = new JButton("Cancelar");
            botonesPanel.add(agregarBTN);
            botonesPanel.add(cancelarBTN);
            add(botonesPanel, BorderLayout.SOUTH);

            // Event listeners
            agregarBTN.addActionListener(e -> agregarPrescripcion());
            cancelarBTN.addActionListener(e -> dispose());
        }

        private void agregarPrescripcion() {
            String medicamento = medicamentoTF.getText().trim();
            int cantidad = (Integer) cantidadSpinner.getValue();
            String indicaciones = indicacionesTA.getText().trim();
            String duracion = duracionTF.getText().trim();

            if (medicamento.isEmpty() || indicaciones.isEmpty() || duracion.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos", 
                    "Campos incompletos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            prescripcion = new PrescripcionMedicamento(medicamento, cantidad, indicaciones, duracion);
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
}
