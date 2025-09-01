package cr.ac.una.presentation_layer.Model;

import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.service_layer.IServiceObserver;
import cr.ac.una.utilities.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PacienteTableModel extends AbstractTableModel implements IServiceObserver<Paciente> {
    private final String[] cols = {"ID", "Nombre", "Apellido", "Fecha Nacimiento", "Teléfono"};
    private final Class<?>[] types = { Integer.class, String.class, String.class, String.class, String.class};
    private final List<Paciente> rows = new ArrayList<>();

    public void setRows(List<Paciente> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Paciente getAt(int row) { return (row >= 0 && row < rows.size()) ? rows.get(row) : null; }

    // ----- AbstractTableModel -----
    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column){ return cols[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex];}
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Paciente paciente = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return paciente.getID();
            case 1: return paciente.getNombre();
            case 2: return paciente.getApellido();
            case 3: return paciente.getFechaNacimiento();
            case 4: return paciente.getTelefono();
            default: return null;
        }
    }

    // Observer
    @Override
    public void onDataChanged(ChangeType type, Paciente paciente) {
        switch (type) {
            case CREATED: {
                rows.add(paciente);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }

            case UPDATED: {
                int i = indexOf(paciente.getID());
                if (i >= 0) {
                    rows.set(i, paciente);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            
            case DELETED: {
                int i = indexOf(paciente.getID());
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
