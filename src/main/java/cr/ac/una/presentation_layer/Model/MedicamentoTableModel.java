package cr.ac.una.presentation_layer.Model;

import cr.ac.una.domain_layer.Medicamento;
import cr.ac.una.service_layer.IServiceObserver;
import cr.ac.una.utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoTableModel extends AbstractTableModel implements IServiceObserver<Medicamento> {
    private final String[] cols = {"Código", "Nombre", "Presentación"};
    private final Class<?>[] types = { String.class, String.class, String.class};
    private final List<Medicamento> rows = new ArrayList<>();

    public void setRows(List<Medicamento> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Medicamento getAt(int row) { return (row >= 0 && row < rows.size()) ? rows.get(row) : null; }

    // ----- AbstractTableModel -----
    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column){ return cols[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex];}
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Medicamento medicamento = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return medicamento.getCodigo();
            case 1: return medicamento.getNombreMedic();
            case 2: return medicamento.getPresentacion();
            default: return null;
        }
    }

    // Observer
    @Override
    public void onDataChanged(ChangeType type, Medicamento medicamento) {
        switch (type) {
            case CREATED: {
                rows.add(medicamento);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }

            case UPDATED: {
                int i = indexOf(medicamento.getCodigo());
                if (i >= 0) {
                    rows.set(i, medicamento);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            
            case DELETED: {
                int i = indexOf(medicamento.getCodigo());
                if (i >= 0) {
                    rows.remove(i);
                    fireTableRowsDeleted(i, i);
                }
                break;
            }
        }
    }

    private int indexOf(String codigo) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getCodigo().equals(codigo)) return i;
        }
        return -1;
    }
}
