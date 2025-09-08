package cr.ac.una.presentation_layer.Views;

import com.github.lgooddatepicker.components.DatePicker;
import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Controller.RecetaController;
import cr.ac.una.presentation_layer.Model.RecetaTableModel;
import cr.ac.una.utilities.AgregarMedicamentoDialog;
import cr.ac.una.utilities.BuscarPacienteDialog;
import cr.ac.una.utilities.EditarPrescripcionDialog;

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

    private JTextField pacienteTF;
    private JTextField medicoTF;
    private List<PrescripcionMedicamento> prescripcionesTemporales;
    private RecetaController recetaController;
    private RecetaTableModel recetaTableModel;
    private String nombreDoctor;

    public JPanel getMainPanel() { return MainPanel; }

    public Prescripcion() {
        this.prescripcionesTemporales = new ArrayList<>();
        Init();
        CargarComponentes();
        Listeners();
    }

    public Prescripcion(RecetaController recetaController, RecetaTableModel recetaTableModel, String nombreDoctor) {
        this();
        this.recetaController = recetaController;
        this.recetaTableModel = recetaTableModel;
        this.nombreDoctor = nombreDoctor;
        medicoTF.setText(nombreDoctor);
        loadData();
    }

    private void Init() {
        pacienteTF = new JTextField(20);
        medicoTF = new JTextField(20);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        LocalDate fechaRetiro = LocalDate.now().plusDays(7);
        RetiroDPicker.setDate(fechaRetiro);
    }

    private void CargarComponentes() {
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

    private void Listeners() {
        BuscarPacBTN.addActionListener(e -> buscarPaciente());
        AgregarMedBTN.addActionListener(e -> abrirDialogoPrescripcion());
        GuardarBTN.addActionListener(e -> confeccionarReceta());
        EditarBTN.addActionListener(e -> editarMedicamentoSeleccionado());
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
        AgregarMedicamentoDialog dialog = new AgregarMedicamentoDialog(this);
        dialog.setVisible(true);

        if (dialog.isMedicamentoAgregado()) {
            PrescripcionMedicamento prescripcion = dialog.getPrescripcionMedicamento();
            prescripcionesTemporales.add(prescripcion);
            actualizarTablaPrescripciones();
        }
    }

    private void editarMedicamentoSeleccionado() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < prescripcionesTemporales.size()) {
            PrescripcionMedicamento prescripcionSeleccionada = prescripcionesTemporales.get(filaSeleccionada);

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

        if (MostrarPaciente != null) { MostrarPaciente.setText(""); }
        if (NombrePaciente != null) { NombrePaciente.setText("PacienteNombre"); }

        LocalDate fechaRetiro = LocalDate.now().plusDays(7);
        RetiroDPicker.setDate(fechaRetiro);
        prescripcionesTemporales.clear();
        actualizarTablaPrescripciones();
        table1.clearSelection();
    }

    private void actualizarTablaPrescripciones() {
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
}
