package cr.ac.una.service_layer;

import cr.ac.una.data_access_layer.IFileStore;
import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class PrescripcionService implements IService<PrescripcionMedicamento> {
    private final IFileStore<PrescripcionMedicamento> prescripcionFileStore;
    private final List<IServiceObserver<PrescripcionMedicamento>> listenerPrescripciones = new ArrayList<>();

    public PrescripcionService(IFileStore<PrescripcionMedicamento> prescripcionFileStore) {
        this.prescripcionFileStore = prescripcionFileStore;
    }

    @Override
    public void agregar(PrescripcionMedicamento entity) {
        List<PrescripcionMedicamento> prescripciones = prescripcionFileStore.readAll();
        prescripciones.add(entity);
        prescripcionFileStore.writeAll(prescripciones);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void borrar(int id) {
        List<PrescripcionMedicamento> prescripciones = prescripcionFileStore.readAll();
        PrescripcionMedicamento removed = null;
        for (int i = 0; i < prescripciones.size(); i++) {
            if (prescripciones.get(i).getMedicamento().equals(String.valueOf(id))) { 
                removed = prescripciones.remove(i); 
                break; 
            }
        }
        prescripcionFileStore.writeAll(prescripciones);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    public void borrarPorMedicamento(String medicamento) {
        List<PrescripcionMedicamento> prescripciones = prescripcionFileStore.readAll();
        PrescripcionMedicamento removed = null;
        for (int i = 0; i < prescripciones.size(); i++) {
            if (prescripciones.get(i).getMedicamento().equals(medicamento)) { 
                removed = prescripciones.remove(i); 
                break; 
            }
        }
        prescripcionFileStore.writeAll(prescripciones);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    @Override
    public void actualizar(PrescripcionMedicamento entity) {
        List<PrescripcionMedicamento> prescripciones = prescripcionFileStore.readAll();
        for (int i = 0; i < prescripciones.size(); i++) {
            if (prescripciones.get(i).getMedicamento().equals(entity.getMedicamento())) {
                prescripciones.set(i, entity);
                break;
            }
        }
        prescripcionFileStore.writeAll(prescripciones);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<PrescripcionMedicamento> leerTodos() {
        return prescripcionFileStore.readAll();
    }

    @Override
    public PrescripcionMedicamento leerPorId(int id) {
        return prescripcionFileStore.readAll().stream()
                .filter(x -> x.getMedicamento().equals(String.valueOf(id)))
                .findFirst().orElse(null);
    }

    public PrescripcionMedicamento leerPorMedicamento(String medicamento) {
        return prescripcionFileStore.readAll().stream()
                .filter(x -> x.getMedicamento().equals(medicamento))
                .findFirst().orElse(null);
    }

    public List<PrescripcionMedicamento> leerPorIndicaciones(String indicaciones) {
        return prescripcionFileStore.readAll().stream()
                .filter(x -> x.getIndicaciones().toLowerCase().contains(indicaciones.toLowerCase()))
                .toList();
    }

    @Override
    public void addObserver(IServiceObserver<PrescripcionMedicamento> listener) {
        if (listener != null) listenerPrescripciones.add(listener);
    }

    private void notifyObservers(ChangeType type, PrescripcionMedicamento entity) {
        for (IServiceObserver<PrescripcionMedicamento> l : listenerPrescripciones) l.onDataChanged(type, entity);
    }
}
