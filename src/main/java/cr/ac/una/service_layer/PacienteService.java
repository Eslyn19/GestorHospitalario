package cr.ac.una.service_layer;

import cr.ac.una.data_access_layer.IFileStore;
import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class PacienteService implements IService<Paciente> {
    private final IFileStore<Paciente> pacienteFileStore;
    private final List<IServiceObserver<Paciente>> listenerPacientes = new ArrayList<>();

    public PacienteService(IFileStore<Paciente> pacienteFileStore) {
        this.pacienteFileStore = pacienteFileStore;
    }

    @Override
    public void agregar(Paciente entity) {
        List<Paciente> pacientes = pacienteFileStore.readAll();
        
        boolean existeId = pacientes.stream().anyMatch(p -> p.getID() == entity.getID());
        if (existeId) {
            throw new IllegalArgumentException("Ya existe un paciente con el ID: " + entity.getID());
        }
        
        boolean existeTelefono = pacientes.stream().anyMatch(p -> p.getTelefono().equals(entity.getTelefono()));
        if (existeTelefono) {
            throw new IllegalArgumentException("Ya existe un paciente con el teléfono: " + entity.getTelefono());
        }
        
        pacientes.add(entity);
        pacienteFileStore.writeAll(pacientes);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void borrar(int id) {
        List<Paciente> pacientes = pacienteFileStore.readAll();
        Paciente removed = null;
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getID() == id) { removed = pacientes.remove(i); break; }
        }
        pacienteFileStore.writeAll(pacientes);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    @Override
    public void actualizar(Paciente entity) {
        List<Paciente> pacientes = pacienteFileStore.readAll();
        
        boolean pacienteExiste = pacientes.stream().anyMatch(p -> p.getID() == entity.getID());
        if (!pacienteExiste) {
            throw new IllegalArgumentException("No existe un paciente con el ID: " + entity.getID());
        }
        
        boolean existeTelefono = pacientes.stream()
            .anyMatch(p -> p.getID() != entity.getID() && p.getTelefono().equals(entity.getTelefono()));
        if (existeTelefono) {
            throw new IllegalArgumentException("Ya existe otro paciente con el telefono: " + entity.getTelefono());
        }
        
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getID() == entity.getID()) {
                pacientes.set(i, entity);
                break;
            }
        }
        pacienteFileStore.writeAll(pacientes);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<Paciente> leerTodos() {
        return pacienteFileStore.readAll();
    }

    @Override
    public Paciente leerPorId(int id) {
        return pacienteFileStore.readAll().stream().filter(x -> x.getID() == id).findFirst().orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Paciente> listener) {
        if (listener != null) listenerPacientes.add(listener);
    }

    private void notifyObservers(ChangeType type, Paciente entity) {
        for (IServiceObserver<Paciente> l : listenerPacientes) l.onDataChanged(type, entity);
    }
}
