package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.PrescripcionMedicamento;

import javax.swing.*;

public class AgregarMedicamentoDialog extends JDialog {
    private AgregarMedicamento agregarMedicamento;
    private PrescripcionMedicamento prescripcionMedicamento;
    private boolean medicamentoAgregado = false;

    public AgregarMedicamentoDialog(JFrame parent) {
        super(parent, "Agregar Medicamento", true);

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
    public boolean isMedicamentoAgregado() {
        return medicamentoAgregado;
    }

    public void setPrescripcionMedicamento(PrescripcionMedicamento prescripcion) {
        this.prescripcionMedicamento = prescripcion;
        this.medicamentoAgregado = true;
    }
}

