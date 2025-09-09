package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Medicamento;
import cr.ac.una.presentation_layer.Controller.MedicamentoController;
import cr.ac.una.presentation_layer.Model.MedicamentoTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MedicamentoView extends JFrame{
    private JPanel PanelBase;
    private JPanel UpperPanel;
    private JPanel LowerPanel;
    private JPanel MidPanel;
    private JPanel UpperPanelBTN;
    private JPanel UpperPanelTF;
    private JPanel IDESPanel;
    private JPanel NombresPanel;
    private JTextField codigoTF;
    private JPanel ApellidoPanel;
    private JPanel NombrePanel2;
    private JPanel NomApPanel;
    private JLabel nombreLabel;
    private JTextField nombreTF;
    private JLabel PresentacionLabel;
    private JTextField PresentacionTF;
    private JButton GuardarBTN;
    private JButton LimpiarBTN;
    private JButton EliminarBTN;
    private JTable TablaMedicos;
    private JScrollPane JSrollPane;
    private JTextField BuscarIDTF;
    private JPanel BuscarMedicoPanel;
    private JPanel SpacePanel;
    private JLabel IDBuscarLabel;
    private JButton ActualizarBTN;

    private MedicamentoController medicamentoController;
    private MedicamentoTableModel medicamentoTableModel;

    public MedicamentoView(MedicamentoController medicamentoController, MedicamentoTableModel medicamentoTableModel, List<Medicamento> datos, boolean showAsWindow) {
        this.medicamentoController = medicamentoController;
        this.medicamentoTableModel = medicamentoTableModel;
        addListeners();
        bind(medicamentoController, medicamentoTableModel, datos);

        Color white = new Color(255, 255, 255);
        configurarPanel(UpperPanel, "Medicamento", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(MidPanel, "Búsqueda", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(LowerPanel, "Listado", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);

        codigoTF.setPreferredSize(new Dimension(20, 25));
        nombreTF.setPreferredSize(new Dimension(20, 25));
        PresentacionTF.setPreferredSize(new Dimension(20, 25));
        BuscarIDTF.setPreferredSize(new Dimension(20, 25));

        setContentPane(PanelBase);
        ImageIcon icon = new ImageIcon(getClass().getResource("/medicamentos.png"));
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setTitle("Medicamentos");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if (showAsWindow) {
            setVisible(true);
        }
    }

    public JPanel getPanelBase() { return PanelBase; }

    private void addListeners() {
        GuardarBTN.addActionListener(e -> onAdd());
        ActualizarBTN.addActionListener(e -> onUpdate());
        EliminarBTN.addActionListener(e -> onDelete());
        LimpiarBTN.addActionListener(e -> onClear());
        TablaMedicos.getSelectionModel().addListSelectionListener(this::onTableSelection);
        BuscarIDTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onSearch();
            }
        });
    }

    public void bind(MedicamentoController controller, MedicamentoTableModel model, List<Medicamento> datosIniciales) {
        this.medicamentoController = controller;
        this.medicamentoTableModel = model;
        TablaMedicos.setModel(model);
        if (datosIniciales != null) medicamentoTableModel.setRows(datosIniciales);
        codigoTF.requestFocus();
    }

    // Agregar estilos a los paneles contenedores
    private void configurarPanel(JPanel panel, String titulo, Color bordeColor, Font fuente, Color tituloColor) {
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(bordeColor, 1),
                        titulo,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        fuente,
                        tituloColor
                )
        );
    }

    private void onAdd() {
        try {
            requireBound();
            DatosForm d = readForm();
            medicamentoController.agregar(d.codigo, d.nombreMedic, d.presentacion);
            onClear();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage(), ex);
        }
    }

    private void onUpdate() {
        try {
            requireBound();
            DatosForm d = readForm();
            medicamentoController.actualizar(d.codigo, d.nombreMedic, d.presentacion);
            onClear();
        } catch (Exception ex) {
            showError("Error al actualizar: " + ex.getMessage(), ex);
        }
    }

    private void onDelete() {
        try {
            requireBound();
            String codigo = safe(codigoTF.getText());
            if (codigo.isEmpty()) { warn("Código inválido."); return; }
            int op = JOptionPane.showConfirmDialog(PanelBase,
                    "¿Eliminar medicamento con código " + codigo + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                medicamentoController.eliminar(codigo);
                onClear();
            }
        } catch (Exception ex) {
            showError("Error al borrar: " + ex.getMessage(), ex);
        }
    }

    private void onClear(){
        codigoTF.setText("");
        nombreTF.setText("");
        PresentacionTF.setText("");
        codigoTF.requestFocus();
        try {
            medicamentoTableModel.setRows(medicamentoController.leerTodos());
        } catch (Exception ignored) {}
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (medicamentoTableModel == null) return;
        int row = TablaMedicos.getSelectedRow();
        if (row < 0) return;
        Medicamento medicamento = medicamentoTableModel.getAt(row);
        if (medicamento == null) return;

        codigoTF.setText(medicamento.getCodigo());
        nombreTF.setText(medicamento.getNombreMedic());
        PresentacionTF.setText(medicamento.getPresentacion());
    }

    // Búsqueda en tiempo real de la tabla
    private void onSearch() {
        try {
            requireBound();
            String texto = safe(BuscarIDTF.getText()).toLowerCase();
            if (texto.isEmpty()) {
                medicamentoTableModel.setRows(medicamentoController.leerTodos());
                return;
            }
            List<Medicamento> all = medicamentoController.leerTodos();
            java.util.List<Medicamento> filtered = new java.util.ArrayList<>();
            for (Medicamento m : all) {
                if (m.getCodigo() != null && m.getCodigo().toLowerCase().contains(texto)) {
                    filtered.add(m);
                }
            }
            medicamentoTableModel.setRows(filtered);
        } catch (Exception ex) {
            showError("Error en búsqueda: " + ex.getMessage(), ex);
        }
    }

    private static class DatosForm {
        String codigo; String nombreMedic; String presentacion;
    }

    private DatosForm readForm() {
        DatosForm d = new DatosForm();
        d.codigo = safe(codigoTF.getText());
        d.nombreMedic = safe(nombreTF.getText());
        d.presentacion = safe(PresentacionTF.getText());

        if (d.codigo.isEmpty()) throw new IllegalArgumentException("El código es obligatorio.");
        if (d.nombreMedic.isEmpty()) throw new IllegalArgumentException("El nombre del medicamento es obligatorio.");
        if (d.presentacion.isEmpty()) throw new IllegalArgumentException("La presentación es obligatoria.");

        return d;
    }

    private void requireBound() {
        if (medicamentoController == null || medicamentoTableModel == null)
            throw new IllegalStateException("MedicamentoView no está enlazado (bind) a controller/model.");
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }

    private void showError(String msg, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(PanelBase, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void warn(String msg) {
        JOptionPane.showMessageDialog(PanelBase, msg, "Atención", JOptionPane.WARNING_MESSAGE);
    }

}
