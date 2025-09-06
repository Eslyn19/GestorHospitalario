package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.presentation_layer.Controller.PacienteController;
import cr.ac.una.presentation_layer.Model.PacienteTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class BuscarPaciente extends JFrame {
    private JTable resultsTable;
    private JButton seleccionarBTN;
    private JButton cancelarBTN;
    
    private PacienteController pacienteController;
    private PacienteTableModel pacienteTableModel;
    private Paciente pacienteSeleccionado;
    private boolean pacienteSeleccionadoFlag = false;

    public BuscarPaciente(){
        setupUI();
        setupEventListeners();
        cargarPacientes();
    }

    private void setupUI() {
        setTitle("Seleccionar Paciente");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
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
        seleccionarBTN.addActionListener(e -> {
            seleccionarPaciente();
        });
        
        cancelarBTN.addActionListener(e -> {
            dispose();
        });
        
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
