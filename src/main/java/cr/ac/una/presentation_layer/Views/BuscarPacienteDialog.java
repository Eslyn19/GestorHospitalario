package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Paciente;

import javax.swing.*;

public class BuscarPacienteDialog extends JDialog {
    private BuscarPacientes buscarPacientes;
    private Paciente pacienteSeleccionado;

    public BuscarPacienteDialog(JFrame parent) {
        super(parent, "Seleccionar Paciente", true);

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
    public void setPacienteSeleccionado(Paciente paciente) { this.pacienteSeleccionado = paciente; }
}
