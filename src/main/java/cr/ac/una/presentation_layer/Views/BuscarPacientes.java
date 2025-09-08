package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.presentation_layer.Controller.PacienteController;
import cr.ac.una.presentation_layer.Model.PacienteTableModel;
import cr.ac.una.service_layer.PacienteService;
import cr.ac.una.data_access_layer.PacienteFileStore;
import cr.ac.una.utilities.BuscarPacienteDialog;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class BuscarPacientes extends JFrame {
    private JTable TablaPacientes;
    private JPanel MainPanel;
    private JButton AceptarBTN;
    private JButton RegresarBTN;
    private JPanel PanelBotones;
    private JScrollPane Scroller;
    
    private PacienteController pacienteController;
    private PacienteTableModel pacienteTableModel;
    private Paciente pacienteSeleccionado;
    private boolean pacienteSeleccionadoFlag = false;
    private JDialog parentDialog;
    private BuscarPacienteDialog dialogCallback;

    public BuscarPacientes() {
        CargarComponentes();
        Listeners();
        cargarPacientes();
    }
    
    public BuscarPacientes(JDialog parentDialog) {
        this.parentDialog = parentDialog;
        CargarComponentes();
        Listeners();
        cargarPacientes();
    }
    
    public void setDialogCallback(BuscarPacienteDialog dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    private void CargarComponentes() {
        setTitle("Seleccionar Paciente");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setContentPane(MainPanel);
        TablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        pacienteController = new PacienteController(new PacienteService(new PacienteFileStore(new File("pacientes.xml"))));
        pacienteTableModel = new PacienteTableModel();
        TablaPacientes.setModel(pacienteTableModel);
    }

    private void Listeners() {
        AceptarBTN.addActionListener(e -> { seleccionarPaciente(); });
        RegresarBTN.addActionListener(e -> {
            if (parentDialog != null) {
                parentDialog.dispose();
            } else {
                dispose();
            }
        });

        TablaPacientes.addMouseListener(new java.awt.event.MouseAdapter() {
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
        int fila = TablaPacientes.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un paciente",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        pacienteSeleccionado = pacienteTableModel.getAt(fila);
        if (pacienteSeleccionado == null) {
            return;
        }

        if (dialogCallback != null) {
            dialogCallback.setPacienteSeleccionado(pacienteSeleccionado);
        }

        if (parentDialog != null) {
            parentDialog.dispose();
        } else {
            dispose();
        }
    }


    public Paciente getPacienteSeleccionado() { return pacienteSeleccionado; }
}
