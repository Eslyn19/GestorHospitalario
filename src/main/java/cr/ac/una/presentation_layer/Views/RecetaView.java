package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecetaView extends JFrame{
    private JPanel PanelBase;
    private JPanel UpperPanel;
    private JPanel LowerPanel;
    private JPanel MidPanel;
    private JPanel UpperPanelBTN;
    private JPanel UpperPanelTF;
    private JPanel PacientePanel;
    private JPanel MedicoPanel;
    private JPanel FechaRetiroPanel;
    private JPanel PrescripcionesPanel;
    private JLabel pacienteLabel;
    private JTextField pacienteTF;
    private JLabel medicoLabel;
    private JTextField medicoTF;
    private JLabel fechaRetiroLabel;
    private JTextField fechaRetiroTF;
    private JLabel prescripcionesLabel;
    private JList<PrescripcionMedicamento> prescripcionesList;
    private JScrollPane prescripcionesScrollPane;
    private JButton AgregarPrescripcionBTN;
    private JButton EliminarPrescripcionBTN;
    private JButton ConfeccionarRecetaBTN;
    private JButton LimpiarBTN;
    private JTable TablaRecetas;
    private JScrollPane JScrollPane;
    private JTextField BuscarTF;
    private JPanel BuscarPanel;
    private JPanel SpacePanel;
    private JLabel BuscarLabel;
    private JButton ActualizarBTN;

    private RecetaController recetaController;
    private RecetaTableModel recetaTableModel;
    private List<PrescripcionMedicamento> prescripcionesTemporales;

    public RecetaView(RecetaController recetaController, RecetaTableModel recetaTableModel, List<Receta> datos) {
        this(recetaController, recetaTableModel, datos, true);
    }

    public RecetaView(RecetaController recetaController, RecetaTableModel recetaTableModel, List<Receta> datos, boolean isVisible) {
        this.recetaController = recetaController;
        this.recetaTableModel = recetaTableModel;
        this.prescripcionesTemporales = new ArrayList<>();
        
        initializeComponents();
        setupUI();
        setupEventListeners();
        loadData(datos);
        
        if (isVisible) {
            setVisible(true);
        }
    }

    private void initializeComponents() {
        // Configurar lista de prescripciones
        prescripcionesList = new JList<>();
        prescripcionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prescripcionesScrollPane = new JScrollPane(prescripcionesList);
        
        // Configurar tabla
        TablaRecetas = new JTable(recetaTableModel);
        TablaRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane = new JScrollPane(TablaRecetas);
        
        // Configurar botones
        AgregarPrescripcionBTN.setPreferredSize(new Dimension(150, 30));
        EliminarPrescripcionBTN.setPreferredSize(new Dimension(150, 30));
        ConfeccionarRecetaBTN.setPreferredSize(new Dimension(150, 30));
        LimpiarBTN.setPreferredSize(new Dimension(100, 30));
        ActualizarBTN.setPreferredSize(new Dimension(100, 30));
        
        // Configurar fecha de retiro (por defecto, 7 días después)
        LocalDate fechaRetiro = LocalDate.now().plusDays(7);
        fechaRetiroTF.setText(fechaRetiro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void setupUI() {
        setContentPane(PanelBase);
        setTitle("Confección de Recetas Médicas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Configurar bordes
        UpperPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Datos de la Receta", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
        
        LowerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Historial de Recetas", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
    }

    private void setupEventListeners() {
        // Botón Agregar Prescripción
        AgregarPrescripcionBTN.addActionListener(e -> abrirDialogoPrescripcion());
        
        // Botón Eliminar Prescripción
        EliminarPrescripcionBTN.addActionListener(e -> eliminarPrescripcionSeleccionada());
        
        // Botón Confeccionar Receta
        ConfeccionarRecetaBTN.addActionListener(e -> confeccionarReceta());
        
        // Botón Limpiar
        LimpiarBTN.addActionListener(e -> limpiarCampos());
        
        // Botón Actualizar
        ActualizarBTN.addActionListener(e -> actualizarTabla());
        
        // Selección en tabla
        TablaRecetas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarRecetaSeleccionada();
            }
        });
        
        // Búsqueda en tiempo real
        BuscarTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarRecetas();
            }
        });
    }

    private void loadData(List<Receta> datos) {
        recetaTableModel.setRows(datos);
    }

    private void abrirDialogoPrescripcion() {
        PrescripcionDialog dialog = new PrescripcionDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isPrescripcionAgregada()) {
            PrescripcionMedicamento prescripcion = dialog.getPrescripcion();
            prescripcionesTemporales.add(prescripcion);
            actualizarListaPrescripciones();
        }
    }

    private void eliminarPrescripcionSeleccionada() {
        int indiceSeleccionado = prescripcionesList.getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            prescripcionesTemporales.remove(indiceSeleccionado);
            actualizarListaPrescripciones();
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
            String fechaRetiroStr = fechaRetiroTF.getText().trim();

            if (paciente.isEmpty() || medico.isEmpty() || fechaRetiroStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos obligatorios", 
                    "Campos incompletos", 
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

            // Validar fecha de retiro
            LocalDate fechaRetiro;
            try {
                fechaRetiro = LocalDate.parse(fechaRetiroStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (fechaRetiro.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, 
                        "La fecha de retiro no puede ser anterior a hoy", 
                        "Fecha inválida", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Formato de fecha inválido. Use dd/MM/yyyy", 
                    "Fecha inválida", 
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
            actualizarTabla();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al confeccionar la receta: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRecetaSeleccionada() {
        int filaSeleccionada = TablaRecetas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Receta receta = recetaTableModel.getAt(filaSeleccionada);
            if (receta != null) {
                pacienteTF.setText(receta.getPaciente());
                medicoTF.setText(receta.getMedico());
                fechaRetiroTF.setText(receta.getFechaRetiro() != null ? 
                    receta.getFechaRetiro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
                prescripcionesTemporales.clear();
                prescripcionesTemporales.addAll(receta.getPrescripciones());
                actualizarListaPrescripciones();
            }
        }
    }

    private void limpiarCampos() {
        pacienteTF.setText("");
        medicoTF.setText("");
        LocalDate fechaRetiro = LocalDate.now().plusDays(7);
        fechaRetiroTF.setText(fechaRetiro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        prescripcionesTemporales.clear();
        actualizarListaPrescripciones();
        TablaRecetas.clearSelection();
    }

    private void actualizarListaPrescripciones() {
        DefaultListModel<PrescripcionMedicamento> model = new DefaultListModel<>();
        for (PrescripcionMedicamento prescripcion : prescripcionesTemporales) {
            model.addElement(prescripcion);
        }
        prescripcionesList.setModel(model);
    }

    private void actualizarTabla() {
        List<Receta> recetas = recetaController.leerTodos();
        recetaTableModel.setRows(recetas);
    }

    private void buscarRecetas() {
        String textoBusqueda = BuscarTF.getText().trim().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            actualizarTabla();
            return;
        }

        List<Receta> todasLasRecetas = recetaController.leerTodos();
        List<Receta> recetasFiltradas = todasLasRecetas.stream()
            .filter(r -> r.getId().toLowerCase().contains(textoBusqueda) ||
                        r.getPaciente().toLowerCase().contains(textoBusqueda) ||
                        r.getMedico().toLowerCase().contains(textoBusqueda) ||
                        r.getEstado().toLowerCase().contains(textoBusqueda))
            .toList();
        
        recetaTableModel.setRows(recetasFiltradas);
    }

    public JPanel getPanelBase() {
        return PanelBase;
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
