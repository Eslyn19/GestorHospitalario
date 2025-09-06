package cr.ac.una.utilities;

import cr.ac.una.domain_layer.PrescripcionMedicamento;

import javax.swing.*;
import java.awt.*;

public class EditarPrescripcionDialog extends JDialog {
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
