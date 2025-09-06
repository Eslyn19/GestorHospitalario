package cr.ac.una.presentation_layer.Views;

import com.github.lgooddatepicker.components.DatePicker;
import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.domain_layer.Medicamento;
import cr.ac.una.presentation_layer.Controller.PacienteController;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Model.PacienteTableModel;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.presentation_layer.Model.MedicamentoTableModel;
import cr.ac.una.service_layer.MedicamentoService;
import cr.ac.una.data_access_layer.MedicamentoFileStore;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
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
    private JButton EditarBTN;
    private JButton DescartarMedBTN;
    private JPanel ContenedorPanel;
    private JPanel FechaPanel;
    private DatePicker RetiroDPicker;
    private JPanel PacienteNamePanel;
    private JPanel TablaPanel;
    private JScrollPane JScrollpane;
    private JPanel BotonesPanel;
    private JLabel NombrePaciente;
    private JTextField MostrarPaciente;

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
        
        // Botón Editar
        EditarBTN.addActionListener(e -> editarMedicamentoSeleccionado());
        
        // Botón Descartar Medicamento
        DescartarMedBTN.addActionListener(e -> eliminarPrescripcionSeleccionada());
        
    }

    private void loadData() {
        if (recetaTableModel != null) {
            List<Receta> recetas = recetaController.leerTodos();
            recetaTableModel.setRows(recetas);
        }
    }

    private void buscarPaciente() {
        try {
            // Crear un JDialog modal para seleccionar paciente
            BuscarPacienteDialog dialog = new BuscarPacienteDialog(this);
            dialog.setVisible(true);
            
            // Obtener el paciente seleccionado directamente
            Paciente paciente = dialog.getPacienteSeleccionado();
            if (paciente != null) {
                // Actualizar el campo de texto del paciente
                String nombreCompleto = paciente.getNombre() + " " + paciente.getApellido();
                pacienteTF.setText(nombreCompleto);
                
                // Actualizar el label NombrePaciente con el nombre del paciente
                if (NombrePaciente != null) {
                    NombrePaciente.setText(nombreCompleto);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al buscar paciente: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
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

    private void editarMedicamentoSeleccionado() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < prescripcionesTemporales.size()) {
            PrescripcionMedicamento prescripcionSeleccionada = prescripcionesTemporales.get(filaSeleccionada);
            
            // Abrir diálogo de edición
            EditarPrescripcionDialog dialog = new EditarPrescripcionDialog(this, prescripcionSeleccionada);
            dialog.setVisible(true);
            
            if (dialog.isPrescripcionEditada()) {
                PrescripcionMedicamento prescripcionEditada = dialog.getPrescripcionEditada();
                prescripcionesTemporales.set(filaSeleccionada, prescripcionEditada);
                actualizarTablaPrescripciones();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione una prescripción para editar", 
                "Selección requerida", 
                JOptionPane.WARNING_MESSAGE);
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
                String mensaje = "Campos obligatorios faltantes:\n";
                if (paciente.isEmpty()) mensaje += "- Paciente (use 'Buscar Paciente')\n";
                if (medico.isEmpty()) mensaje += "- Médico\n";
                mensaje += "\nComplete estos campos antes de crear la receta.";
                
                JOptionPane.showMessageDialog(this, 
                    mensaje, 
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
                    "Debe agregar al menos un medicamento antes de crear la receta.\n" +
                    "Use el botón 'Agregar Medicamento' para agregar medicamentos a la receta.", 
                    "Medicamentos requeridos", 
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
        if (MostrarPaciente != null) {
            MostrarPaciente.setText("");
        }
        if (NombrePaciente != null) {
            NombrePaciente.setText("PacienteNombre");
        }
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

    // Clase interna para el diálogo de búsqueda de pacientes
    private class BuscarPacienteDialog extends JDialog {
        private JTable resultsTable;
        private JButton seleccionarBTN;
        private JButton cancelarBTN;
        
        private PacienteController pacienteController;
        private PacienteTableModel pacienteTableModel;
        private Paciente pacienteSeleccionado;

        public BuscarPacienteDialog(JFrame parent) {
            super(parent, "Seleccionar Paciente", true);
            setupUI();
            setupEventListeners();
            cargarPacientes();
        }

        private void setupUI() {
            setSize(600, 400);
            setLocationRelativeTo(getParent());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setResizable(false);
            
            // Color de fondo principal
            Color backgroundColor = new Color(0x6ADAAE);
            setBackground(backgroundColor);
            
            setLayout(new BorderLayout());
            
            // Tabla de resultados
            resultsTable = new JTable();
            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(resultsTable);
            scrollPane.setBackground(backgroundColor);
            add(scrollPane, BorderLayout.CENTER);
            
            // Panel de botones
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(backgroundColor);
            seleccionarBTN = new JButton("Ok");
            cancelarBTN = new JButton("Cancelar");
            
            // Estilo de los botones: fondo blanco, letra negra
            seleccionarBTN.setBackground(Color.WHITE);
            seleccionarBTN.setForeground(Color.BLACK);
            cancelarBTN.setBackground(Color.WHITE);
            cancelarBTN.setForeground(Color.BLACK);
            
            buttonPanel.add(seleccionarBTN);
            buttonPanel.add(cancelarBTN);
            add(buttonPanel, BorderLayout.SOUTH);
            
            // Inicializar controlador
            try {
                pacienteController = new PacienteController(
                    new cr.ac.una.service_layer.PacienteService(
                        new cr.ac.una.data_access_layer.PacienteFileStore(new File("pacientes.xml"))
                    )
                );
                pacienteTableModel = new PacienteTableModel();
                resultsTable.setModel(pacienteTableModel);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }

        private void setupEventListeners() {
            // Botón Seleccionar
            seleccionarBTN.addActionListener(e -> seleccionarPaciente());
            
            // Botón Cancelar
            cancelarBTN.addActionListener(e -> dispose());
            
            // Doble clic para seleccionar
            resultsTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        seleccionarPaciente();
                    }
                }
            });
        }
        
        private void cargarPacientes() {
            List<Paciente> pacientes = pacienteController.leerTodos();
            pacienteTableModel.setRows(pacientes);
        }
        
        private void seleccionarPaciente() {
            int fila = resultsTable.getSelectedRow();
            
            if (fila >= 0) {
                pacienteSeleccionado = pacienteTableModel.getAt(fila);
                if (pacienteSeleccionado != null) {
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Por favor seleccione un paciente", 
                    "Selección requerida", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }

        public Paciente getPacienteSeleccionado() {
            return pacienteSeleccionado;
        }
    }

    // Clase interna para el diálogo de edición de prescripción
    private class EditarPrescripcionDialog extends JDialog {
        private JSpinner cantidadSpinner;
        private JTextArea indicacionesTA;
        private JTextField duracionTF;
        private JButton guardarBTN;
        private JButton cancelarBTN;
        private PrescripcionMedicamento prescripcionEditada;
        private boolean prescripcionEditadaFlag = false;
        private PrescripcionMedicamento prescripcionOriginal;

        public EditarPrescripcionDialog(JFrame parent, PrescripcionMedicamento prescripcion) {
            super(parent, "Editar Prescripción", true);
            this.prescripcionOriginal = prescripcion;
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

            // Medicamento (solo lectura)
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            camposPanel.add(new JLabel("Medicamento:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JTextField medicamentoTF = new JTextField(prescripcionOriginal.getMedicamento());
            medicamentoTF.setEditable(false);
            medicamentoTF.setBackground(getBackground());
            camposPanel.add(medicamentoTF, gbc);

            // Cantidad
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            camposPanel.add(new JLabel("Cantidad:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cantidadSpinner = new JSpinner(new SpinnerNumberModel(prescripcionOriginal.getCantidad(), 1, 999, 1));
            camposPanel.add(cantidadSpinner, gbc);

            // Indicaciones
            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            camposPanel.add(new JLabel("Indicaciones:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
            indicacionesTA = new JTextArea(3, 20);
            indicacionesTA.setText(prescripcionOriginal.getIndicaciones());
            indicacionesTA.setLineWrap(true);
            indicacionesTA.setWrapStyleWord(true);
            camposPanel.add(new JScrollPane(indicacionesTA), gbc);

            // Duración
            gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
            camposPanel.add(new JLabel("Duración (días):"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            duracionTF = new JTextField(prescripcionOriginal.getDuracion());
            camposPanel.add(duracionTF, gbc);

            add(camposPanel, BorderLayout.CENTER);

            // Panel de botones
            JPanel botonesPanel = new JPanel(new FlowLayout());
            guardarBTN = new JButton("Guardar");
            cancelarBTN = new JButton("Cancelar");
            botonesPanel.add(guardarBTN);
            botonesPanel.add(cancelarBTN);
            add(botonesPanel, BorderLayout.SOUTH);

            // Event listeners
            guardarBTN.addActionListener(e -> guardarEdicion());
            cancelarBTN.addActionListener(e -> dispose());
        }

        private void guardarEdicion() {
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

            // Crear prescripción editada manteniendo el medicamento original
            prescripcionEditada = new PrescripcionMedicamento(
                prescripcionOriginal.getMedicamento(), 
                cantidad, 
                indicaciones, 
                duracion
            );
            prescripcionEditadaFlag = true;
            dispose();
        }

        public PrescripcionMedicamento getPrescripcionEditada() {
            return prescripcionEditada;
        }

        public boolean isPrescripcionEditada() {
            return prescripcionEditadaFlag;
        }
    }
}
