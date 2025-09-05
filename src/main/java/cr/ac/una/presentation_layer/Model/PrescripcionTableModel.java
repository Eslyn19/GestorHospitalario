package cr.ac.una.presentation_layer.Model;

import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.service_layer.IServiceObserver;
import cr.ac.una.utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PrescripcionTableModel extends AbstractTableModel implements IServiceObserver<PrescripcionMedicamento> {
    private final String[] cols = {"Medicamento", "Cantidad", "Indicaciones", "Duración"};
    private final Class<?>[] types = { String.class, Integer.class, String.class, String.class};
    private final List<PrescripcionMedicamento> rows = new ArrayList<>();

    public void setRows(List<PrescripcionMedicamento> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public PrescripcionMedicamento getAt(int row) { 
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null; 
    }

    // ----- AbstractTableModel -----
    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column){ return cols[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex];}
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        PrescripcionMedicamento prescripcion = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return prescripcion.getMedicamento();
            case 1: return prescripcion.getCantidad();
            case 2: return prescripcion.getIndicaciones();
            case 3: return prescripcion.getDuracion();
            default: return null;
        }
    }

    // Observer
    @Override
    public void onDataChanged(ChangeType type, PrescripcionMedicamento prescripcion) {
        switch (type) {
            case CREATED: {
                rows.add(prescripcion);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }

            case UPDATED: {
                int i = indexOf(prescripcion.getMedicamento());
                if (i >= 0) {
                    rows.set(i, prescripcion);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            
            case DELETED: {
                int i = indexOf(prescripcion.getMedicamento());
                if (i >= 0) {
                    rows.remove(i);
                    fireTableRowsDeleted(i, i);
                }
                break;
            }
        }
    }

    private int indexOf(String medicamento) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getMedicamento().equals(medicamento)) return i;
        }
        return -1;
    }
}
