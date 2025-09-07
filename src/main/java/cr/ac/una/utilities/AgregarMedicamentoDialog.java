package cr.ac.una.utilities;

import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.presentation_layer.Views.AgregarMedicamento;

import javax.swing.*;
import java.awt.*;

public class AgregarMedicamentoDialog extends JDialog {
    private AgregarMedicamento agregarMedicamento;
    private PrescripcionMedicamento prescripcionMedicamento;
    private boolean medicamentoAgregado = false;

    public AgregarMedicamentoDialog(JFrame parent) {
        super(parent, "Agregar Medicamento", true);
        setupUI();
    }

    private void setupUI() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        agregarMedicamento = new AgregarMedicamento(this);
        agregarMedicamento.setDialogCallback(this);
        setContentPane(agregarMedicamento.getContentPane());
        
        agregarMedicamento.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                prescripcionMedicamento = agregarMedicamento.getPrescripcionMedicamento();
            }
        });
    }

    public PrescripcionMedicamento getPrescripcionMedicamento() {
        return prescripcionMedicamento;
    }
    
    public void setPrescripcionMedicamento(PrescripcionMedicamento prescripcion) {
        this.prescripcionMedicamento = prescripcion;
        this.medicamentoAgregado = true;
    }
    
    public boolean isMedicamentoAgregado() {
        return medicamentoAgregado;
    }
}

