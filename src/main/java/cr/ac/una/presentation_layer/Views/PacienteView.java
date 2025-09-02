package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.presentation_layer.Controller.PacienteController;
import cr.ac.una.presentation_layer.Model.PacienteTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacienteView extends JFrame{
    private JPanel PanelBase;
    private JPanel UpperPanel;
    private JPanel LowerPanel;
    private JPanel MidPanel;
    private JPanel UpperPanelBTN;
    private JPanel UpperPanelTF;
    private JPanel IDESPanel;
    private JPanel NombresPanel;
    private JTextField ID_textfield;
    private JPanel ApellidoPanel;
    private JPanel NombrePanel2;
    private JPanel NomApPanel;
    private JLabel nombreLabel;
    private JTextField nombreTF;
    private JLabel apellidoLabel;
    private JTextField ApellidoTF;
    private JButton GuardarBTN;
    private JButton LimpiarBTN;
    private JButton EliminarBTN;
    private JTable TablaMedicos;
    private JScrollPane JSrollPane;
    private JTextField BuscarIDTF;
    // private JButton BuscarBTN;  <-- eliminado
    private JPanel BuscarMedicoPanel;
    private JPanel SpacePanel;
    private JLabel IDBuscarLabel;
    private JLabel idLabel;
    private JButton ActualizarBTN;
    private JTextField TelefonoTF;
    private JLabel labelTelefono;
    private JTextField FechaTF;
    private JLabel labelFecha;

    private PacienteController pacienteController;
    private PacienteTableModel pacienteTableModel;

    public PacienteView(PacienteController pacienteController, PacienteTableModel pacienteTableModel, List<Paciente> datos) {
        this(pacienteController, pacienteTableModel, datos, true);
    }

    public PacienteView(PacienteController pacienteController, PacienteTableModel pacienteTableModel, List<Paciente> datos, boolean showAsWindow) {
        this.pacienteController = pacienteController;
        this.pacienteTableModel = pacienteTableModel;
        addListeners();
        bind(pacienteController, pacienteTableModel, datos);

        Color white = new Color(255,255,255);
        configurarPanel(UpperPanel, "Paciente", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(MidPanel, "Búsqueda", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(LowerPanel, "Listado", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);

        ID_textfield.setPreferredSize(new Dimension(20, 25));
        nombreTF.setPreferredSize(new Dimension(20, 25));
        FechaTF.setPreferredSize(new Dimension(20, 25));
        TelefonoTF.setPreferredSize(new Dimension(20, 25));
        BuscarIDTF.setPreferredSize(new Dimension(20, 25));

        if (showAsWindow) {
            setContentPane(PanelBase);
            ImageIcon icon = new ImageIcon(getClass().getResource("/Paciente.png"));
            setIconImage(icon.getImage());
            setLocationRelativeTo(null);
            setTitle("Pacientes");
            setSize(850, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }

    //Método estático para crear un panel
    public static JPanel createPacientePanel(PacienteController pacienteController, PacienteTableModel tableModel, List<Paciente> pacientes) {
        PacienteView pacienteView = new PacienteView(pacienteController, tableModel, pacientes, false);
        return pacienteView.getPanelBase();
    }

    public JPanel getPanelBase() { return PanelBase; }

    private void addListeners() {
        GuardarBTN.addActionListener(e -> onAdd());
        ActualizarBTN.addActionListener(e -> onUpdate());
        EliminarBTN.addActionListener(e -> onDelete());
        LimpiarBTN.addActionListener(e -> onClear());
        TablaMedicos.getSelectionModel().addListSelectionListener(this::onTableSelection);

        // Búsqueda en tiempo real: filtra mientras se escribe
        BuscarIDTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onSearch();
            }
        });
    }

    public void bind(PacienteController controller, PacienteTableModel model, List<Paciente> datosIniciales) {
        this.pacienteController = controller;
        this.pacienteTableModel = model;
        TablaMedicos.setModel(model);
        if (datosIniciales != null) pacienteTableModel.setRows(datosIniciales);
        ID_textfield.requestFocus();
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
            pacienteController.agregar(d.ID, d.nombre, d.apellido, d.fechaNacimiento, d.telefono);
            onClear();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage(), ex);
        }
    }

    private void onUpdate() {
        try {
            requireBound();
            DatosForm d = readForm();
            pacienteController.Actualizar(d.ID, d.nombre, d.apellido, d.fechaNacimiento, d.telefono);
            onClear();
        } catch (Exception ex) {
            showError("Error al actualizar: " + ex.getMessage(), ex);
        }
    }

    private void onDelete() {
        try {
            requireBound();
            Integer id = parseInt(ID_textfield.getText());
            if (id == null || id <= 0) { warn("ID inválido."); return; }
            int op = JOptionPane.showConfirmDialog(PanelBase,
                    "¿Eliminar paciente con ID " + id + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                pacienteController.eliminar(id);
                onClear();
            }
        } catch (Exception ex) {
            showError("Error al borrar: " + ex.getMessage(), ex);
        }
    }

    // Limpiar campos
    private void onClear(){
        ID_textfield.setText("");
        nombreTF.setText("");
        ApellidoTF.setText("");
        FechaTF.setText("");
        TelefonoTF.setText("");
        ID_textfield.requestFocus();
        // restaurar listado completo
        try {
            pacienteTableModel.setRows(pacienteController.leerTodos());
        } catch (Exception ex) {
            // ignorar
        }
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (pacienteTableModel == null) return;
        int row = TablaMedicos.getSelectedRow();
        if (row < 0) return;
        Paciente paciente = pacienteTableModel.getAt(row);
        if (paciente == null) return;

        ID_textfield.setText(String.valueOf(paciente.getID()));
        nombreTF.setText(paciente.getNombre());
        ApellidoTF.setText(paciente.getApellido());
        FechaTF.setText(paciente.getFechaNacimiento());
        TelefonoTF.setText(paciente.getTelefono());
    }

    // Buscar (filtrado por ID, nombre o apellido) - ahora en tiempo real
    private void onSearch() {
        try {
            requireBound();
            String texto = safe(BuscarIDTF.getText()).toLowerCase();
            if (texto.isEmpty()) {
                pacienteTableModel.setRows(pacienteController.leerTodos());
                return;
            }
            List<Paciente> all = pacienteController.leerTodos();
            java.util.List<Paciente> filtered = new java.util.ArrayList<>();
            for (Paciente p : all) {
                String idStr = String.valueOf(p.getID());
                if ((idStr != null && idStr.toLowerCase().contains(texto))
                        || (p.getNombre() != null && p.getNombre().toLowerCase().contains(texto))
                        || (p.getApellido() != null && p.getApellido().toLowerCase().contains(texto))) {
                    filtered.add(p);
                }
            }
            pacienteTableModel.setRows(filtered);
        } catch (Exception ex) {
            showError("Error en búsqueda: " + ex.getMessage(), ex);
        }
    }

    private static class DatosForm {
        int ID; String nombre; String apellido; String fechaNacimiento; String telefono;
    }

    private DatosForm readForm() {
        DatosForm d = new DatosForm();
        d.ID = orDefault(parseInt(ID_textfield.getText()), -1);
        d.nombre = safe(nombreTF.getText());
        d.apellido = safe(ApellidoTF.getText());
        d.fechaNacimiento = safe(FechaTF.getText());
        d.telefono = safe(TelefonoTF.getText());

        if (d.ID <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
        if (d.nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (d.apellido.isEmpty()) throw new IllegalArgumentException("El apellido es obligatorio.");
        if (d.fechaNacimiento.isEmpty()) throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        if (d.telefono.isEmpty()) throw new IllegalArgumentException("El teléfono es obligatorio.");

        return d;
    }

    private void requireBound() {
        if (pacienteController == null || pacienteTableModel == null)
            throw new IllegalStateException("PacienteView no está enlazado (bind) a controller/model.");
    }

    private Integer parseInt(String s) {
        try { return (s == null || s.trim().isEmpty()) ? null : Integer.parseInt(s.trim()); }
        catch (Exception e) { return null; }
    }
    private int orDefault(Integer v, int def) { return v == null ? def : v; }
    private String safe(String s) { return s == null ? "" : s.trim(); }

    private void showError(String msg, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(PanelBase, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void warn(String msg) {
        JOptionPane.showMessageDialog(PanelBase, msg, "Atención", JOptionPane.WARNING_MESSAGE);
    }

}
