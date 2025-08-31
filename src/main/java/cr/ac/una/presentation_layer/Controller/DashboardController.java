package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.data_access_layer.RecetaFileStore;
import cr.ac.una.presentation_layer.Views.DashboardView.DashboardView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.time.YearMonth;
import java.util.Map;

public class DashboardController {
    private DashboardView view;
    private RecetaFileStore recetaStore;

    public DashboardController(RecetaFileStore recetaStore, DashboardView view) {
        this.recetaStore = recetaStore;
        this.view = view;
        init();
    }

    private void init() {
        view.cbMedicamento.removeAllItems();
        for (String m : recetaStore.getMedicamentosDisponibles()) view.cbMedicamento.addItem(m);
        view.btnActualizar.addActionListener(e -> refreshCharts());
        refreshCharts();
    }

    private void refreshCharts() {
        String med = (String) view.cbMedicamento.getSelectedItem();
        int fm = (Integer) view.spFromMonth.getValue();
        int fy = (Integer) view.spFromYear.getValue();
        int tm = (Integer) view.spToMonth.getValue();
        int ty = (Integer) view.spToYear.getValue();
        YearMonth from = YearMonth.of(fy, fm);
        YearMonth to = YearMonth.of(ty, tm);

        SwingWorker<Void, Void> w = new SwingWorker<>() {
            Map<YearMonth,Integer> porMes;
            Map<String,Integer> porEstado;
            @Override protected Void doInBackground() {
                porMes = recetaStore.getPrescripcionesPorMes(med, from, to);
                porEstado = recetaStore.getRecetasPorEstado();
                return null;
            }
            @Override protected void done() {
                TimeSeries ts = new TimeSeries("Prescripciones");
                for (Map.Entry<YearMonth,Integer> e : porMes.entrySet()) {
                    YearMonth ym = e.getKey();
                    ts.addOrUpdate(new Month(ym.getMonthValue(), ym.getYear()), e.getValue());
                }
                TimeSeriesCollection dataset = new TimeSeriesCollection(ts);
                var chartLine = ChartFactory.createTimeSeriesChart("Medicamentos prescritos por mes", "Mes", "Cantidad", dataset, false, true, false);
                view.setLineChartPanel(new ChartPanel(chartLine));

                DefaultPieDataset pie = new DefaultPieDataset();
                for (Map.Entry<String,Integer> e : porEstado.entrySet()) pie.setValue(e.getKey(), e.getValue());
                var chartPie = ChartFactory.createPieChart("Recetas por estado", pie, true, true, false);
                view.setPieChartPanel(new ChartPanel(chartPie));
            }
        };
        w.execute();
    }
}
