package cr.ac.una.presentation_layer.Views.HistorialRecetas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HistorialRecetasView {
    private JPanel PanelBase;

    public JTextField txtBuscar;
    public JButton btnRefrescar;
    public JButton btnVerDetalle;
    public JTable tableRecetas;

    private JPanel panelTableHolder;

    // Listener que el controlador puede registrar para recibir eventos de búsqueda
    private SearchListener searchListener;

    public HistorialRecetasView() {
        PanelBase = new JPanel(new BorderLayout(8,8));

        // buscador y botones
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(28);
        btnRefrescar = new JButton("Refrescar");
        btnVerDetalle = new JButton("Ver detalle");

        // etiqueta actualizada: ahora solo busca por ID
        top.add(new JLabel("Buscar (id):"));
        top.add(txtBuscar);
        top.add(btnRefrescar);
        top.add(btnVerDetalle);

        PanelBase.add(top, BorderLayout.NORTH);

        // listener para búsqueda en tiempo real (keyReleased)
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onSearchTextChanged();
            }
        });

        // holder para tabla
        panelTableHolder = new JPanel(new BorderLayout());
        panelTableHolder.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new java.awt.Color(166,166,166)),
                "Histórico de recetas",
                TitledBorder.LEFT, TitledBorder.TOP));
        // el controller pone el TableModel
        tableRecetas = new JTable();
        tableRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelTableHolder.add(new JScrollPane(tableRecetas), BorderLayout.CENTER);

        PanelBase.add(panelTableHolder, BorderLayout.CENTER);
    }

    public JPanel getPanelBase() { return PanelBase; }

    //Permite que el controlador establezca el TableModel desde fuera
    public void setTableModel(javax.swing.table.TableModel model) {
        tableRecetas.setModel(model);
        tableRecetas.revalidate();
        tableRecetas.repaint();
    }

    //El listener recibirá la cadena actual (vacía = restaurar todos)
    public void setSearchListener(SearchListener listener) {
        this.searchListener = listener;
    }

    // Llamado internamente cuando cambia el texto de búsqueda (tiempo real)
    private void onSearchTextChanged() {
        String texto = safe(txtBuscar.getText()).toLowerCase();
        if (searchListener != null) {
            // si está vacío entonces muestra todos
            searchListener.onSearchTextChanged(texto);
        }
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }

    //Interfaz que debe implementar quien controle la vista para recibir eventos de búsqueda en tiempo real.
    public interface SearchListener {
        void onSearchTextChanged(String text);
    }
}

