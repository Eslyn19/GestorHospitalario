package cr.ac.una.service_layer;

import cr.ac.una.data_access_layer.AdminFileStore;
import cr.ac.una.domain_layer.Admin;
import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class AdminService implements IService<Admin> {
    
    private AdminFileStore fileStore;
    private final List<IServiceObserver<Admin>> ListenerAdmin = new ArrayList<>();

    public AdminService(AdminFileStore fileStore) {
        this.fileStore = fileStore;
    }
    
    @Override
    public void agregar(Admin entity) {
        List<Admin> admins = fileStore.readAll();
        admins.add(entity);
        fileStore.writeAll(admins);
        notifyObservers(ChangeType.CREATED, entity);
    }
    
    @Override
    public void borrar(int id) {
        List<Admin> admins = fileStore.readAll();
        admins.removeIf(admin -> admin.getID() == id);
        fileStore.writeAll(admins);
        notifyObservers(ChangeType.DELETED, null);
    }
    
    @Override
    public void actualizar(Admin entity) {
        List<Admin> admins = fileStore.readAll();
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getID() == entity.getID()) {
                admins.set(i, entity);
                break;
            }
        }
        fileStore.writeAll(admins);
        notifyObservers(ChangeType.UPDATED, entity);
    }
    
    @Override
    public List<Admin> leerTodos() {
        return fileStore.readAll();
    }
    
    @Override
    public Admin leerPorId(int id) {
        return fileStore.readAll().stream()
                .filter(admin -> admin.getID() == id)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public void addObserver(IServiceObserver<Admin> listener) {
        if (listener != null) ListenerAdmin.add(listener);
    }

    // Método específico para autenticación
    public boolean autenticar(int id, String clave) {
        Admin admin = leerPorId(id);
        return admin != null && admin.getClave().equals(clave);
    }

    private void notifyObservers(ChangeType type, Admin entity) {
        for (IServiceObserver<Admin> l : ListenerAdmin) l.onDataChanged(type, entity);
    }
}
