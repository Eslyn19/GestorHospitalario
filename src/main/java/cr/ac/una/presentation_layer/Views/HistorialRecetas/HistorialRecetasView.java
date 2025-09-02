package cr.ac.una.presentation_layer.Views.HistorialRecetas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HistorialRecetasView {
    private JPanel PanelBase;

    // componentes públicos (como en DashboardView)
    public JTextField txtBuscar;
    public JButton btnRefrescar;
    public JButton btnVerDetalle;
    public JTable tableRecetas;

    private JPanel panelTableHolder;

    public HistorialRecetasView() {
        PanelBase = new JPanel(new BorderLayout(8,8));

        // Top: buscador + botones
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(28);
        btnRefrescar = new JButton("Refrescar");
        btnVerDetalle = new JButton("Ver detalle");

        top.add(new JLabel("Buscar (id, fecha, estado):"));
        top.add(txtBuscar);
        top.add(btnRefrescar);
        top.add(btnVerDetalle);

        PanelBase.add(top, BorderLayout.NORTH);

        // Center: holder para tabla
        panelTableHolder = new JPanel(new BorderLayout());
        panelTableHolder.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new java.awt.Color(166,166,166)),
                "Histórico de recetas",
                TitledBorder.LEFT, TitledBorder.TOP));
        // tabla vacía por defecto; el controller pondrá el TableModel
        tableRecetas = new JTable();
        tableRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelTableHolder.add(new JScrollPane(tableRecetas), BorderLayout.CENTER);

        PanelBase.add(panelTableHolder, BorderLayout.CENTER);
    }

    public JPanel getPanelBase() { return PanelBase; }

    //Permite que el controlador establezca el TableModel desde fuera.

    public void setTableModel(javax.swing.table.TableModel model) {
        tableRecetas.setModel(model);
        tableRecetas.revalidate();
        tableRecetas.repaint();
    }
}
