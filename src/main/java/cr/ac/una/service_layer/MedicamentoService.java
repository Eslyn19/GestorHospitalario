package cr.ac.una.service_layer;

import cr.ac.una.data_access_layer.IFileStore;
import cr.ac.una.domain_layer.Medicamento;
import cr.ac.una.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoService implements IService<Medicamento> {
    private final IFileStore<Medicamento> medicamentoFileStore;
    private final List<IServiceObserver<Medicamento>> listenerMedicamentos = new ArrayList<>();

    public MedicamentoService(IFileStore<Medicamento> medicamentoFileStore) {
        this.medicamentoFileStore = medicamentoFileStore;
    }

    @Override
    public void agregar(Medicamento entity) {
        List<Medicamento> medicamentos = medicamentoFileStore.readAll();
        medicamentos.add(entity);
        medicamentoFileStore.writeAll(medicamentos);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void borrar(int id) {
        List<Medicamento> medicamentos = medicamentoFileStore.readAll();
        Medicamento removed = null;
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equals(String.valueOf(id))) { 
                removed = medicamentos.remove(i); 
                break; 
            }
        }
        medicamentoFileStore.writeAll(medicamentos);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    public void borrarPorCodigo(String codigo) {
        List<Medicamento> medicamentos = medicamentoFileStore.readAll();
        Medicamento removed = null;
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equals(codigo)) { 
                removed = medicamentos.remove(i); 
                break; 
            }
        }
        medicamentoFileStore.writeAll(medicamentos);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    @Override
    public void actualizar(Medicamento entity) {
        List<Medicamento> medicamentos = medicamentoFileStore.readAll();
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equals(entity.getCodigo())) {
                medicamentos.set(i, entity);
                break;
            }
        }
        medicamentoFileStore.writeAll(medicamentos);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<Medicamento> leerTodos() {
        return medicamentoFileStore.readAll();
    }

    @Override
    public Medicamento leerPorId(int id) {
        return medicamentoFileStore.readAll().stream()
                .filter(x -> x.getCodigo().equals(String.valueOf(id)))
                .findFirst().orElse(null);
    }

    public Medicamento leerPorCodigo(String codigo) {
        return medicamentoFileStore.readAll().stream()
                .filter(x -> x.getCodigo().equals(codigo))
                .findFirst().orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Medicamento> listener) {
        if (listener != null) listenerMedicamentos.add(listener);
    }

    private void notifyObservers(ChangeType type, Medicamento entity) {
        for (IServiceObserver<Medicamento> l : listenerMedicamentos) l.onDataChanged(type, entity);
    }
}
