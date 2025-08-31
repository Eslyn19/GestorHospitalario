package cr.ac.una.presentation_layer.Model;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.service_layer.IServiceObserver;
import cr.ac.una.utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DoctorTableModel extends AbstractTableModel implements IServiceObserver<Doctor> {
    private final String[] cols = {"ID", "Nombre", "Apellido", "Especialidad"};
    private final Class<?>[] types = { Integer.class, String.class, String.class, String.class};
    private final List<Doctor> rows = new ArrayList<>();

    public void setRows(List<Doctor> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Doctor getAt(int row) { return (row >= 0 && row < rows.size()) ? rows.get(row) : null; }

    // ----- AbstractTableModel -----
    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column){ return cols[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex];}
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Doctor doctor = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return doctor.getID();
            case 1: return doctor.getNombre();
            case 2: return doctor.getApellido();
            case 3: return doctor.getEspecialidad();
            default: return null;
        }
    }

    // Observer
    @Override
    public void onDataChanged(ChangeType type, Doctor doctor) {
        switch (type) {
            case CREATED: {
                rows.add(doctor);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }

            case UPDATED: {
                int i = indexOf(doctor.getID());
                if (i >= 0) {
                    rows.set(i, doctor);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            
            case DELETED: {
                int i = indexOf(doctor.getID());
                if (i >= 0) {
                    rows.remove(i);
                    fireTableRowsDeleted(i, i);
                }
                break;
            }
        }
    }

    private int indexOf(int id) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getID() == id) return i;
        }
        return -1;
    }
}