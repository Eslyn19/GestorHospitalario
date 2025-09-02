package cr.ac.una.presentation_layer.Views.HistorialRecetas;

import cr.ac.una.domain_layer.Receta;
import cr.ac.una.domain_layer.RecetaDetalle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecetaDetalleDialog extends JDialog {

    private final Receta receta;

    public RecetaDetalleDialog(Frame owner, Receta receta) {
        super(owner, "Detalle de receta " + (receta == null ? "" : receta.getId()), true);
        this.receta = receta;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(6,6));

        JPanel top = new JPanel(new GridLayout(1, 3, 6,6));
        top.add(new JLabel("ID: " + (receta == null ? "" : receta.getId())));
        top.add(new JLabel("Fecha: " + (receta == null ? "" : receta.getFecha())));
        top.add(new JLabel("Estado: " + (receta == null ? "" : receta.getEstado())));
        add(top, BorderLayout.NORTH);

        // tabla de detalles
        String[] cols = {"Código", "Cantidad"};
        List<RecetaDetalle> detalles = receta == null ? java.util.Collections.emptyList() : receta.getDetalle();
        Object[][] data = new Object[detalles.size()][cols.length];
        for (int i = 0; i < detalles.size(); i++) {
            RecetaDetalle d = detalles.get(i);
            data[i][0] = d.getCodigo();
            data[i][1] = d.getCantidad();
        }
        JTable tbl = new JTable(data, cols);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btn = new JButton("Cerrar");
        btn.addActionListener(e -> dispose());
        bot.add(btn);
        add(bot, BorderLayout.SOUTH);
    }
}
