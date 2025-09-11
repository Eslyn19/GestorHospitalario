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

        IniciarComponentes();
        ConfigurarComponentes();
        Listeners();
        CargarData(datos);
        
        if (isVisible) {
            setVisible(true);
        }
    }

    private void IniciarComponentes() {
        prescripcionesList = new JList<>();
        prescripcionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prescripcionesScrollPane = new JScrollPane(prescripcionesList);
        
        TablaRecetas = new JTable(recetaTableModel);
        TablaRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane = new JScrollPane(TablaRecetas);
        
        AgregarPrescripcionBTN.setPreferredSize(new Dimension(150, 30));
        EliminarPrescripcionBTN.setPreferredSize(new Dimension(150, 30));
        ConfeccionarRecetaBTN.setPreferredSize(new Dimension(150, 30));
        LimpiarBTN.setPreferredSize(new Dimension(100, 30));
        ActualizarBTN.setPreferredSize(new Dimension(100, 30));
        
        // Configurar fecha de retiro  dias despues
        LocalDate fechaRetiro = LocalDate.now().plusDays(7);
        fechaRetiroTF.setText(fechaRetiro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void ConfigurarComponentes() {
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

    private void Listeners() {
        AgregarPrescripcionBTN.addActionListener(e -> abrirDialogoPrescripcion());
        EliminarPrescripcionBTN.addActionListener(e -> eliminarPrescripcionSeleccionada());
        ConfeccionarRecetaBTN.addActionListener(e -> confeccionarReceta());
        LimpiarBTN.addActionListener(e -> limpiarCampos());
        ActualizarBTN.addActionListener(e -> actualizarTabla());
        TablaRecetas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarRecetaSeleccionada();
            }
        });
        BuscarTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarRecetas();
            }
        });
    }

    private void CargarData(List<Receta> datos) {
        recetaTableModel.setRows(datos);
    }

    private void abrirDialogoPrescripcion() {
        AgregarMedicamentoDialog dialog = new AgregarMedicamentoDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isMedicamentoAgregado()) {
            PrescripcionMedicamento prescripcion = dialog.getPrescripcionMedicamento();
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
}
