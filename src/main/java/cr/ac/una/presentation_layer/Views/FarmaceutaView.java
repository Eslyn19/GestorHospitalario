package cr.ac.una.presentation_layer.Views;

import cr.ac.una.domain_layer.Farmaceuta;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;
import cr.ac.una.presentation_layer.Model.FarmaceutaTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class FarmaceutaView extends JFrame{
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
    private JLabel idLabel;
    private JButton ActualizarBTN;

    private FarmaceutaController farmaceutaController;
    private FarmaceutaTableModel farmaceutaTableModel;

    public FarmaceutaView(FarmaceutaController farmaceutaController, FarmaceutaTableModel farmaceutaTableModel, List<Farmaceuta> datos) {
        this(farmaceutaController, farmaceutaTableModel, datos, true);
    }
    
    public FarmaceutaView(FarmaceutaController farmaceutaController, FarmaceutaTableModel farmaceutaTableModel, List<Farmaceuta> datos, boolean showAsWindow) {
        this.farmaceutaController = farmaceutaController;
        this.farmaceutaTableModel = farmaceutaTableModel;
        addListeners();
        bind(farmaceutaController, farmaceutaTableModel, datos);

        Color white = new Color(255, 255, 255);
        configurarPanel(UpperPanel, "Farmaceuta", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(MidPanel, "Búsqueda", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        configurarPanel(LowerPanel, "Listado", white, new Font("Arial", Font.BOLD, 13), Color.WHITE);

        ID_textfield.setPreferredSize(new Dimension(20, 25));
        nombreTF.setPreferredSize(new Dimension(20, 25));
        ApellidoTF.setPreferredSize(new Dimension(20, 25));
        BuscarIDTF.setPreferredSize(new Dimension(20, 25));

        if (showAsWindow) {
            setContentPane(PanelBase);
            ImageIcon icon = new ImageIcon(getClass().getResource("/Farmaceutas.png"));
            setIconImage(icon.getImage());
            setLocationRelativeTo(null);
            setTitle("Farmaceutas");
            setSize(850, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }
    
    // Static method to create farmaceuta panel for embedding
    public static JPanel createFarmaceutaPanel(FarmaceutaController farmaceutaController, FarmaceutaTableModel tableModel, List<Farmaceuta> farmaceutas) {
        FarmaceutaView farmaceutaView = new FarmaceutaView(farmaceutaController, tableModel, farmaceutas, false);
        return farmaceutaView.getPanelBase();
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
    
    /**
     * Maneja el evento de cambiar clave
     */
    private void onCambiarClave() {
        // Verificar si hay un farmacéuta seleccionado en la tabla
        int selectedRow = TablaMedicos.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un farmacéuta de la tabla para cambiar su clave.", 
                "Seleccionar Farmacéuta", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el farmacéuta seleccionado
        Farmaceuta selectedFarmaceuta = farmaceutaTableModel.getAt(selectedRow);
        if (selectedFarmaceuta != null) {
            // Mostrar información del farmacéuta cuya clave se va a cambiar
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea cambiar la clave del farmacéuta " + selectedFarmaceuta.getNombre() + " " + selectedFarmaceuta.getApellido() + 
                " (ID: " + selectedFarmaceuta.getID() + ")?",
                "Confirmar Cambio de Clave",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                // Abrir ventana de cambiar clave pasando el farmacéuta y el controlador
                SwingUtilities.invokeLater(() -> {
                    CambiarClave ventanaCambiarClave = new CambiarClave(selectedFarmaceuta, farmaceutaController);
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
            
            // Buscar el farmacéuta por ID
            Farmaceuta farmaceuta = farmaceutaController.leerPorId(id);
            
            if (farmaceuta != null) {
                // Mostrar los datos del farmacéuta en un JOptionPane
                String mensaje = String.format(
                    "Farmacéuta encontrado:\n\n" +
                    "ID: %d\n" +
                    "Nombre: %s\n" +
                    "Apellido: %s\n" +
                    "Clave: %s",
                    farmaceuta.getID(),
                    farmaceuta.getNombre(),
                    farmaceuta.getApellido(),
                    farmaceuta.getClave()
                );
                
                JOptionPane.showMessageDialog(this,
                    mensaje,
                    "Farmacéuta ID: " + id,
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Farmacéuta no existe
                JOptionPane.showMessageDialog(this,
                    "No se encontró ningún farmacéuta con el ID: " + id,
                    "Farmacéuta no encontrado",
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar el farmacéuta: " + e.getMessage(),
                "Error de búsqueda",
                JOptionPane.ERROR_MESSAGE);
        }
        BuscarIDTF.setText("");
    }

    public void bind(FarmaceutaController controller, FarmaceutaTableModel model, List<Farmaceuta> datosIniciales) {
        this.farmaceutaController = controller;
        this.farmaceutaTableModel = model;
        TablaMedicos.setModel(model);
        if (datosIniciales != null) farmaceutaTableModel.setRows(datosIniciales);
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
            farmaceutaController.agregar(d.ID, d.nombre, d.apellido);
            onClear();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage(), ex);
        }
    }

    private void onUpdate() {
        try {
            requireBound();
            DatosForm d = readForm();
            farmaceutaController.Actualizar(d.ID, d.nombre, d.apellido);
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
                    "¿Eliminar farmaceuta con ID " + id + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                farmaceutaController.eliminar(id);
                onClear();
            }
        } catch (Exception ex) {
            showError("Error al borrar: " + ex.getMessage(), ex);
        }
    }

    // Limpiar campos de palabras
    private void onClear(){
        ID_textfield.setText("");
        nombreTF.setText("");
        ApellidoTF.setText("");
        ID_textfield.requestFocus(); // *opc
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (farmaceutaTableModel == null) return;
        int row = TablaMedicos.getSelectedRow();
        if (row < 0) return;
        Farmaceuta farmaceuta = farmaceutaTableModel.getAt(row);
        if (farmaceuta == null) return;

        ID_textfield.setText(String.valueOf(farmaceuta.getID()));
        nombreTF.setText(farmaceuta.getNombre());
        ApellidoTF.setText(farmaceuta.getApellido());
    }

    private static class DatosForm {
        int ID; String nombre; String apellido;
    }

    private DatosForm readForm() {
        DatosForm d = new DatosForm();
        d.ID    = orDefault(parseInt(ID_textfield.getText()), -1);
        d.nombre= safe(nombreTF.getText());
        d.apellido   = safe(ApellidoTF.getText());

        if (d.ID <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
        if (d.nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (d.apellido.isEmpty()) throw new IllegalArgumentException("El apellido es obligatorio.");
        
        return d;
    }

    private void requireBound() {
        if (farmaceutaController == null || farmaceutaTableModel == null)
            throw new IllegalStateException("FarmaceutaView no está enlazado (bind) a controller/model.");
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
