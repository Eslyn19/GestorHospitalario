package cr.ac.una.presentation_layer.Views.DashboardView;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DashboardView {
    private JPanel PanelBase;
    public JComboBox<String> cbMedicamento;
    public JSpinner spFromMonth, spFromYear, spToMonth, spToYear;
    public JButton btnActualizar;
    private JPanel panelLineaHolder;
    private JPanel panelPieHolder;

    public DashboardView() {
        PanelBase = new JPanel(new BorderLayout(8,8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbMedicamento = new JComboBox<>();
        top.add(new JLabel("Medicamento:")); top.add(cbMedicamento);

        spFromMonth = new JSpinner(new SpinnerNumberModel(1,1,12,1));
        spFromYear  = new JSpinner(new SpinnerNumberModel(2025,1900,2100,1));
        spToMonth   = new JSpinner(new SpinnerNumberModel(12,1,12,1));
        spToYear    = new JSpinner(new SpinnerNumberModel(2025,1900,2100,1));

        top.add(new JLabel("Desde (M/A):")); top.add(spFromMonth); top.add(spFromYear);
        top.add(new JLabel("Hasta (M/A):"));  top.add(spToMonth);  top.add(spToYear);

        btnActualizar = new JButton("Actualizar");
        top.add(btnActualizar);

        PanelBase.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1,2,8,8));
        panelLineaHolder = new JPanel(new BorderLayout());
        panelPieHolder   = new JPanel(new BorderLayout());
        center.add(panelLineaHolder);
        center.add(panelPieHolder);
        PanelBase.add(center, BorderLayout.CENTER);

        panelLineaHolder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(166,166,166)), "Prescripciones por mes", TitledBorder.LEFT, TitledBorder.TOP));
        panelPieHolder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(166,166,166)), "Recetas por estado", TitledBorder.LEFT, TitledBorder.TOP));
    }

    public JPanel getPanelBase() { return PanelBase; }

    public void setLineChartPanel(ChartPanel cp) {
        panelLineaHolder.removeAll(); panelLineaHolder.add(cp, BorderLayout.CENTER);
        panelLineaHolder.revalidate(); panelLineaHolder.repaint();
    }
    public void setPieChartPanel(ChartPanel cp) {
        panelPieHolder.removeAll(); panelPieHolder.add(cp, BorderLayout.CENTER);
        panelPieHolder.revalidate(); panelPieHolder.repaint();
    }
}
