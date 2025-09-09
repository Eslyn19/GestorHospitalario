package cr.ac.una.presentation_layer.Model;

import cr.ac.una.domain_layer.Receta;
import cr.ac.una.service_layer.IServiceObserver;
import cr.ac.una.utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecetaTableModel extends AbstractTableModel implements IServiceObserver<Receta> {
    private final String[] cols = {"Receta ID", "Paciente", "Médico", "Fecha Confección", "Fecha Retiro", "Estado", "Prescripciones"};
    private final Class<?>[] types = { String.class, String.class, String.class, String.class, String.class, String.class, Integer.class};
    private final List<Receta> rows = new ArrayList<>();

    public void setRows(List<Receta> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Receta getAt(int row) { 
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null; 
    }

    // ----- AbstractTableModel -----
    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column){ return cols[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex];}
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Receta receta = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return receta.getId();
            case 1: return receta.getPaciente();
            case 2: return receta.getMedico();
            case 3: return receta.getFechaConfeccion() != null ? receta.getFechaConfeccion().toString() : "";
            case 4: return receta.getFechaRetiro() != null ? receta.getFechaRetiro().toString() : "";
            case 5: return receta.getEstado();
            case 6: return receta.getPrescripciones().size();
            default: return null;
        }
    }

    // Observer
    @Override
    public void onDataChanged(ChangeType type, Receta receta) {
        switch (type) {
            case CREATED: {
                rows.add(receta);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }

            case UPDATED: {
                int i = indexOf(receta.getId());
                if (i >= 0) {
                    rows.set(i, receta);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            
            case DELETED: {
                int i = indexOf(receta.getId());
                if (i >= 0) {
                    rows.remove(i);
                    fireTableRowsDeleted(i, i);
                }
                break;
            }
        }
    }

    private int indexOf(String id) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getId().equals(id)) return i;
        }
        return -1;
    }
}
