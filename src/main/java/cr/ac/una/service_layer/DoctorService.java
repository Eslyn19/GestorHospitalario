package cr.ac.una.service_layer;

import cr.ac.una.data_access_layer.IFileStore;
import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class DoctorService implements IService<Doctor>{
    private final IFileStore<Doctor> fileStore;
    private final List<IServiceObserver<Doctor>> listenerDoctores = new ArrayList<>();

    public DoctorService(IFileStore<Doctor> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void agregar(Doctor entity) {
        List<Doctor> doctores = fileStore.readAll();
        
        boolean existeId = doctores.stream().anyMatch(d -> d.getID() == entity.getID());
        if (existeId) {
            throw new IllegalArgumentException("Ya existe un doctor con el ID: " + entity.getID());
        }
        
        doctores.add(entity);
        fileStore.writeAll(doctores);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void borrar(int id) {
        List<Doctor> doctores = fileStore.readAll();
        Doctor removed = null;
        for (int i = 0; i < doctores.size(); i++) {
            if (doctores.get(i).getID() == id) { removed = doctores.remove(i); break; }
        }
        fileStore.writeAll(doctores);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    @Override
    public void actualizar(Doctor entity) {
        List<Doctor> doctores = fileStore.readAll();
        
        boolean doctorExiste = doctores.stream().anyMatch(d -> d.getID() == entity.getID());
        if (!doctorExiste) {
            throw new IllegalArgumentException("No existe un doctor con el ID: " + entity.getID());
        }
        
        for (int i = 0; i < doctores.size(); i++) {
            if (doctores.get(i).getID() == entity.getID()) {
                doctores.set(i, entity);
                break;
            }
        }
        fileStore.writeAll(doctores);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<Doctor> leerTodos() {
        return fileStore.readAll();
    }

    @Override
    public Doctor leerPorId(int id) {
        return fileStore.readAll().stream().filter(x -> x.getID() == id).findFirst().orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Doctor> listener) {
        if (listener!= null) listenerDoctores.add(listener);
    }

    private void notifyObservers(ChangeType type, Doctor entity) {
        for (IServiceObserver<Doctor> l : listenerDoctores) l.onDataChanged(type, entity);
    }
}
