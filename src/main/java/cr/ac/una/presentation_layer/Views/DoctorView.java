package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Model.DoctorTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class DoctorView extends JFrame{
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
    private JButton BuscarBTN;
    private JButton CambiarClaveBTN;
    private JPanel BuscarMedicoPanel;
    private JPanel SpacePanel;
    private JLabel IDBuscarLabel;
    private JTextField EspecialidadTF;
    private JLabel idLabel;
    private JButton ActualizarBTN;

    private DoctorController doctorController;
    private DoctorTableModel doctorTableModel;

    public DoctorView(DoctorController doctorController,  DoctorTableModel doctorTableModel, List<Doctor> datos) {
        this(doctorController, doctorTableModel, datos, true);
    }
    
    public DoctorView(DoctorController doctorController,  DoctorTableModel doctorTableModel, List<Doctor> datos, boolean showAsWindow) {
        this.doctorController = doctorController;
        this.doctorTableModel = doctorTableModel;
        addListeners();
        bind(doctorController, doctorTableModel, datos);

        Color white = new Color(255, 255, 255);
        configurarPanel(UpperPanel, "Medico", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(MidPanel, "Búsqueda", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(LowerPanel, "Listado", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);

        ID_textfield.setPreferredSize(new Dimension(20, 25));
        EspecialidadTF.setPreferredSize(new Dimension(20, 25));
        nombreTF.setPreferredSize(new Dimension(20, 25));
        ApellidoTF.setPreferredSize(new Dimension(20, 25));
        BuscarIDTF.setPreferredSize(new Dimension(20, 25));

        if (showAsWindow) {
            setContentPane(PanelBase);
            ImageIcon icon = new ImageIcon(getClass().getResource("/Doctores.png"));
            setIconImage(icon.getImage());
            setLocationRelativeTo(null);
            setTitle("Doctores");
            setSize(850, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }
    
    // Static method to create doctor panel for embedding
    public static JPanel createDoctorPanel(DoctorController doctorController, DoctorTableModel tableModel, List<Doctor> doctors) {
        DoctorView doctorView = new DoctorView(doctorController, tableModel, doctors, false);
        return doctorView.getPanelBase();
    }

    public JPanel getPanelBase() { return PanelBase; }

    private void addListeners() {
        GuardarBTN.addActionListener(e -> onAdd());
        ActualizarBTN.addActionListener(e -> onUpdate());
        EliminarBTN.addActionListener(e -> onDelete());
        LimpiarBTN.addActionListener(e -> onClear());
        CambiarClaveBTN.addActionListener(e -> onCambiarClave());
        BuscarBTN.addActionListener(e -> onBuscar());
        TablaMedicos.getSelectionModel().addListSelectionListener(this::onTableSelection);
    }

    private void onCambiarClave() {
        // Verificar si hay un doctor seleccionado en la tabla
        int selectedRow = TablaMedicos.getSelectedRow();

        // Obtener el doctor seleccionado
        Doctor selectedDoctor = doctorTableModel.getAt(selectedRow);
        if (selectedDoctor != null) {
            // Mostrar información del doctor cuya clave se va a cambiar
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea cambiar la clave del (ID: " + selectedDoctor.getID() + ")?",
                "Confirmar Cambio de Clave",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                // Abrir ventana de cambiar clave pasando el doctor y el controlador
                SwingUtilities.invokeLater(() -> {
                    CambiarClave ventanaCambiarClave = new CambiarClave(selectedDoctor, doctorController);
                });
            }
        }
    }
    
    private void onBuscar() {
        try {
            String idTexto = BuscarIDTF.getText().trim();
            
            // Validar que se haya ingresado un ID
            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un ID para buscar",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Convertir a entero
            int id;
            try {
                id = Integer.parseInt(idTexto);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número válido",
                    "ID inválido",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buscar el doctor por ID
            Doctor doctor = doctorController.leerPorId(id);
            
            if (doctor != null) {
                // Mostrar los datos del doctor en un JOptionPane
                String mensaje = String.format(
                    "Doctor encontrado:\n\n" +
                    "ID: %d\n" +
                    "Nombre: %s\n" +
                    "Apellido: %s\n" +
                    "Especialidad: %s\n" +
                    "Clave: %s",
                    doctor.getID(),
                    doctor.getNombre(),
                    doctor.getApellido(),
                    doctor.getEspecialidad(),
                    doctor.getClave()
                );
                
                JOptionPane.showMessageDialog(this,
                    mensaje,
                    "Doctor ID: " + id,
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Doctor no existe
                JOptionPane.showMessageDialog(this,
                    "No se encontró ningún doctor con el ID: " + id,
                    "Doctor no encontrado",
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar el doctor: " + e.getMessage(),
                "Error de búsqueda",
                JOptionPane.ERROR_MESSAGE);
        }
        BuscarIDTF.setText("");
    }

    public void bind(DoctorController controller, DoctorTableModel model, List<Doctor> datosIniciales) {
        this.doctorController = controller;
        this.doctorTableModel = model;
        TablaMedicos.setModel(model);
        if (datosIniciales != null) doctorTableModel.setRows(datosIniciales);
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
            doctorController.agregar(d.ID, d.nombre, d.apellido, d.especialidad);
            onClear();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage(), ex);
        }
    }

    private void onUpdate() {
        try {
            requireBound();
            DatosForm d = readForm();
            doctorController.Actualizar(d.ID, d.nombre, d.apellido, d.especialidad);
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
                    "¿Eliminar cliente con ID " + id + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                doctorController.eliminar(id);
                onClear();
            }
        } catch (Exception ex) {
            showError("Error al borrar: " + ex.getMessage(), ex);
        }
    }

    // Limpiar campos de palabras
    private void onClear(){
        ID_textfield.setText("");
        EspecialidadTF.setText("");
        nombreTF.setText("");
        ApellidoTF.setText("");
        ID_textfield.requestFocus(); // *opc
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (doctorTableModel == null) return;
        int row = TablaMedicos.getSelectedRow();
        if (row < 0) return;
        Doctor doctor = doctorTableModel.getAt(row);
        if (doctor == null) return;

        ID_textfield.setText(String.valueOf(doctor.getID()));
        nombreTF.setText(doctor.getNombre());
        ApellidoTF.setText(doctor.getApellido());
        EspecialidadTF.setText(doctor.getEspecialidad());
    }

    private static class DatosForm {
        int ID; String nombre; String apellido; String especialidad;
    }

    private DatosForm readForm() {
        DatosForm d = new DatosForm();
        d.ID    = orDefault(parseInt(ID_textfield.getText()), -1);
        d.nombre= safe(nombreTF.getText());
        d.apellido   = safe(ApellidoTF.getText());
        d.especialidad = safe(EspecialidadTF.getText());

        if (d.ID <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
        if (d.nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (d.apellido.isEmpty()) throw new IllegalArgumentException("El apellido es obligatorio.");
        if (d.especialidad.isEmpty()) throw new IllegalArgumentException("La especialidad es obligatoria.");
        
        return d;
    }

    private void requireBound() {
        if (doctorController == null || doctorTableModel == null)
            throw new IllegalStateException("DoctorView no está enlazado (bind) a controller/model.");
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
