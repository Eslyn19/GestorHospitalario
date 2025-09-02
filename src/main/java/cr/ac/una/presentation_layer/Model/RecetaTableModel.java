package cr.ac.una.presentation_layer.Model;

import cr.ac.una.domain_layer.Receta;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecetaTableModel extends AbstractTableModel {
    private final String[] cols = {"ID", "Fecha", "Estado", "Cant. Medicamentos"};
    private final Class<?>[] types = { String.class, String.class, String.class, Integer.class };
    private final List<Receta> rows = new ArrayList<>();

    public void setRows(List<Receta> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Receta getAt(int row) { return (row >= 0 && row < rows.size()) ? rows.get(row) : null; }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column){ return cols[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex];}
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Receta r = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return r.getId();
            case 1: return r.getFecha();
            case 2: return r.getEstado();
            case 3: return r.getDetalle() == null ? 0 : r.getDetalle().size();
            default: return null;
        }
    }
}

