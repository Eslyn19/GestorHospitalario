package cr.ac.una.utilities;

import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.presentation_layer.Controller.PacienteController;
import cr.ac.una.presentation_layer.Model.PacienteTableModel;
import cr.ac.una.presentation_layer.Views.BuscarPacientes;
import cr.ac.una.service_layer.PacienteService;
import cr.ac.una.data_access_layer.PacienteFileStore;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BuscarPacienteDialog extends JDialog {
    private BuscarPacientes buscarPacientes;
    private Paciente pacienteSeleccionado;

    public BuscarPacienteDialog(JFrame parent) {
        super(parent, "Seleccionar Paciente", true);
        setupUI();
    }

    private void setupUI() {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        buscarPacientes = new BuscarPacientes(this);
        buscarPacientes.setDialogCallback(this);
        setContentPane(buscarPacientes.getContentPane());
        
        buscarPacientes.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                pacienteSeleccionado = buscarPacientes.getPacienteSeleccionado();
            }
        });
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }
    
    public void setPacienteSeleccionado(Paciente paciente) {
        this.pacienteSeleccionado = paciente;
    }
}
